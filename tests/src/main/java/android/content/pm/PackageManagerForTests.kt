package android.content.pm

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.content.res.theResources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.DummyDrawable
import android.os.UserHandle

class PackageManagerForTests : PackageManager()
{
   override fun getLaunchIntentForPackage(packageName: String): Intent? = null

   override fun getResourcesForApplication(app: ApplicationInfo): Resources = theResources

   override fun getResourcesForApplication(packageName: String): Resources = theResources

   override fun getReceiverInfo(component: ComponentName, flags: Int)= ActivityInfo()

   override fun queryIntentActivityOptions(
      caller: ComponentName?,
      specifics: Array<out Intent>?,
      intent: Intent,
      flags: Int
                                          ): MutableList<ResolveInfo>
   {
      return ArrayList()
   }

   override fun getApplicationIcon(info: ApplicationInfo): Drawable=DummyDrawable

   override fun getApplicationIcon(packageName: String): Drawable=DummyDrawable

   override fun extendVerificationTimeout(id: Int, verificationCodeAtTimeout: Int, millisecondsToDelay: Long)
   {
   }

   override fun getApplicationEnabledSetting(packageName: String): Int= COMPONENT_ENABLED_STATE_ENABLED

   override fun queryIntentServices(intent: Intent, flags: Int): MutableList<ResolveInfo>
   {
      return ArrayList()
   }

   override fun isPermissionRevokedByPolicy(permissionName: String, packageName: String)=false

   override fun checkPermission(permissionName: String, packageName: String)= PERMISSION_GRANTED

   override fun checkSignatures(packageName1: String, packageName2: String)= SIGNATURE_MATCH

   override fun checkSignatures(uid1: Int, uid2: Int): Int= SIGNATURE_MATCH

   override fun removePackageFromPreferred(packageName: String)
   {
   }

   override fun addPermission(info: PermissionInfo)=true

   override fun getDrawable(packageName: String, resid: Int, appInfo: ApplicationInfo?): Drawable?=null

   override fun getChangedPackages(sequenceNumber: Int): ChangedPackages?=null

   override fun getPackageInfo(packageName: String, flags: Int)= PackageInfo()

   override fun getPackageInfo(versionedPackage: VersionedPackage, flags: Int)= PackageInfo()

   override fun getPackagesHoldingPermissions(permissions: Array<String>, flags: Int): MutableList<PackageInfo>
   {
      return ArrayList()
   }

   override fun addPermissionAsync(info: PermissionInfo)=true

   override fun getSystemAvailableFeatures(): Array<FeatureInfo>
   {
      return emptyArray()
   }

   override fun getSystemSharedLibraryNames(): Array<String>?=null

   override fun queryIntentContentProviders(intent: Intent, flags: Int): MutableList<ResolveInfo>
   {
      return ArrayList()
   }

   override fun getApplicationBanner(info: ApplicationInfo): Drawable?=null

   override fun getApplicationBanner(packageName: String): Drawable?=null

   override fun getPackageGids(packageName: String): IntArray
   {
      return IntArray(0)
   }

   override fun getPackageGids(packageName: String, flags: Int): IntArray
   {
     return IntArray(0)
   }

   override fun getResourcesForActivity(activityName: ComponentName): Resources= theResources

   override fun getPackagesForUid(uid: Int): Array<String>?=null
   override fun getPermissionGroupInfo(permissionName: String, flags: Int)= PermissionGroupInfo()

   override fun addPackageToPreferred(packageName: String)
   {
   }

   override fun getComponentEnabledSetting(componentName: ComponentName):Int= COMPONENT_ENABLED_STATE_ENABLED

   override fun getLeanbackLaunchIntentForPackage(packageName: String): Intent?=null

   override fun getInstalledPackages(flags: Int): MutableList<PackageInfo>
   {
      return ArrayList()
   }

   override fun getAllPermissionGroups(flags: Int): MutableList<PermissionGroupInfo>
   {
      return ArrayList()
   }

   override fun getNameForUid(uid: Int): String?=null

   override fun updateInstantAppCookie(cookie: ByteArray?)
   {
   }

   override fun getApplicationLogo(info: ApplicationInfo): Drawable?=null

   override fun getApplicationLogo(packageName: String): Drawable?=null

   override fun getApplicationLabel(info: ApplicationInfo): CharSequence=""

   override fun getPreferredActivities(
      outFilters: MutableList<IntentFilter>,
      outActivities: MutableList<ComponentName>,
      packageName: String?
                                      )=0

   override fun setApplicationCategoryHint(packageName: String, categoryHint: Int)
   {
   }

