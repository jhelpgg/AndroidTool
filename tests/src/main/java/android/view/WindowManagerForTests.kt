package android.view

import android.view.ViewGroup.LayoutParams

object WindowManagerForTests : WindowManager
{
   val display = Display()

   init
   {
      println(this.javaClass.name)
   }

   override fun getDefaultDisplay()=this.display

   override fun addView(view: View, params: LayoutParams)=Unit

   override fun updateViewLayout(view: View, params: LayoutParams)=Unit

   override fun removeView(view: View)=Unit

   override fun removeViewImmediate(view: View)=Unit
}