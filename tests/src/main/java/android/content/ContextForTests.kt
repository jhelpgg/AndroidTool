package android.content

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.DownloadManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.SearchManager
import android.app.UiModeManager
import android.app.WallpaperManager
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.role.RoleManager
import android.app.usage.NetworkStatsManager
import android.app.usage.StorageStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.companion.CompanionDeviceManager
import android.content.pm.ApplicationInfo
import android.content.pm.CrossProfileApps
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.pm.PackageManagerForTests
import android.content.pm.ShortcutManager
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.theResources
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.graphics.Bitmap
import android.graphics.drawable.DummyDrawable
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.biometrics.BiometricManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.fingerprint.FingerprintManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.IpSecManager
import android.net.Uri
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.rtt.WifiRttManager
import android.nfc.NfcManager
import android.os.BatteryManager
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.DropBoxManager
import android.os.Handler
import android.os.HardwarePropertiesManager
import android.os.Looper
import android.os.PowerManager
import android.os.UserHandle
import android.os.UserManager
import android.os.Vibrator
import android.os.health.SystemHealthManager
import android.os.storage.StorageManager
import android.print.PrintManager
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.Display
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textclassifier.TextClassificationManager
import android.view.textservice.TextServicesManager
import jhelp.tests.DoubleReferenceMap
import jhelp.tests.androidResourcesDirectory
import jhelp.tests.atLeastLolipopMR1_22
import jhelp.tests.atLeastLolipop_21
import jhelp.tests.atLeastMarshmallow_23
import jhelp.tests.atLeastNougatMR1_25
import jhelp.tests.atLeastNougat_24
import jhelp.tests.atLeastOreo_26
import jhelp.tests.atLeastPie_28
import jhelp.tests.atLeastQ_29
import jhelp.tests.createDirectory
import jhelp.tests.erase
import jhelp.tests.manifest.AndroidManifest
import jhelp.tests.outsideDirectory
import jhelp.tests.theApplication
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

private fun createChildDirectory(parent: File, name: String): File
{
   val directory = File(parent, name)

   if (!directory.createDirectory())
   {
      println("Issue when create '$name' directory : ${directory.absolutePath}")
   }

   return directory
}

private fun createDirectory(name: String) = createChildDirectory(outsideDirectory, name)

val obbDirectory by lazy { createDirectory("obb") }

val externalDirectory by lazy { createDirectory("external") }

val externalCacheDirectory by lazy { createChildDirectory(externalDirectory, "cache") }

val externalMediaDirectory by lazy { createChildDirectory(externalDirectory, "media") }

val resourcesDirectory by lazy { androidResourcesDirectory }

val dataDirectory by lazy { createDirectory("data") }

val databaseDirectory by lazy { createDirectory("database") }

val fileDirectory by lazy { createDirectory("file") }

val codeDirectory by lazy { createDirectory("code") }

val codeCacheDirectory by lazy { createChildDirectory(codeDirectory, "cache") }

val noBackupDirectory by lazy { createDirectory("noBackup") }

val cacheDirectory by lazy { createDirectory("cache") }


