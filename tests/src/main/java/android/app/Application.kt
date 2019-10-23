package android.app

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.ComponentCallbacks
import android.content.ComponentCallbacks2
import android.content.ComponentName
import android.content.Context
import android.content.ContextForTests
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.ServiceConnection
import android.content.res.Configuration
import android.content.res.Resources
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.os.UserHandle
import android.view.Display
import java.io.InputStream

open class Application(private val context: Context) : Context()
{
   interface ActivityLifecycleCallbacks
   {

      /**
       * Called as the first step of the Activity being created. This is always called before
       * [Activity.onCreate].
       */
      fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

      /**
       * Called when the Activity calls [super.onCreate()][Activity.onCreate].
       */
      fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)

      /**
       * Called as the last step of the Activity being created. This is always called after
       * [Activity.onCreate].
       */
      fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

      /**
       * Called as the first step of the Activity being started. This is always called before
       * [Activity.onStart].
       */
      fun onActivityPreStarted(activity: Activity) = Unit

      /**
       * Called when the Activity calls [super.onStart()][Activity.onStart].
       */
      fun onActivityStarted(activity: Activity)

      /**
       * Called as the last step of the Activity being started. This is always called after
       * [Activity.onStart].
       */
      fun onActivityPostStarted(activity: Activity) = Unit

      /**
       * Called as the first step of the Activity being resumed. This is always called before
       * [Activity.onResume].
       */
      fun onActivityPreResumed(activity: Activity) = Unit

      /**
       * Called when the Activity calls [super.onResume()][Activity.onResume].
       */
      fun onActivityResumed(activity: Activity)

      /**
       * Called as the last step of the Activity being resumed. This is always called after
       * [Activity.onResume] and [Activity.onPostResume].
       */
      fun onActivityPostResumed(activity: Activity) = Unit

      /**
       * Called as the first step of the Activity being paused. This is always called before
       * [Activity.onPause].
       */
      fun onActivityPrePaused(activity: Activity) = Unit

      /**
       * Called when the Activity calls [super.onPause()][Activity.onPause].
       */
      fun onActivityPaused(activity: Activity)

      /**
       * Called as the last step of the Activity being paused. This is always called after
       * [Activity.onPause].
       */
      fun onActivityPostPaused(activity: Activity) = Unit

      /**
       * Called as the first step of the Activity being stopped. This is always called before
       * [Activity.onStop].
       */
      fun onActivityPreStopped(activity: Activity) = Unit

      /**
       * Called when the Activity calls [super.onStop()][Activity.onStop].
       */
      fun onActivityStopped(activity: Activity)

      /**
       * Called as the last step of the Activity being stopped. This is always called after
       * [Activity.onStop].
       */
      fun onActivityPostStopped(activity: Activity) = Unit

