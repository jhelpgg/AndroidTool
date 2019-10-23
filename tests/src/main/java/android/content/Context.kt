package android.content

import android.annotation.TargetApi
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.TypedArray
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import android.util.AttributeSet
import android.view.Display
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.Executor
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.atomic.AtomicInteger

abstract class Context
{
   companion object
   {
      @JvmStatic
      val MODE_PRIVATE = 0x0000

      @JvmStatic
      val MODE_WORLD_READABLE = 0x0001

      @JvmStatic
      val MODE_WORLD_WRITEABLE = 0x0002

      @JvmStatic
      val MODE_APPEND = 0x8000

      @JvmStatic
      val MODE_MULTI_PROCESS = 0x0004

      @JvmStatic
      val MODE_ENABLE_WRITE_AHEAD_LOGGING = 0x0008

      @JvmStatic
      val MODE_NO_LOCALIZED_COLLATORS = 0x0010

      @JvmStatic
      val BIND_AUTO_CREATE = 0x0001

      @JvmStatic
      val BIND_DEBUG_UNBIND = 0x0002

      @JvmStatic
      val BIND_NOT_FOREGROUND = 0x0004

      @JvmStatic
      val BIND_ABOVE_CLIENT = 0x0008

      @JvmStatic
      val BIND_ALLOW_OOM_MANAGEMENT = 0x0010

      @JvmStatic
      val BIND_WAIVE_PRIORITY = 0x0020

      @JvmStatic
      val BIND_IMPORTANT = 0x0040

      @JvmStatic
      val BIND_ADJUST_WITH_ACTIVITY = 0x0080

      @JvmStatic
      val BIND_NOT_PERCEPTIBLE = 0x00000100

      @JvmStatic
      val BIND_INCLUDE_CAPABILITIES = 0x000001000

      @JvmStatic
      val POWER_SERVICE = "power"

      @JvmStatic
      val WINDOW_SERVICE = "window"

      @JvmStatic
      val LAYOUT_INFLATER_SERVICE = "layout_inflater"

      @JvmStatic
      val ACCOUNT_SERVICE = "account"

      @JvmStatic
      val ACTIVITY_SERVICE = "activity"

      @JvmStatic
      val ALARM_SERVICE = "alarm"

      @JvmStatic
      val NOTIFICATION_SERVICE = "notification"

      @JvmStatic
      val ACCESSIBILITY_SERVICE = "accessibility"

      @JvmStatic
      val CAPTIONING_SERVICE = "captioning"

      @JvmStatic
      val KEYGUARD_SERVICE = "keyguard"

      @JvmStatic
      val LOCATION_SERVICE = "location"

      @JvmStatic
      val SEARCH_SERVICE = "search"

      @JvmStatic
      val SENSOR_SERVICE = "sensor"

      @JvmStatic
      val STORAGE_SERVICE = "storage"

      @JvmStatic
      val STORAGE_STATS_SERVICE = "storagestats"

      @JvmStatic
      val WALLPAPER_SERVICE = "wallpaper"

      @JvmStatic
      val VIBRATOR_SERVICE = "vibrator"

      @JvmStatic
      val CONNECTIVITY_SERVICE = "connectivity"

      @JvmStatic
      val IPSEC_SERVICE = "ipsec"

      @JvmStatic
      val NETWORK_STATS_SERVICE = "netstats"

      @JvmStatic
      val WIFI_SERVICE = "wifi"

      @JvmStatic
      val WIFI_P2P_SERVICE = "wifip2p"

      @JvmStatic
      val WIFI_AWARE_SERVICE = "wifiaware"

      @JvmStatic
      val WIFI_RTT_RANGING_SERVICE = "wifirtt"

      @JvmStatic
      val NSD_SERVICE = "servicediscovery"

      @JvmStatic
      val AUDIO_SERVICE = "audio"

      @JvmStatic
      val FINGERPRINT_SERVICE = "fingerprint"

      @JvmStatic
      val BIOMETRIC_SERVICE = "biometric"

      @JvmStatic
      val MEDIA_ROUTER_SERVICE = "media_router"

      @JvmStatic
      val MEDIA_SESSION_SERVICE = "media_session"

      @JvmStatic
      val TELEPHONY_SERVICE = "phone"

      @JvmStatic
      val TELEPHONY_SUBSCRIPTION_SERVICE = "telephony_subscription_service"

      @JvmStatic
      val TELECOM_SERVICE = "telecom"

      @JvmStatic
      val CARRIER_CONFIG_SERVICE = "carrier_config"

      @JvmStatic
      val EUICC_SERVICE = "euicc"

      @JvmStatic
      val CLIPBOARD_SERVICE = "clipboard"

      @JvmStatic
      val TEXT_CLASSIFICATION_SERVICE = "textclassification"

      @JvmStatic
      val INPUT_METHOD_SERVICE = "input_method"

      @JvmStatic
      val TEXT_SERVICES_MANAGER_SERVICE = "textservices"

      @JvmStatic
      val APPWIDGET_SERVICE = "appwidget"

      @JvmStatic
      val DEVICE_POLICY_SERVICE = "device_policy"

      @JvmStatic
      val UI_MODE_SERVICE = "uimode"

      @JvmStatic
      val DOWNLOAD_SERVICE = "download"

      @JvmStatic
      val BATTERY_SERVICE = "batterymanager"

      @JvmStatic
      val NFC_SERVICE = "nfc"

      @JvmStatic
      val BLUETOOTH_SERVICE = "bluetooth"

      @JvmStatic
      val USB_SERVICE = "usb"

      @JvmStatic
      val ADB_SERVICE = "adb"

      @JvmStatic
      val INPUT_SERVICE = "input"

      @JvmStatic
      val DISPLAY_SERVICE = "display"

      @JvmStatic
      val USER_SERVICE = "user"

      @JvmStatic
      val LAUNCHER_APPS_SERVICE = "launcherapps"

      @JvmStatic
      val RESTRICTIONS_SERVICE = "restrictions"

      @JvmStatic
      val APP_OPS_SERVICE = "appops"

      @JvmStatic
      val ROLE_SERVICE = "role"

      @JvmStatic
      val CAMERA_SERVICE = "camera"

      @JvmStatic
      val PRINT_SERVICE = "print"

      @JvmStatic
      val COMPANION_DEVICE_SERVICE = "companiondevice"

      @JvmStatic
      val CONSUMER_IR_SERVICE = "consumer_ir"

      @JvmStatic
      val TV_INPUT_SERVICE = "tv_input"

      @JvmStatic
      val USAGE_STATS_SERVICE = "usagestats"

      @JvmStatic
      val JOB_SCHEDULER_SERVICE = "jobscheduler"

      @JvmStatic
      val MEDIA_PROJECTION_SERVICE = "media_projection"

      @JvmStatic
      val MIDI_SERVICE = "midi"

      @JvmStatic
      val HARDWARE_PROPERTIES_SERVICE = "hardware_properties"

      @JvmStatic
      val SHORTCUT_SERVICE = "shortcut"

      @JvmStatic
      val SYSTEM_HEALTH_SERVICE = "systemhealth"

      @JvmStatic
      val CROSS_PROFILE_APPS_SERVICE = "crossprofileapps"

      val nextID = AtomicInteger(1)

      @JvmStatic
      val CONTEXT_INCLUDE_CODE = 0x00000001

      @JvmStatic
      val CONTEXT_IGNORE_SECURITY = 0x00000002

      @JvmStatic
      val CONTEXT_RESTRICTED = 0x00000004

      @JvmStatic
      val SENSOR_PRIVACY_SERVICE = "sensor_privacy"
      @JvmStatic
      val TIME_ZONE_RULES_MANAGER_SERVICE = "time_zone_rules_manager"
      @JvmStatic
      val TEST_NETWORK_SERVICE = "test_network"
      @JvmStatic
      val WIFI_P2_SERVICE = "wifi_p2"
      @JvmStatic
      val P_SERVICE = "p"
      @JvmStatic
      val WIFI_SCANNING_SERVICE = "wifi_scanning"
      @JvmStatic
      val RADIO_SERVICE = "radio"
      @JvmStatic
      val PERMISSION_SERVICE = "permission"
      @JvmStatic
      val DROPBOX_SERVICE = "dropbox"
   }