private val servicesMap by lazy {
   val services = DoubleReferenceMap<String, Class<out Any>>()
   services.associate(Context.CLIPBOARD_SERVICE, android.text.ClipboardManager::class.java)
   services.associate(Context.ACCESSIBILITY_SERVICE, AccessibilityManager::class.java)
   services.associate(Context.CAPTIONING_SERVICE, CaptioningManager::class.java)
   services.associate(Context.ACCOUNT_SERVICE, AccountManager::class.java)
   services.associate(Context.ACTIVITY_SERVICE, ActivityManager::class.java)
   services.associate(Context.ALARM_SERVICE, AlarmManager::class.java)
   services.associate(Context.AUDIO_SERVICE, AudioManager::class.java)
   services.associate(Context.MEDIA_ROUTER_SERVICE, MediaRouter::class.java)
   services.associate(Context.BLUETOOTH_SERVICE, BluetoothManager::class.java)
   services.associate(Context.CLIPBOARD_SERVICE, ClipboardManager::class.java)
   services.associate(Context.CONNECTIVITY_SERVICE, ConnectivityManager::class.java)
   services.associate(Context.DEVICE_POLICY_SERVICE, DevicePolicyManager::class.java)
   services.associate(Context.DOWNLOAD_SERVICE, DownloadManager::class.java)
   services.associate(Context.NFC_SERVICE, NfcManager::class.java)
   services.associate(Context.DROPBOX_SERVICE, DropBoxManager::class.java)
   services.associate(Context.INPUT_SERVICE, InputManager::class.java)
   services.associate(Context.DISPLAY_SERVICE, DisplayManager::class.java)
   services.associate(Context.INPUT_METHOD_SERVICE, InputMethodManager::class.java)
   services.associate(Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager::class.java)
   services.associate(Context.KEYGUARD_SERVICE, KeyguardManager::class.java)
   services.associate(Context.LAYOUT_INFLATER_SERVICE, LayoutInflater::class.java)
   services.associate(Context.LOCATION_SERVICE, LocationManager::class.java)
   services.associate(Context.NOTIFICATION_SERVICE, NotificationManager::class.java)
   services.associate(Context.NSD_SERVICE, NsdManager::class.java)
   services.associate(Context.POWER_SERVICE, PowerManager::class.java)
   services.associate(Context.SEARCH_SERVICE, SearchManager::class.java)
   services.associate(Context.SENSOR_SERVICE, SensorManager::class.java)
   services.associate(Context.STORAGE_SERVICE, StorageManager::class.java)
   services.associate(Context.TELEPHONY_SERVICE, TelephonyManager::class.java)
   services.associate(Context.UI_MODE_SERVICE, UiModeManager::class.java)
   services.associate(Context.USB_SERVICE, UsbManager::class.java)
   services.associate(Context.VIBRATOR_SERVICE, Vibrator::class.java)
   services.associate(Context.WALLPAPER_SERVICE, WallpaperManager::class.java)
   services.associate(Context.WIFI_SERVICE, WifiManager::class.java)
   services.associate(Context.WIFI_P2P_SERVICE, WifiP2pManager::class.java)
   services.associate(Context.WINDOW_SERVICE, WindowManager::class.java)
   services.associate(Context.USER_SERVICE, UserManager::class.java)
   services.associate(Context.APP_OPS_SERVICE, AppOpsManager::class.java)
   services.associate(Context.PRINT_SERVICE, PrintManager::class.java)
   services.associate(Context.CONSUMER_IR_SERVICE, ConsumerIrManager::class.java)

   if (atLeastLolipop_21)
   {
      servicesAtLeast21(services)
   }

   //
   services
}

@TargetApi(VERSION_CODES.LOLLIPOP)
private fun servicesAtLeast21(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.BATTERY_SERVICE, BatteryManager::class.java)
   services.associate(Context.TELECOM_SERVICE, TelecomManager::class.java)
   services.associate(Context.CAMERA_SERVICE, CameraManager::class.java)
   services.associate(Context.LAUNCHER_APPS_SERVICE, LauncherApps::class.java)
   services.associate(Context.RESTRICTIONS_SERVICE, RestrictionsManager::class.java)
   services.associate(Context.TV_INPUT_SERVICE, TvInputManager::class.java)
   services.associate(Context.JOB_SCHEDULER_SERVICE, JobScheduler::class.java)
   services.associate(Context.MEDIA_PROJECTION_SERVICE, MediaProjectionManager::class.java)
   services.associate(Context.APPWIDGET_SERVICE, AppWidgetManager::class.java)

   if (atLeastLolipopMR1_22)
   {
      servicesAtLeast22(services)
   }
}

@TargetApi(VERSION_CODES.LOLLIPOP_MR1)
private fun servicesAtLeast22(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.TELEPHONY_SUBSCRIPTION_SERVICE, SubscriptionManager::class.java)
   services.associate(Context.USAGE_STATS_SERVICE, UsageStatsManager::class.java)

   if (atLeastMarshmallow_23)
   {
      servicesAtLeast23(services)
   }
}

@TargetApi(VERSION_CODES.M)
private fun servicesAtLeast23(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.CARRIER_CONFIG_SERVICE, CarrierConfigManager::class.java)
   services.associate(Context.FINGERPRINT_SERVICE, FingerprintManager::class.java)
   services.associate(Context.NETWORK_STATS_SERVICE, NetworkStatsManager::class.java)
   services.associate(Context.MIDI_SERVICE, MidiManager::class.java)

   if (atLeastNougat_24)
   {
      servicesAtLeast24(services)
   }
}

@TargetApi(VERSION_CODES.N)
private fun servicesAtLeast24(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.HARDWARE_PROPERTIES_SERVICE, HardwarePropertiesManager::class.java)
   services.associate(Context.SYSTEM_HEALTH_SERVICE, SystemHealthManager::class.java)

   if (atLeastNougatMR1_25)
   {
      servicesAtLeast25(services)
   }
}

