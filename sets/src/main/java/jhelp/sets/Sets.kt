package jhelp.sets

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

fun <T> MutableCollection<T>.removeOn(condition: (T) -> Boolean)
{
   if (VERSION.SDK_INT >= VERSION_CODES.N)
   {
      this.removeIf(condition)
   }
   else
   {
      val iterator = this.iterator()

      while (iterator.hasNext())
      {
         if (condition(iterator.next()))
         {
            iterator.remove()
         }
      }
   }
}