   abstract fun getAssets(): AssetManager

   abstract fun getResources(): Resources

   abstract fun getPackageManager(): PackageManager

   abstract fun getContentResolver(): ContentResolver

   abstract fun getMainLooper(): Looper

   abstract fun removeStickyBroadcastAsUser(intent: Intent, user: UserHandle)

   open fun getMainExecutor(): Executor
   {
      return ScheduledThreadPoolExecutor(1)
   }

   abstract fun getApplicationContext(): Context

   open fun getNextAutofillId() = nextID.getAndIncrement()

   open fun registerComponentCallbacks(callback: ComponentCallbacks) = Unit

   open fun unregisterComponentCallbacks(callback: ComponentCallbacks) = Unit

   open fun getText(@StringRes resId: Int): CharSequence
   {
      return getResources().getText(resId)
   }

   open fun getString(@StringRes resId: Int): String
   {
      return getResources().getString(resId)
   }

   open fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
   {
      return getResources().getString(resId, *formatArgs)
   }

   @TargetApi(VERSION_CODES.M)
   @ColorInt
   open fun getColor(@ColorRes id: Int): Int
   {
      return getResources().getColor(id, getTheme())
   }

   @TargetApi(VERSION_CODES.LOLLIPOP)
   open fun getDrawable(@DrawableRes id: Int): Drawable?
   {
      return getResources().getDrawable(id, getTheme())
   }

