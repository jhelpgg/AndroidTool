package android.app

import android.os.Bundle
import jhelp.tests.theApplication

fun Activity.callOnCreate(bundle:Bundle?)
{
   this.onCreate(bundle)
}

fun Activity.callOnPostCreate(bundle:Bundle?)
{
   this.onPostCreate(bundle)
}

fun Activity.callOnStart()
{
   this.onStart()
}

fun Activity.callOnResume()
{
   this.onResume()
}

fun Activity.callOnPostResume()
{
   this.onPostResume()
}

fun fireOnActivityPreCreated(activity: Activity, savedInstanceState: Bundle?)
{
   theApplication.fireOnActivityPreCreated(activity,savedInstanceState)
}