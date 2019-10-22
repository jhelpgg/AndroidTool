package jhelp.thread.permission

import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import jhelp.thread.pools.CancelableTask
import jhelp.thread.pools.delayed
import jhelp.thread.promise.FutureResult
import jhelp.thread.promise.Promise
import jhelp.thread.promise.future
import jhelp.thread.promise.futureFailed
import java.util.concurrent.atomic.AtomicBoolean

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

   fun initialize(application: Application)
   {
      if (this.initialized.getAndSet(true))
      {
         return
      }

      application.registerActivityLifecycleCallbacks(ApplicationLifeCycleListener)
   }

   fun initialize(activity: Activity) = this.initialize(activity.application)

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
            currentStatus == PermissionStatus.ASKING || currentStatus == PermissionStatus.DENIED
                                                                                          -> PermissionStatus.DENIED
            else                                                                          -> PermissionStatus.TO_ASK
         }
      }
      else
      {
         PermissionStatus.GRANTED
      }
   }

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

   fun onPermission(permission: String, task: () -> Unit): FutureResult<Unit> =
      when (this.permissionStatus(permission))
      {
         PermissionStatus.GRANTED -> task.future()
         PermissionStatus.DENIED  -> futureFailed(PermissionDeniedException(permission))
         PermissionStatus.TO_ASK  -> this.askPermission(permission, task)
         PermissionStatus.ASKING  -> this.askPermission(permission, task)
      }
}

fun <R : Any> (() -> R).onPermission(permission: String) =
   PermissionManager.onPermission(permission) {}.then { this() }

fun <P, R : Any> ((P) -> R).onPermission(permission: String): (P) -> FutureResult<R> =
   { parameter: P -> PermissionManager.onPermission(permission) {}.then { this(parameter) } }

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