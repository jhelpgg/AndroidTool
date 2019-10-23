package android.view

import android.content.Context

class LayoutInflaterForTests(context:Context): LayoutInflater(context)
{
   init
   {
      println(this.javaClass.name)
   }

   override fun cloneInContext(newContext: Context)=this
}