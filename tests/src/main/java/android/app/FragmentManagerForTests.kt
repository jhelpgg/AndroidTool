package android.app

import android.app.Fragment.SavedState
import android.os.Bundle
import java.io.FileDescriptor
import java.io.PrintWriter

object FragmentManagerForTests : FragmentManager()
{
   init
   {
      println(this.javaClass.name)
   }

   override fun saveFragmentInstanceState(f: Fragment?): SavedState
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun findFragmentById(id: Int): Fragment
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getFragments(): MutableList<Fragment>
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun beginTransaction(): FragmentTransaction
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun putFragment(bundle: Bundle?, key: String?, fragment: Fragment?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun removeOnBackStackChangedListener(listener: OnBackStackChangedListener?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getFragment(bundle: Bundle?, key: String?): Fragment
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun unregisterFragmentLifecycleCallbacks(cb: FragmentLifecycleCallbacks?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getPrimaryNavigationFragment(): Fragment
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getBackStackEntryCount(): Int
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun isDestroyed(): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getBackStackEntryAt(index: Int): BackStackEntry
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun executePendingTransactions(): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun popBackStackImmediate(): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun popBackStackImmediate(name: String?, flags: Int): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun popBackStackImmediate(id: Int, flags: Int): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun findFragmentByTag(tag: String?): Fragment
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun addOnBackStackChangedListener(listener: OnBackStackChangedListener?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun dump(prefix: String?, fd: FileDescriptor?, writer: PrintWriter?, args: Array<out String>?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun isStateSaved(): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun popBackStack()
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun popBackStack(name: String?, flags: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun popBackStack(id: Int, flags: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun registerFragmentLifecycleCallbacks(cb: FragmentLifecycleCallbacks?, recursive: Boolean)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }
}