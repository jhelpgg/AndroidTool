package jhelp.thread.permission

import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import jhelp.thread.pools.CancelableTask
import jhelp.thread.pools.delayed
import jhelp.thread.pools.parallel
import jhelp.thread.promise.FutureResult
import jhelp.thread.promise.Promise
import jhelp.thread.promise.future
import jhelp.thread.promise.futureFailed
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Manage permissions and call registered tasks.
 *
 * To initialize it, two strategies are recommended (Choose the one fit more to your application):
 * 1. Create a class that extends [Application] and call [PermissionManager.initialize] inside the
 *    [Application.onCreate] method (Don't forget to adapt AndroidManifest). This way, the initialization is made even
 *    if application is start on background, via a JobScheduler by example.
 *
 * 1. Call [PermissionManager.initialize] inside ech [Activity.onCreate] of activities can start first.
 *    With this approach, the system work only when user use tha application.
 *
 * Initialization is mandatory, the system can't work without it.
 *
 * To do a task with a specific permission, just call [PermissionManager.onPermission], then the system will do the work.
 * That is to say:
 * 1. If permission granted, do the task immediately
 * 1. If permission is denied, push a error in [FutureResult]
 * 1. If permission not ask and need user validation, show the Android popup, wait user decision abd play tadk or push
 * error depends if user accept or refuse?
 *
 */
object PermissionManager
{
   private val lock = Object()
   private val initialized = AtomicBoolean(false)
   private val permissionsStatus = HashMap<String, PermissionStatus>()
   private var permissionRequestStatus = PermissionRequestStatus.NO_REQUEST
   private val permissionsToAsk = ArrayList<String>()
   private val permissionsRequested = ArrayList<String>()
   private val tasksWaitPermissions = HashMap<String, ArrayList<Promise<Unit>>>()
   private var askPermissionsTask: CancelableTask? = null
   private val allowAskPermission = HashMap<String, (BeforeAskPermissionAction) -> Unit>()

   /**
    * Initialize from [Application]
    */
   fun initialize(application: Application)
   {
      if (this.initialized.getAndSet(true))
      {
         return
      }

      application.registerActivityLifecycleCallbacks(ApplicationLifeCycleListener)
   }

   /**
    * Initialize from [Activity]
    */
   fun initialize(activity: Activity) = this.initialize(activity.application)

   /**
    * Compute new current permission status
    */
   private fun checkPermissionStatus(
      permission: String,
      currentStatus: PermissionStatus = PermissionStatus.TO_ASK
                                    ): PermissionStatus
   {
      val activity = ApplicationLifeCycleListener.currentActivity()
         ?: return currentStatus

      return if (VERSION.SDK_INT >= VERSION_CODES.M)
      {
         when
         {
            activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED -> PermissionStatus.GRANTED
            !activity.shouldShowRequestPermissionRationale(permission)                    -> PermissionStatus.TO_ASK
            else                                                                          -> PermissionStatus.DENIED
         }
      }
      else
      {
         PermissionStatus.GRANTED
      }
   }

   /**
    * Current status of given permission
    */
   fun permissionStatus(permission: String) =
      synchronized(this.permissionsStatus)
      {
         this.permissionsStatus.getOrPut(permission,
                                         {
                                            this.checkPermissionStatus(permission)
                                         })
      }

   @TargetApi(VERSION_CODES.M)
   private fun requestPermissions()
   {
      synchronized(this.lock)
      {
         if (this.permissionRequestStatus == PermissionRequestStatus.REQUESTING)
         {
            this.askPermissionsTask = this::requestPermissions.delayed(2048)
            return
         }

         if (this.permissionsToAsk.isEmpty())
         {
            return
         }

         this.permissionRequestStatus = PermissionRequestStatus.REQUESTING
         val activity = ApplicationLifeCycleListener.currentActivity()
            ?: return
         val permissions = this.permissionsToAsk.toTypedArray()
         this.permissionsRequested.addAll(this.permissionsToAsk)
         this.permissionsToAsk.clear()
         activity.requestPermissions(permissions, 123456789)
      }
   }

   internal fun updatePermissions()
   {
      synchronized(this.lock)
      {
         if (this.permissionRequestStatus != PermissionRequestStatus.REQUESTING)
         {
            return
         }

         this.permissionRequestStatus = PermissionRequestStatus.REQUEST_DONE

         for (permission in this.permissionsRequested)
         {
            val permissionStatus = this.checkPermissionStatus(permission, this.permissionStatus(permission))
            this.permissionsStatus[permission] = permissionStatus
            val taskList = this.tasksWaitPermissions.remove(permission)
               ?: continue

            if (permissionStatus == PermissionStatus.GRANTED)
            {
               taskList.forEach { it.result(Unit) }
            }
            else
            {
               val permissionException = PermissionDeniedException(permission)
               taskList.forEach { it.error(permissionException) }
            }
         }

         this.permissionsRequested.clear()
      }
   }

   private fun askPermissionIfAllowed(permission: String, task: () -> Unit): FutureResult<Unit>
   {
      synchronized(this.allowAskPermission)
      {
         val taskAllow = this.allowAskPermission.remove(permission)

         if (taskAllow != null)
         {
            return this.alertBeforeAskPermission(permission, task, taskAllow)
         }
      }

      return this.askPermission(permission, task)
   }

   private fun denyPermission(permission: String)
   {
      synchronized(this.permissionsStatus)
      {
         this.permissionsStatus[permission] = PermissionStatus.DENIED
      }
   }

   private fun alertBeforeAskPermission(
      permission: String,
      task: () -> Unit,
      taskAllow: (BeforeAskPermissionAction) -> Unit
                                       ): FutureResult<Unit>
   {
      val promise = Promise<Pair<String, () -> Unit>>()
      val beforeAskPermissionAction = BeforeAskPermissionAction(permission, task, promise)
      val future =
         promise.futureResult
            .thenUnwrap { (permission, task) -> askPermission(permission, task) }
            .onError { denyPermission(it.message!!) }

      ({ taskAllow(beforeAskPermissionAction) }).parallel()

      return future
   }

   private fun askPermission(permission: String, task: () -> Unit): FutureResult<Unit>
   {
      val promise = Promise<Unit>()

      synchronized(this.lock) {
         this.askPermissionsTask?.cancel()
         val permissionStatus = this.permissionStatus(permission)

         if (permissionStatus == PermissionStatus.TO_ASK)
         {
            this.permissionsToAsk.add(permission)
            this.permissionsStatus[permission] = PermissionStatus.ASKING
         }

         this.tasksWaitPermissions.getOrPut(permission, { ArrayList() }).add(promise)
         this.askPermissionsTask = this::requestPermissions.delayed(2048)
      }

      return promise.futureResult.then { task() }
   }

   /**
    * Play task when permission granted by user.
    *
    * Return a [FutureResult] to be able do something else after the given task.
    * The [FutureResult] is put on error if permission denied, so its also possible to react to it.
    */
   fun onPermission(permission: String, task: () -> Unit): FutureResult<Unit> =
      when (this.permissionStatus(permission))
      {
         PermissionStatus.GRANTED -> task.future()
         PermissionStatus.DENIED  -> futureFailed(PermissionDeniedException(permission))
         PermissionStatus.TO_ASK  -> this.askPermissionIfAllowed(permission, task)
         PermissionStatus.ASKING  -> this.askPermission(permission, task)
      }

   fun registerForReactJustBeforeAskPermissionToUser(permission: String, task: (BeforeAskPermissionAction) -> Unit)
   {
      if (this.permissionStatus(permission) == PermissionStatus.TO_ASK)
      {
         synchronized(this.allowAskPermission)
         {
            this.allowAskPermission[permission] = task
         }
      }
   }
}

/**
 * Play task when permission granted by user.
 *
 * Return a [FutureResult] to be able do something else after the given task.
 * The [FutureResult] is put on error if permission denied, so its also possible to react to it.
 */
fun <R : Any> (() -> R).onPermission(permission: String) =
   PermissionManager.onPermission(permission) {}.then { this() }

/**
 * Create a function that will play task when permission granted by user.
 *
 * Created function will return a [FutureResult] to be able do something else after the given task.
 * The [FutureResult] is put on error if permission denied, so its also possible to react to it.
 */
fun <P, R : Any> ((P) -> R).onPermission(permission: String): (P) -> FutureResult<R> =
   { parameter: P -> PermissionManager.onPermission(permission) {}.then { this(parameter) } }

/**
 * For task that need more than one permission
 */
fun <R : Any> (() -> R).onPermissions(vararg permissions: String): FutureResult<R>
{
   return when
   {
      permissions.isEmpty() -> this.future()
      permissions.size == 1 -> this.onPermission(permissions[0])
      else                  ->
         ({ this.onPermission(permissions[0])() })
            .onPermissions(*permissions.copyOfRange(1, permissions.size))
   }
}