package jhelp.tests.lifecycle

import android.app.Activity
import android.app.callOnCreate
import android.app.fireOnActivityPreCreated
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import jhelp.tests.theApplication

internal object LifeCycleManager
{
   private val activities = HashMap<String, Pair<Activity, LifeCycleStatus>>()

   internal   fun launchApplication()
   {
      theApplication.onCreate()
   }

   fun launch(activity: Activity, bundle: Bundle?)
   {
      synchronized(this.activities)
      {
      val name = activity.javaClass.name
      val activityStatus = this.activities[name]

      if (activityStatus == null)
      {
         fireOnActivityPreCreated(activity, bundle)
         activity.callOnCreate(bundle)

         this.activities[name] = Pair(activity, LifeCycleStatus.RESUMED)
      }
   }
   }
}