@TargetApi(VERSION_CODES.N_MR1)
private fun servicesAtLeast25(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.SHORTCUT_SERVICE, ShortcutManager::class.java)

   if (atLeastOreo_26)
   {
      servicesAtLeast26(services)
   }
}

@TargetApi(VERSION_CODES.O)
private fun servicesAtLeast26(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.TEXT_CLASSIFICATION_SERVICE, TextClassificationManager::class.java)
   services.associate(Context.STORAGE_STATS_SERVICE, StorageStatsManager::class.java)
   services.associate(Context.WIFI_AWARE_SERVICE, WifiAwareManager::class.java)
   services.associate(Context.COMPANION_DEVICE_SERVICE, CompanionDeviceManager::class.java)

   if (atLeastPie_28)
   {
      servicesAtLeast28(services)
   }
}


@TargetApi(VERSION_CODES.P)
private fun servicesAtLeast28(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.IPSEC_SERVICE, IpSecManager::class.java)
   services.associate(Context.EUICC_SERVICE, EuiccManager::class.java)
   services.associate(Context.WIFI_RTT_RANGING_SERVICE, WifiRttManager::class.java)
   services.associate(Context.CROSS_PROFILE_APPS_SERVICE, CrossProfileApps::class.java)

   if (atLeastQ_29)
   {
      servicesAtLeast29(services)
   }
}

@TargetApi(VERSION_CODES.Q)
private fun servicesAtLeast29(services: DoubleReferenceMap<String, Class<out Any>>)
{
   services.associate(Context.BIOMETRIC_SERVICE, BiometricManager::class.java)
   services.associate(Context.ROLE_SERVICE, RoleManager::class.java)
}

@SuppressLint("MissingPermission")
class ContextForTests : Context()
{
   private val sharedPreferences = HashMap<String, SharedPreferences>()
   private val databases = HashMap<String, SQLiteDatabase>()

   init
   {
      println(this.javaClass.name)
   }

   override fun getApplicationContext() = theApplication

   override fun setWallpaper(bitmap: Bitmap) = Unit

   override fun setWallpaper(data: InputStream) = Unit

   override fun removeStickyBroadcastAsUser(intent: Intent, user: UserHandle)=Unit

   // TODO Manage permissions
   override fun checkCallingOrSelfPermission(permission: String) = PackageManager.PERMISSION_GRANTED

   override fun getClassLoader() : ClassLoader = this.javaClass.classLoader!!

   // TODO Manage permissions
   override fun checkCallingOrSelfUriPermission(uri: Uri, modeFlags: Int) = PackageManager.PERMISSION_GRANTED

   override fun getObbDir() = obbDirectory