   @TargetApi(VERSION_CODES.M)
   open fun getColorStateList(@ColorRes id: Int): ColorStateList
   {
      return getResources().getColorStateList(id, getTheme())
   }

   abstract fun setTheme(@StyleRes resid: Int)

   open fun getThemeResId() = 0

   abstract fun getTheme(): Resources.Theme

   open fun obtainStyledAttributes(@StyleableRes attrs: IntArray): TypedArray
   {
      return getTheme().obtainStyledAttributes(attrs) //TypedArray
   }

   open fun obtainStyledAttributes(
      @StyleRes resid: Int,
      @StyleableRes attrs: IntArray
                                  ): TypedArray
   {
      return getTheme().obtainStyledAttributes(resid, attrs)
   }

   open fun obtainStyledAttributes(
      set: AttributeSet?, @StyleableRes attrs: IntArray
                                  ): TypedArray
   {
      return getTheme().obtainStyledAttributes(set, attrs, 0, 0)
   }

   open fun obtainStyledAttributes(
      set: AttributeSet?,
      @StyleableRes attrs: IntArray, @AttrRes defStyleAttr: Int,
      @StyleRes defStyleRes: Int
                                  ): TypedArray
   {
      return getTheme().obtainStyledAttributes(
         set, attrs, defStyleAttr, defStyleRes
                                              )
   }

   abstract fun getClassLoader(): ClassLoader

   abstract fun getPackageName(): String

   open fun getOpPackageName(): String
   {
      throw RuntimeException("Not implemented. Must override in a subclass.")
   }

   abstract fun getApplicationInfo(): ApplicationInfo

   abstract fun getPackageResourcePath(): String

   abstract fun getPackageCodePath(): String

   abstract fun getSharedPreferences(name: String, mode: Int): SharedPreferences

   abstract fun moveSharedPreferencesFrom(sourceContext: Context, name: String): Boolean

   abstract fun deleteSharedPreferences(name: String): Boolean

   abstract fun openFileInput(name: String): FileInputStream

   abstract fun openFileOutput(name: String, mode: Int): FileOutputStream

   abstract fun deleteFile(name: String): Boolean

   abstract fun getFileStreamPath(name: String): File

   abstract fun getDataDir(): File

   abstract fun getFilesDir(): File

   abstract fun getNoBackupFilesDir(): File

   abstract fun getExternalFilesDir(type: String?): File?

   abstract fun getExternalFilesDirs(type: String): Array<File>

   abstract fun getObbDir(): File

   abstract fun getObbDirs(): Array<File>

   abstract fun getCacheDir(): File

   abstract fun getCodeCacheDir(): File

   abstract fun getExternalCacheDir(): File?

   abstract fun getExternalCacheDirs(): Array<File>

   abstract fun getExternalMediaDirs(): Array<File>

   abstract fun fileList(): Array<String>

