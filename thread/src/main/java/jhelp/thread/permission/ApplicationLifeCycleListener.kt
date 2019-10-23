package jhelp.thread.permission

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * Observe activities life cycle of current [Application]
 *
 * It is used to know current active [Activity] and for refresh permissions status if need each time an [Activity] resumed
 */
internal object ApplicationLifeCycleListener : Application.ActivityLifecycleCallbacks
{
   private val lock = Object()
   private var currentActivity = WeakReference<Activity>(null)

   override fun onActivityPaused(activity: Activity) = Unit

   override fun onActivityStarted(activity: Activity)
   {
      synchronized(this.lock) { this.currentActivity = WeakReference(activity) }
   }

   override fun onActivityDestroyed(activity: Activity)
   {
      synchronized(this.lock)
      {
         if (this.currentActivity.get() == activity)
         {
            this.currentActivity.clear()
         }
      }
   }

   override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

   override fun onActivityStopped(activity: Activity)
   {
      synchronized(this.lock)
      {
         if (this.currentActivity.get() == activity)
         {
            this.currentActivity.clear()
         }
      }
   }

   override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)
   {
      synchronized(this.lock) { this.currentActivity = WeakReference(activity) }
   }

   override fun onActivityResumed(activity: Activity)
   {
      synchronized(this.lock) { this.currentActivity = WeakReference(activity) }

      PermissionManager.updatePermissions()
   }

   internal fun currentActivity() = this.currentActivity.get()
}