   // TODO Manage permissions
   override fun checkUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int) = PackageManager.PERMISSION_GRANTED

   // TODO Manage permissions
   override fun checkUriPermission(
      uri: Uri?,
      readPermission: String?,
      writePermission: String?,
      pid: Int,
      uid: Int,
      modeFlags: Int
                                  ) = PackageManager.PERMISSION_GRANTED

   override fun getExternalFilesDirs(type: String): Array<File>
   {
      val directory =File(externalDirectory, type)
      return directory.listFiles()!!
   }

   override fun getPackageResourcePath() = resourcesDirectory.absolutePath!!

   override fun deleteSharedPreferences(name: String): Boolean
   {
      this.sharedPreferences.remove(name)
      return true
   }

   // TODO Manage permissions
   override fun checkPermission(permission: String, pid: Int, uid: Int) = PackageManager.PERMISSION_GRANTED

   override fun startIntentSender(
      intent: IntentSender,
      fillInIntent: Intent?,
      flagsMask: Int,
      flagsValues: Int,
      extraFlags: Int
                                 ) =
      this.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, null)

   override fun startIntentSender(
      intent: IntentSender,
      fillInIntent: Intent?,
      flagsMask: Int,
      flagsValues: Int,
      extraFlags: Int,
      options: Bundle?
                                 )
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getSharedPreferences(name: String, mode: Int) =
      this.sharedPreferences.getOrPut(name) { SharedPreferencesForTests() }

   // TODO Broadcast management
   override fun sendStickyBroadcastAsUser(intent: Intent, user: UserHandle)=Unit

   override fun getDataDir() = dataDirectory

   override fun getWallpaper() = DummyDrawable

   override fun isDeviceProtectedStorage() = false

   override fun getExternalFilesDir(type: String?) =
      type?.let { File(externalDirectory, it) }
         ?: externalDirectory

   // TODO Broadcast management
   override fun sendBroadcastAsUser(intent: Intent, user: UserHandle)=Unit

   // TODO Broadcast management
   override fun sendBroadcastAsUser(intent: Intent, user: UserHandle, receiverPermission: String?)=Unit

   override fun getExternalCacheDir() = externalCacheDirectory

   override fun getDatabasePath(name: String) = File(databaseDirectory, name)

   override fun getFileStreamPath(name: String) = File(fileDirectory, name)

   //TODO Service management
   override fun stopService(service: Intent) = true

   // TODO Manage permissions
   override fun checkSelfPermission(permission: String) = PackageManager.PERMISSION_GRANTED

   // TODO Broadcast management
   override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter): Intent?
   {
      return null
   }

   // TODO Broadcast management
   override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter, flags: Int): Intent?
   {
      return null
   }

   // TODO Broadcast management
   override fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter,
      broadcastPermission: String?,
      scheduler: Handler?
                                ): Intent?
   {
      return null
   }

   // TODO Broadcast management
   override fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter,
      broadcastPermission: String?,
      scheduler: Handler?,
      flags: Int
                                ): Intent?
   {
      return null
   }

   override fun getSystemServiceName(serviceClass: Class<*>) = servicesMap.get2(serviceClass)

   override fun getMainLooper() = Looper.getMainLooper()!!

   // TODO Manage permissions
   override fun enforceCallingOrSelfPermission(permission: String, message: String?)
   {
   }

   override fun getPackageCodePath() = outsideDirectory.absolutePath!!

   // TODO Manage permissions
   override fun checkCallingUriPermission(uri: Uri, modeFlags: Int) = PackageManager.PERMISSION_GRANTED

   override fun getWallpaperDesiredMinimumWidth() = 128

   override fun createDeviceProtectedStorageContext() = this

   override fun openFileInput(name: String) = FileInputStream(File(fileDirectory, name))

   override fun getCodeCacheDir() = codeCacheDirectory

   // TOD Service management
   override fun bindService(service: Intent, conn: ServiceConnection, flags: Int) = false

   override fun deleteDatabase(name: String) = this.databases.remove(name) != null

   override fun getAssets(): AssetManager
   {
      TODO("not implemented : Manage AssetManager")
   }

   override fun getNoBackupFilesDir() = noBackupDirectory

   // TODO Manage Activities
   override fun startActivities(intents: Array< Intent>)=Unit

   // TODO Manage Activities
   override fun startActivities(intents: Array< Intent>, options: Bundle?)=Unit

   override fun getResources(): Resources = theResources

   override fun fileList(): Array<String> = fileDirectory.list()
      ?: emptyArray()

   override fun setTheme(resid: Int) = Unit

   // TODO broadcast management
   override fun unregisterReceiver(receiver: BroadcastReceiver)
   {
   }

   // TOTO manage permissions
   override fun enforcePermission(permission: String, pid: Int, uid: Int, message: String?)
   {
   }

   override fun openFileOutput(name: String, mode: Int) = FileOutputStream(File(fileDirectory, name))

   // TODO broadcast management
   override fun sendStickyOrderedBroadcast(
      intent: Intent,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                          )
   {
   }

   override fun createConfigurationContext(overrideConfiguration: Configuration) = this

   override fun getFilesDir() = fileDirectory

   // TODO broadcast management
   override fun sendBroadcast(intent: Intent)
   {
   }

   // TODO broadcast management
   override fun sendBroadcast(intent: Intent, receiverPermission: String?)
   {
   }

   // TODO broadcast management
   override fun sendOrderedBroadcastAsUser(
      intent: Intent,
      user: UserHandle,
      receiverPermission: String?,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                          )
   {
   }

   // TODO Permissions management
   override fun grantUriPermission(toPackage: String, uri: Uri, modeFlags: Int)
   {
   }

   // TODO Permissions management
   override fun enforceCallingUriPermission(uri: Uri, modeFlags: Int, message: String?)
   {
   }

   override fun getCacheDir() = cacheDirectory

   override fun clearWallpaper() = Unit

   // TODO Broadcast management
   override fun sendStickyOrderedBroadcastAsUser(
      intent: Intent,
      user: UserHandle,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                                )
   {
   }

   // TODO Activity management
   override fun startActivity(intent: Intent)
   {
   }

   // TODO Activity management
   override fun startActivity(intent: Intent, options: Bundle?)
   {
   }

   override fun getPackageManager(): PackageManager = PackageManagerForTests()

   override fun openOrCreateDatabase(name: String, mode: Int, factory: CursorFactory?) =
      this.databases.getOrPut(name) { SQLiteDatabase.create(factory) }

   override fun openOrCreateDatabase(
      name: String,
      mode: Int,
      factory: CursorFactory?,
      errorHandler: DatabaseErrorHandler?
                                    ) =
      this.databases.getOrPut(name) { SQLiteDatabase.create(factory) }

   override fun deleteFile(name: String) = File(fileDirectory, name).erase()

   // TODO Service management
   override fun startService(service: Intent): ComponentName?
   {
      return null
   }

   // TODO Permissions management
   override fun revokeUriPermission(uri: Uri, modeFlags: Int)
   {
   }

   // TODO Permissions management
   override fun revokeUriPermission(toPackage: String, uri: Uri, modeFlags: Int)
   {
   }

   override fun moveDatabaseFrom(sourceContext: Context, name: String): Boolean
   {
      if (this == sourceContext)
      {
         return this.databases.containsKey(name)
      }

      if (sourceContext !is ContextForTests || this.databases.containsKey(name))
      {
         return false
      }

      val database = sourceContext.databases.remove(name)
         ?: return true
      this.databases[name] = database
      return true
   }

   override fun startInstrumentation(className: ComponentName, profileFile: String?, arguments: Bundle?) = false

   //TODO Broadcast management
   override fun sendOrderedBroadcast(intent: Intent, receiverPermission: String?)
   {
   }

   //TODO Broadcast management
   override fun sendOrderedBroadcast(
      intent: Intent,
      receiverPermission: String?,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                    )
   {
   }

   // TODO Service management
   override fun unbindService(conn: ServiceConnection)
   {
   }

   override fun getApplicationInfo(): ApplicationInfo
   {
      TODO("not implemented")
   }

   override fun getWallpaperDesiredMinimumHeight() = 128

   override fun createDisplayContext(display: Display) = this

   override fun createContextForSplit(splitName: String) = this

   override fun getTheme(): Resources.Theme = Resources.Theme()

   override fun getPackageName() = AndroidManifest.packageName

   override fun getContentResolver(): ContentResolver
   {
      TODO("not implemented")
   }

   override fun getObbDirs(): Array<File>
   {
      return obbDirectory.listFiles()
         ?: emptyArray()
   }

   // TODO Permissions management
   override fun enforceCallingOrSelfUriPermission(uri: Uri, modeFlags: Int, message: String?)
   {
   }

   override fun moveSharedPreferencesFrom(sourceContext: Context, name: String): Boolean
   {
      if (this == sourceContext)
      {
         return this.sharedPreferences.containsKey(name)
      }

      if (sourceContext !is ContextForTests || this.sharedPreferences.containsKey(name))
      {
         return false
      }

      val preferences = sourceContext.sharedPreferences.remove(name)
         ?: return true
      this.sharedPreferences[name] = preferences
      return true
   }

   override fun getExternalMediaDirs(): Array<File>
   {
      return externalMediaDirectory.listFiles()
         ?: emptyArray()
   }

   // TODO Permissions
   override fun checkCallingPermission(permission: String) = PackageManager.PERMISSION_GRANTED

   override fun getExternalCacheDirs(): Array<File>
   {
      return externalCacheDirectory.listFiles()
         ?: emptyArray()
   }

   // TODO Broadcast
   override fun sendStickyBroadcast(intent: Intent)
   {
   }

   // TODO Permissions
   override fun enforceCallingPermission(permission: String, message: String?)
   {
   }

   override fun peekWallpaper() = DummyDrawable

   // TODO System services
   override fun getSystemService(name: String): Any?
   {
      return null
   }

   // TODO foreground services
   override fun startForegroundService(service: Intent): ComponentName?
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getDir(name: String, mode: Int) = File(fileDirectory, name)

   override fun databaseList() = this.databases.map { it.key }.toTypedArray()

   override fun createPackageContext(packageName: String, flags: Int) = this

   // TODO permissions
   override fun enforceUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int, message: String?)
   {
   }

   // TODO permissions
   override fun enforceUriPermission(
      uri: Uri?,
      readPermission: String?,
      writePermission: String?,
      pid: Int,
      uid: Int,
      modeFlags: Int,
      message: String?
                                    )
   {
   }

   // TODO Broadcast
   override fun removeStickyBroadcast(intent: Intent)
   {
   }
}