   abstract fun getDir(name: String, mode: Int): File

   abstract fun openOrCreateDatabase(
      name: String,
      mode: Int, factory: CursorFactory?
                                    ): SQLiteDatabase

   abstract fun openOrCreateDatabase(
      name: String,
      mode: Int, factory: CursorFactory?,
      errorHandler: DatabaseErrorHandler?
                                    ): SQLiteDatabase

   abstract fun moveDatabaseFrom(sourceContext: Context, name: String): Boolean

   abstract fun deleteDatabase(name: String): Boolean

   abstract fun getDatabasePath(name: String): File

   abstract fun databaseList(): Array<String>

   abstract fun getWallpaper(): Drawable

   abstract fun peekWallpaper(): Drawable

   abstract fun getWallpaperDesiredMinimumWidth(): Int

   abstract fun getWallpaperDesiredMinimumHeight(): Int

   abstract fun setWallpaper(bitmap: Bitmap)

   abstract fun setWallpaper(data: InputStream)

   abstract fun clearWallpaper()

   abstract fun startActivity( intent: Intent)

   abstract fun startActivity(
      intent: Intent,
      options: Bundle?
                             )

   abstract fun startActivities( intents: Array<Intent>)

   abstract fun startActivities( intents: Array<Intent>, options: Bundle?)

   abstract fun startIntentSender(
      intent: IntentSender, fillInIntent: Intent?,
      flagsMask: Int, flagsValues: Int,
      extraFlags: Int
                                 )

   abstract fun startIntentSender(
      intent: IntentSender, fillInIntent: Intent?,
      flagsMask: Int, flagsValues: Int,
      extraFlags: Int, options: Bundle?
                                 )

   abstract fun sendBroadcast( intent: Intent)

   abstract fun sendBroadcast(
      intent: Intent,
      receiverPermission: String?
                             )

   abstract fun sendOrderedBroadcast(
      intent: Intent,
      receiverPermission: String?
                                    )

   abstract fun sendOrderedBroadcast(
      intent: Intent,
      receiverPermission: String?, resultReceiver: BroadcastReceiver?,
      scheduler: Handler?, initialCode: Int, initialData: String?,
      initialExtras: Bundle?
                                    )

   abstract fun sendBroadcastAsUser(
      intent: Intent,
      user: UserHandle
                                   )

   abstract fun sendBroadcastAsUser(
      intent: Intent,
      user: UserHandle, receiverPermission: String?
                                   )

   abstract fun sendOrderedBroadcastAsUser(
      intent: Intent,
      user: UserHandle, receiverPermission: String?, resultReceiver: BroadcastReceiver?,
      scheduler: Handler?, initialCode: Int, initialData: String?,
      initialExtras: Bundle?
                                          )

   abstract fun sendStickyBroadcast( intent: Intent)

   abstract fun sendStickyOrderedBroadcast(
      intent: Intent,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?, initialCode: Int, initialData: String?,
      initialExtras: Bundle?
                                          )

   abstract fun removeStickyBroadcast( intent: Intent)

   abstract fun sendStickyBroadcastAsUser(intent: Intent, user: UserHandle)

   abstract fun sendStickyOrderedBroadcastAsUser(
      intent: Intent, user: UserHandle, resultReceiver: BroadcastReceiver?,
      scheduler: Handler?, initialCode: Int, initialData: String?,
      initialExtras: Bundle?
                                                )