   override fun setInstallerPackageName(targetPackage: String, installerPackageName: String?)=Unit

   override fun getUserBadgedLabel(label: CharSequence, user: UserHandle): CharSequence=label

   override fun canRequestPackageInstalls()=false

   override fun isInstantApp()=false

   override fun isInstantApp(packageName: String)=false

   override fun getActivityIcon(activityName: ComponentName)=DummyDrawable

   override fun getActivityIcon(intent: Intent)=DummyDrawable

   override fun canonicalToCurrentPackageNames(packageNames: Array<String>): Array<String>
   {
      return emptyArray()
   }

   override fun getProviderInfo(component: ComponentName, flags: Int)= ProviderInfo()

   override fun clearPackagePreferredActivities(packageName: String)=Unit

   override fun getPackageInstaller(): PackageInstaller
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun resolveService(intent: Intent, flags: Int): ResolveInfo?=null

   override fun verifyPendingInstall(id: Int, verificationCode: Int)=Unit

   override fun getInstantAppCookie(): ByteArray
   {
      return  ByteArray(0)
   }

   override fun getText(packageName: String, resid: Int, appInfo: ApplicationInfo?): CharSequence?=null

   override fun resolveContentProvider(authority: String, flags: Int): ProviderInfo?=null

   override fun hasSystemFeature(featureName: String)=false

   override fun hasSystemFeature(featureName: String, version: Int)=false

   override fun getInstrumentationInfo(className: ComponentName, flags: Int) = InstrumentationInfo()

   override fun getInstalledApplications(flags: Int): MutableList<ApplicationInfo>
   {
      return ArrayList()
   }

   override fun getUserBadgedDrawableForDensity(
      drawable: Drawable,
      user: UserHandle,
      badgeLocation: Rect?,
      badgeDensity: Int
                                               )=DummyDrawable

   override fun getInstantAppCookieMaxBytes()=73

   override fun getDefaultActivityIcon()=DummyDrawable

   override fun getPreferredPackages(flags: Int): MutableList<PackageInfo>
   {
      return ArrayList()
   }

   override fun addPreferredActivity(filter: IntentFilter, match: Int, set: Array<ComponentName>?, activity: ComponentName)
        =Unit

   override fun getSharedLibraries(flags: Int): MutableList<SharedLibraryInfo>
   {
      return ArrayList()
   }

   override fun queryIntentActivities(intent: Intent, flags: Int): MutableList<ResolveInfo>
   {
      return ArrayList()
   }

   override fun getActivityBanner(activityName: ComponentName): Drawable?=null

   override fun getActivityBanner(intent: Intent): Drawable?=null

   override fun setComponentEnabledSetting(componentName: ComponentName, newState: Int, flags: Int)=Unit

   override fun getApplicationInfo(packageName: String, flags: Int) = ApplicationInfo()

   override fun resolveActivity(intent: Intent, flags: Int): ResolveInfo?=null

   override fun queryBroadcastReceivers(intent: Intent, flags: Int): MutableList<ResolveInfo>
   {
      return ArrayList()
   }

   override fun getXml(packageName: String, resid: Int, appInfo: ApplicationInfo?): XmlResourceParser?=null

   override fun getActivityLogo(activityName: ComponentName): Drawable?=null

   override fun getActivityLogo(intent: Intent): Drawable?=null

   override fun queryPermissionsByGroup(permissionGroup: String, flags: Int): MutableList<PermissionInfo>
   {
      return ArrayList()
   }

   override fun queryContentProviders(processName: String?, uid: Int, flags: Int): MutableList<ProviderInfo>
   {
      return ArrayList()
   }

   override fun getPermissionInfo(permissionName: String, flags: Int) = PermissionInfo()

   override fun removePermission(permissionName: String)=Unit

   override fun queryInstrumentation(targetPackage: String, flags: Int): MutableList<InstrumentationInfo>
   {
     return ArrayList()
   }

   override fun clearInstantAppCookie()=Unit

   override fun currentToCanonicalPackageNames(packageNames: Array<String>)=packageNames

   override fun getPackageUid(packageName: String, flags: Int)=42

   override fun getUserBadgedIcon(drawable: Drawable, user: UserHandle)=DummyDrawable

   override fun getActivityInfo(component: ComponentName, flags: Int) = ActivityInfo()

   override fun isSafeMode()=true

   override fun getInstallerPackageName(packageName: String): String?=null

   override fun setApplicationEnabledSetting(packageName: String, newState: Int, flags: Int)=Unit

   override fun getServiceInfo(component: ComponentName, flags: Int) = ServiceInfo()
}