      /**
       * Called as the first step of the Activity saving its instance state. This is always
       * called before [Activity.onSaveInstanceState].
       */
      fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) = Unit

      /**
       * Called when the Activity calls
       * [super.onSaveInstanceState()][Activity.onSaveInstanceState].
       */
      fun onActivitySaveInstanceState(activity: Activity, outState: Bundle)

      /**
       * Called as the last step of the Activity saving its instance state. This is always
       * called after[Activity.onSaveInstanceState].
       */
      fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) = Unit

      /**
       * Called as the first step of the Activity being destroyed. This is always called before
       * [Activity.onDestroy].
       */
      fun onActivityPreDestroyed(activity: Activity) = Unit

      /**
       * Called when the Activity calls [super.onDestroy()][Activity.onDestroy].
       */
      fun onActivityDestroyed(activity: Activity)

      /**
       * Called as the last step of the Activity being destroyed. This is always called after
       * [Activity.onDestroy].
       */
      fun onActivityPostDestroyed(activity: Activity) = Unit
   }

   /**
    * Callback interface for use with [Application.registerOnProvideAssistDataListener]
    * and [Application.unregisterOnProvideAssistDataListener].
    */
   interface OnProvideAssistDataListener
   {
      /**
       * This is called when the user is requesting an assist, to build a full
       * [Intent.ACTION_ASSIST] Intent with all of the context of the current
       * application.  You can override this method to place into the bundle anything
       * you would like to appear in the [Intent.EXTRA_ASSIST_CONTEXT] part
       * of the assist Intent.
       */
      fun onProvideAssistData(activity: Activity, data: Bundle)
   }

   private val componentCallBacks = ArrayList<ComponentCallbacks>()
   private val componentCallBacks2 = ArrayList<ComponentCallbacks2>()
   private val activityLifecycleCallbacks = ArrayList<ActivityLifecycleCallbacks>()
   private val onProvideAssistDataListeners = ArrayList<OnProvideAssistDataListener>()

   init
   {
      println(this.javaClass.name)
   }

   constructor() : this(ContextForTests())

   internal fun fireOnActivityPreCreated(activity: Activity, savedInstanceState: Bundle?)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPreCreated(activity, savedInstanceState) }
      }
   }

   internal fun fireOnActivityCreated(activity: Activity, savedInstanceState: Bundle?)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityCreated(activity, savedInstanceState) }
      }
   }

   internal fun fireOnActivityPostCreated(activity: Activity, savedInstanceState: Bundle?)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPostCreated(activity, savedInstanceState) }
      }
   }

   internal fun fireOnActivityPreStarted(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPreStarted(activity) }
      }
   }

   internal fun fireOnActivityStarted(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityStarted(activity) }
      }
   }

   internal fun fireOnActivityPostStarted(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPostStarted(activity) }
      }
   }

   internal fun fireOnActivityPreResumed(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPreResumed(activity) }
      }
   }

   internal fun fireOnActivityResumed(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityResumed(activity) }
      }
   }

   internal fun fireOnActivityPostResumed(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPostResumed(activity) }
      }
   }

   internal fun fireOnActivityPrePaused(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPrePaused(activity) }
      }
   }

   internal fun fireOnActivityPaused(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPaused(activity) }
      }
   }

   internal fun fireOnActivityPostPaused(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPostPaused(activity) }
      }
   }

   internal fun fireOnActivityPreStopped(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPreStopped(activity) }
      }
   }

   internal fun fireOnActivityStopped(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityStopped(activity) }
      }
   }

   internal fun fireOnActivityPostStopped(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPostStopped(activity) }
      }
   }

   internal fun fireOnActivityPreSaveInstanceState(activity: Activity, outState: Bundle)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPreSaveInstanceState(activity, outState) }
      }
   }

   internal fun fireOnActivitySaveInstanceState(activity: Activity, outState: Bundle)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivitySaveInstanceState(activity, outState) }
      }
   }

   internal fun fireOnActivityPostSaveInstanceState(activity: Activity, outState: Bundle)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPostSaveInstanceState(activity, outState) }
      }
   }

   internal fun fireOnActivityPreDestroyed(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPreDestroyed(activity) }
      }
   }

   internal fun fireOnActivityDestroyed(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityDestroyed(activity) }
      }
   }

   internal fun fireOnActivityPostDestroyed(activity: Activity)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.forEach { it.onActivityPostDestroyed(activity) }
      }
   }

   internal fun fireOnProvideAssistData(activity: Activity, data: Bundle)
   {
      synchronized(this.onProvideAssistDataListeners)
      {
         this.onProvideAssistDataListeners.forEach { it.onProvideAssistData(activity, data) }
      }
   }

   open fun onCreate() = Unit

   open fun onTerminate() = Unit

   open fun onConfigurationChanged(newConfig: Configuration)
   {
      synchronized(this.componentCallBacks)
      {
         this.componentCallBacks.forEach { it.onConfigurationChanged(newConfig) }
      }

      synchronized(this.componentCallBacks2)
      {
         this.componentCallBacks2.forEach { it.onConfigurationChanged(newConfig) }
      }
   }

   open fun onLowMemory()
   {
      synchronized(this.componentCallBacks)
      {
         this.componentCallBacks.forEach { it.onLowMemory() }
      }

      synchronized(this.componentCallBacks2)
      {
         this.componentCallBacks2.forEach { it.onLowMemory() }
      }
   }

   open fun onTrimMemory(level: Int)
   {
      synchronized(this.componentCallBacks2)
      {
         this.componentCallBacks2.forEach { it.onTrimMemory(level) }
      }
   }

   override fun registerComponentCallbacks(componentCallbacks: ComponentCallbacks)
   {
      if (componentCallbacks is ComponentCallbacks2)
      {
         synchronized(this.componentCallBacks2)
         {
            this.componentCallBacks2.add(componentCallbacks)
         }
      }
      else
      {
         synchronized(this.componentCallBacks)
         {
            this.componentCallBacks.add(componentCallbacks)
         }
      }
   }

   override fun unregisterComponentCallbacks(componentCallbacks: ComponentCallbacks)
   {
      synchronized(this.componentCallBacks)
      {
         this.componentCallBacks.remove(componentCallbacks)
      }

      synchronized(this.componentCallBacks2)
      {
         this.componentCallBacks2.remove(componentCallbacks)
      }
   }

   fun registerActivityLifecycleCallbacks(activityLifecycleCallbacks: ActivityLifecycleCallbacks)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.add(activityLifecycleCallbacks)
      }
   }

   fun unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks: ActivityLifecycleCallbacks)
   {
      synchronized(this.activityLifecycleCallbacks)
      {
         this.activityLifecycleCallbacks.remove(activityLifecycleCallbacks)
      }
   }

   fun registerOnProvideAssistDataListener(onProvideAssistDataListener: OnProvideAssistDataListener)
   {
      synchronized(this.onProvideAssistDataListeners)
      {
         this.onProvideAssistDataListeners.add(onProvideAssistDataListener)
      }
   }

   fun unregisterOnProvideAssistDataListener(onProvideAssistDataListener: OnProvideAssistDataListener)
   {
      synchronized(this.onProvideAssistDataListeners)
      {
         this.onProvideAssistDataListeners.remove(onProvideAssistDataListener)
      }
   }

   override fun getApplicationContext() = this

   override fun setWallpaper(bitmap: Bitmap) =
      this.context.setWallpaper(bitmap)

   override fun setWallpaper(data: InputStream) =
      this.context.setWallpaper(data)

   @SuppressLint("MissingPermission")
   override fun removeStickyBroadcastAsUser(intent: Intent, user: UserHandle) =
      this.context.removeStickyBroadcastAsUser(intent, user)

   override fun checkCallingOrSelfPermission(permission: String) =
      this.context.checkCallingOrSelfPermission(permission)

   override fun getClassLoader(): ClassLoader = this.javaClass.classLoader!!

   override fun checkCallingOrSelfUriPermission(uri: Uri, modeFlags: Int) =
      this.context.checkCallingOrSelfUriPermission(uri, modeFlags)

   override fun getObbDir() =
      this.context.getObbDir()

   override fun checkUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int) =
      this.context.checkUriPermission(uri, pid, uid, modeFlags)

   override fun checkUriPermission(
      uri: Uri?,
      readPermission: String?,
      writePermission: String?,
      pid: Int,
      uid: Int,
      modeFlags: Int
                                  ) =
      this.context.checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags)

   override fun getExternalFilesDirs(type: String) =
      this.context.getExternalFilesDirs(type)

   override fun getPackageResourcePath() =
      this.context.getPackageResourcePath()

   @TargetApi(VERSION_CODES.N)
   override fun deleteSharedPreferences(name: String) =
      this.context.deleteSharedPreferences(name)

   override fun checkPermission(permission: String, pid: Int, uid: Int) =
      this.context.checkPermission(permission, pid, uid)

   override fun startIntentSender(
      intent: IntentSender,
      fillInIntent: Intent?,
      flagsMask: Int,
      flagsValues: Int,
      extraFlags: Int
                                 ) =
      this.context.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags)

   override fun startIntentSender(
      intent: IntentSender,
      fillInIntent: Intent?,
      flagsMask: Int,
      flagsValues: Int,
      extraFlags: Int,
      options: Bundle?
                                 ) =
      this.context.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options)

   override fun getSharedPreferences(name: String, mode: Int) =
      this.context.getSharedPreferences(name, mode)

   @SuppressLint("MissingPermission")
   override fun sendStickyBroadcastAsUser(intent: Intent, user: UserHandle) =
      this.context.sendStickyBroadcastAsUser(intent, user)

   @TargetApi(VERSION_CODES.N)
   override fun getDataDir() =
      this.context.getDataDir()

   override fun getWallpaper() =
      this.context.getWallpaper()

   @TargetApi(VERSION_CODES.N)
   override fun isDeviceProtectedStorage() =
      this.context.isDeviceProtectedStorage()

   override fun getExternalFilesDir(type: String?) =
      this.context.getExternalFilesDir(type)

   @SuppressLint("MissingPermission")
   override fun sendBroadcastAsUser(intent: Intent, user: UserHandle) =
      this.context.sendBroadcastAsUser(intent, user)

   @SuppressLint("MissingPermission")
   override fun sendBroadcastAsUser(intent: Intent, user: UserHandle, receiverPermission: String?) =
      this.context.sendBroadcastAsUser(intent, user, receiverPermission)

   override fun getExternalCacheDir() =
      this.context.getExternalCacheDir()

   override fun getDatabasePath(name: String) =
      this.context.getDatabasePath(name)

   override fun getFileStreamPath(name: String) =
      this.context.getFileStreamPath(name)

   override fun stopService(service: Intent) =
      this.context.stopService(service)

   @TargetApi(VERSION_CODES.M)
   override fun checkSelfPermission(permission: String) =
      this.context.checkSelfPermission(permission)

   override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter) =
      this.context.registerReceiver(receiver, filter)

   @TargetApi(VERSION_CODES.O)
   override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter, flags: Int) =
      this.context.registerReceiver(receiver, filter, flags)

   override fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter,
      broadcastPermission: String?,
      scheduler: Handler?
                                ) =
      this.context.registerReceiver(receiver, filter, broadcastPermission, scheduler)

   @TargetApi(VERSION_CODES.O)
   override fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter,
      broadcastPermission: String?,
      scheduler: Handler?,
      flags: Int
                                ) =
      this.context.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags)

   @TargetApi(VERSION_CODES.M)
   override fun getSystemServiceName(serviceClass: Class<*>) =
      this.context.getSystemServiceName(serviceClass)

   override fun getMainLooper() =
      this.context.getMainLooper()

   override fun enforceCallingOrSelfPermission(permission: String, message: String?) =
      this.context.enforceCallingOrSelfPermission(permission, message)

   override fun getPackageCodePath() =
      this.context.getPackageCodePath()

   override fun checkCallingUriPermission(uri: Uri, modeFlags: Int) =
      this.context.checkCallingUriPermission(uri, modeFlags)

   override fun getWallpaperDesiredMinimumWidth() =
      this.context.getWallpaperDesiredMinimumWidth()

   @TargetApi(VERSION_CODES.N)
   override fun createDeviceProtectedStorageContext() =
      this.context.createDeviceProtectedStorageContext()

   override fun openFileInput(name: String) =
      this.context.openFileInput(name)

   @TargetApi(VERSION_CODES.LOLLIPOP)
   override fun getCodeCacheDir() =
      this.context.getCodeCacheDir()

   override fun bindService(service: Intent, conn: ServiceConnection, flags: Int) =
      this.context.bindService(service, conn, flags)

   override fun deleteDatabase(name: String) =
      this.context.deleteDatabase(name)

   override fun getAssets() =
      this.context.getAssets()

   @TargetApi(VERSION_CODES.LOLLIPOP)
   override fun getNoBackupFilesDir() =
      this.context.getNoBackupFilesDir()

   override fun startActivities(intents: Array<Intent>) =
      this.context.startActivities(intents)

   override fun startActivities(intents: Array<Intent>, options: Bundle?) =
      this.context.startActivities(intents, options)

   override fun getResources() =
      this.context.getResources()

   override fun fileList() =
      this.context.fileList()

   override fun setTheme(resid: Int) =
      this.context.setTheme(resid)

   override fun unregisterReceiver(receiver: BroadcastReceiver) =
      this.context.unregisterReceiver(receiver)

   override fun enforcePermission(permission: String, pid: Int, uid: Int, message: String?) =
      this.context.enforcePermission(permission, pid, uid, message)

   override fun openFileOutput(name: String, mode: Int) =
      this.context.openFileOutput(name, mode)

   @SuppressLint("MissingPermission")
   override fun sendStickyOrderedBroadcast(
      intent: Intent,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                          ) =
      this.context.sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData, initialExtras)

   override fun createConfigurationContext(overrideConfiguration: Configuration) =
      this.context.createConfigurationContext(overrideConfiguration)

   override fun getFilesDir() =
      this.context.getFilesDir()

   override fun sendBroadcast(intent: Intent) =
      this.context.sendBroadcast(intent)

   override fun sendBroadcast(intent: Intent, receiverPermission: String?) =
      this.context.sendBroadcast(intent, receiverPermission)

   @SuppressLint("MissingPermission")
   override fun sendOrderedBroadcastAsUser(
      intent: Intent,
      user: UserHandle,
      receiverPermission: String?,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                          ) =
      this.context.sendOrderedBroadcastAsUser(
         intent,
         user,
         receiverPermission,
         resultReceiver,
         scheduler,
         initialCode,
         initialData,
         initialExtras
                                             )

   override fun grantUriPermission(toPackage: String, uri: Uri, modeFlags: Int) =
      this.context.grantUriPermission(toPackage, uri, modeFlags)

   override fun enforceCallingUriPermission(uri: Uri, modeFlags: Int, message: String?) =
      this.context.enforceCallingUriPermission(uri, modeFlags, message)

   override fun getCacheDir() =
      this.context.getCacheDir()

   override fun clearWallpaper() =
      this.context.clearWallpaper()

   @SuppressLint("MissingPermission")
   override fun sendStickyOrderedBroadcastAsUser(
      intent: Intent,
      user: UserHandle,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                                ) =
      this.context.sendStickyOrderedBroadcastAsUser(
         intent,
         user,
         resultReceiver,
         scheduler,
         initialCode,
         initialData,
         initialExtras
                                                   )

   override fun startActivity(intent: Intent) =
      this.context.startActivity(intent)

   override fun startActivity(intent: Intent, options: Bundle?) =
      this.context.startActivity(intent, options)

   override fun getPackageManager() =
      this.context.getPackageManager()

   override fun openOrCreateDatabase(name: String, mode: Int, factory: CursorFactory?) =
      this.context.openOrCreateDatabase(name, mode, factory)

   override fun openOrCreateDatabase(
      name: String,
      mode: Int,
      factory: CursorFactory?,
      errorHandler: DatabaseErrorHandler?
                                    ) =
      this.context.openOrCreateDatabase(name, mode, factory, errorHandler)

   override fun deleteFile(name: String) =
      this.context.deleteFile(name)

   override fun startService(service: Intent) =
      this.context.startService(service)

   override fun revokeUriPermission(uri: Uri, modeFlags: Int) =
      this.context.revokeUriPermission(uri, modeFlags)

   @TargetApi(VERSION_CODES.O)
   override fun revokeUriPermission(toPackage: String, uri: Uri, modeFlags: Int) =
      this.context.revokeUriPermission(toPackage, uri, modeFlags)

   @TargetApi(VERSION_CODES.N)
   override fun moveDatabaseFrom(sourceContext: Context, name: String) =
      this.context.moveDatabaseFrom(sourceContext, name)

   override fun startInstrumentation(className: ComponentName, profileFile: String?, arguments: Bundle?) =
      this.context.startInstrumentation(className, profileFile, arguments)

   override fun sendOrderedBroadcast(intent: Intent, receiverPermission: String?) =
      this.context.sendOrderedBroadcast(intent, receiverPermission)

   override fun sendOrderedBroadcast(
      intent: Intent,
      receiverPermission: String?,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                    ) =
      this.context.sendOrderedBroadcast(
         intent,
         receiverPermission,
         resultReceiver,
         scheduler,
         initialCode,
         initialData,
         initialExtras
                                       )

   override fun unbindService(conn: ServiceConnection) =
      this.context.unbindService(conn)

   override fun getApplicationInfo() =
      this.context.getApplicationInfo()

   override fun getWallpaperDesiredMinimumHeight() =
      this.context.getWallpaperDesiredMinimumHeight()

   override fun createDisplayContext(display: Display) =
      this.context.createDisplayContext(display)

   @TargetApi(VERSION_CODES.O)
   override fun createContextForSplit(splitName: String) =
      this.context.createContextForSplit(splitName)

   override fun getTheme(): Resources.Theme =
      this.context.getTheme()

   override fun getPackageName() =
      this.context.getPackageName()

   override fun getContentResolver() =
      this.context.getContentResolver()

   override fun getObbDirs() =
      this.context.getObbDirs()

   override fun enforceCallingOrSelfUriPermission(uri: Uri, modeFlags: Int, message: String?) =
      this.context.enforceCallingOrSelfUriPermission(uri, modeFlags, message)

   @TargetApi(VERSION_CODES.N)
   override fun moveSharedPreferencesFrom(sourceContext: Context, name: String) =
      this.context.moveSharedPreferencesFrom(sourceContext, name)

   @TargetApi(VERSION_CODES.LOLLIPOP)
   override fun getExternalMediaDirs() =
      this.context.getExternalMediaDirs()

   override fun checkCallingPermission(permission: String) =
      this.context.checkCallingPermission(permission)

   override fun getExternalCacheDirs() =
      this.context.getExternalCacheDirs()

   @SuppressLint("MissingPermission")
   override fun sendStickyBroadcast(intent: Intent) =
      this.context.sendStickyBroadcast(intent)

   override fun enforceCallingPermission(permission: String, message: String?) =
      this.context.enforceCallingPermission(permission, message)

   override fun peekWallpaper() =
      this.context.peekWallpaper()

   override fun getSystemService(name: String) =
      this.context.getSystemService(name)

   @TargetApi(VERSION_CODES.O)
   override fun startForegroundService(service: Intent) =
      this.context.startForegroundService(service)

   override fun getDir(name: String, mode: Int) =
      this.context.getDir(name, mode)

   override fun databaseList() =
      this.context.databaseList()

   override fun createPackageContext(packageName: String, flags: Int) =
      this.context.createPackageContext(packageName, flags)

   override fun enforceUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int, message: String?) =
      this.context.enforceUriPermission(uri, pid, uid, modeFlags, message)

   override fun enforceUriPermission(
      uri: Uri?,
      readPermission: String?,
      writePermission: String?,
      pid: Int,
      uid: Int,
      modeFlags: Int,
      message: String?
                                    ) =
      this.context.enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message)

   @SuppressLint("MissingPermission")
   override fun removeStickyBroadcast(intent: Intent) =
      this.context.removeStickyBroadcast(intent)

}