   abstract fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter
                                ): Intent?

   abstract fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter,
      flags: Int
                                ): Intent?

   abstract fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter, broadcastPermission: String?,
      scheduler: Handler?
                                ): Intent?

   abstract fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter, broadcastPermission: String?,
      scheduler: Handler?, flags: Int
                                ): Intent?

   abstract fun unregisterReceiver(receiver: BroadcastReceiver)

   abstract fun startService(service: Intent): ComponentName?

   abstract fun startForegroundService(service: Intent): ComponentName?

   abstract fun stopService(service: Intent): Boolean

   abstract fun bindService(
intent:Intent,
      conn: ServiceConnection, flags: Int
                           ): Boolean

   open fun bindService(
      intent:Intent,

      flags: Int, executor: Executor,
      conn: ServiceConnection
                       ): Boolean
   {
      throw RuntimeException("Not implemented. Must override in a subclass.")
   }

   open fun bindIsolatedService(
      intent:Intent,
      flags: Int, instanceName: String,
      executor: Executor, conn: ServiceConnection
                               ): Boolean
   {
      throw RuntimeException("Not implemented. Must override in a subclass.")
   }

   open fun updateServiceGroup(
      conn: ServiceConnection, group: Int,
      importance: Int
                              )
   {
      throw RuntimeException("Not implemented. Must override in a subclass.")
   }

   abstract fun unbindService(conn: ServiceConnection)

   abstract fun startInstrumentation(
      className: ComponentName,
      profileFile: String?, arguments: Bundle?
                                    ): Boolean

   abstract fun getSystemService(name: String): Any?

   open fun <T> getSystemService(serviceClass: Class<T>): T?
   {
      // Because subclasses may override getSystemService(String) we cannot
      // perform a lookup by class alone.  We must first map the class to its
      // service name then invoke the string-based method.
      val serviceName = getSystemServiceName(serviceClass)
      return if (serviceName != null) getSystemService(serviceName) as T? else null
   }

   abstract fun getSystemServiceName(serviceClass: Class<*>): String?

   @CheckResult(suggest = "#enforcePermission(String,int,int,String)")
   abstract fun checkPermission(permission: String, pid: Int, uid: Int): Int

   @CheckResult(suggest = "#enforceCallingPermission(String,String)")

   abstract fun checkCallingPermission(permission: String): Int

   @CheckResult(suggest = "#enforceCallingOrSelfPermission(String,String)")

   abstract fun checkCallingOrSelfPermission(permission: String): Int

   abstract fun checkSelfPermission(permission: String): Int

   abstract fun enforcePermission(
      permission: String, pid: Int, uid: Int, message: String?
                                 )

   abstract fun enforceCallingPermission(
      permission: String, message: String?
                                        )

   abstract fun enforceCallingOrSelfPermission(
      permission: String, message: String?
                                              )

   abstract fun grantUriPermission(
      toPackage: String, uri: Uri,
      modeFlags: Int
                                  )

   abstract fun revokeUriPermission(uri: Uri, modeFlags: Int)

   abstract fun revokeUriPermission(
      toPackage: String, uri: Uri,
      modeFlags: Int
                                   )

   @CheckResult(suggest = "#enforceUriPermission(Uri,int,int,String)")
   abstract fun checkUriPermission(
      uri: Uri, pid: Int, uid: Int,
      modeFlags: Int
                                  ): Int

   abstract fun checkCallingUriPermission(uri: Uri, modeFlags: Int): Int

   @CheckResult(suggest = "#enforceCallingOrSelfUriPermission(Uri,int,String)")
   abstract fun checkCallingOrSelfUriPermission(
      uri: Uri,
      modeFlags: Int
                                               ): Int

   @CheckResult(suggest = "#enforceUriPermission(Uri,String,String,int,int,int,String)")
   abstract fun checkUriPermission(
      uri: Uri?, readPermission: String?,
      writePermission: String?, pid: Int, uid: Int,
      modeFlags: Int
                                  ): Int

   abstract fun enforceUriPermission(
      uri: Uri, pid: Int, uid: Int, modeFlags: Int, message: String?
                                    )

   abstract fun enforceCallingUriPermission(
      uri: Uri, modeFlags: Int, message: String?
                                           )

   abstract fun enforceCallingOrSelfUriPermission(
      uri: Uri, modeFlags: Int, message: String?
                                                 )

   abstract fun enforceUriPermission(
      uri: Uri?, readPermission: String?,
      writePermission: String?, pid: Int, uid: Int, modeFlags: Int,
      message: String?
                                    )

   abstract fun createPackageContext(
      packageName: String,
      flags: Int
                                    ): Context

   abstract fun createContextForSplit(splitName: String): Context

   abstract fun createConfigurationContext(
      overrideConfiguration: Configuration
                                          ): Context

   abstract fun createDisplayContext(display: Display): Context

   abstract fun createDeviceProtectedStorageContext(): Context

   open fun isRestricted(): Boolean
   {
      return false
   }

   abstract fun isDeviceProtectedStorage(): Boolean
}