package jhelp.tests

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.obtainClass
import android.os.Build
import android.os.Bundle
import android.os.SystemProperties
import jhelp.tests.lifecycle.LifeCycleManager
import jhelp.tests.manifest.AndroidManifest

val atLeastKikat_19 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT }
val atLeastLolipop_21 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP }
val atLeastLolipopMR1_22 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 }
val atLeastMarshmallow_23 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.M }
val atLeastNougat_24 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.N }
val atLeastNougatMR1_25 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 }
val atLeastOreo_26 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.O }
val atLeastPie_28 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.P }
val atLeastQ_29 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q }

fun initializeTests(sdkVersion: Int)
{
   // The initialization order is important
   SystemProperties.set("ro.build.version.sdk", sdkVersion)
   callerClass = Class.forName((Throwable().stackTrace[1]).className)
   AndroidManifest.parseAndroidManifest()
   LifeCycleManager.launchApplication()
   val mainActivity = AndroidManifest.mainLauncherActivity()

   if (mainActivity != null)
   {
      launchActivity<Activity>(Intent(theApplication, Class.forName(mainActivity.name)))
   }
}

val theApplication by lazy {
   val applicationName = AndroidManifest.application.name

   if (applicationName.isEmpty())
   {
      Application()
   }
   else
   {
      Class.forName(applicationName).newInstance() as Application
   }
}

fun <A : Activity> launchActivity(intent: Intent, bundle: Bundle? = null): A?
{
   val componentName = intent.component
      ?: return null
   val classActivity = componentName.obtainClass() as? Class<A>
      ?: return null
   val activity = obtainActivity(classActivity)
   activity.setIntent(intent)
   LifeCycleManager.launch(activity, bundle)
   return activity
}

private val activities = HashMap<String, Activity>()

fun <A : Activity> obtainActivity(activityClass: Class<A>): A
{
   val name = activityClass.name
   var activity = activities[name]

   if (activity != null)
   {
      return activity as A
   }

   require(name in AndroidManifest.application.activities().map { it.name }) { "The activity $name not declared in AndroidManifest" }

   activity = activityClass.newInstance()
   activities[name] = activity
   return activity
}