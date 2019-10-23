package jhelp.tests

import kotlin.math.min

fun CharArray.hasOnlyBlank(offset: Int, length: Int): Boolean
{
   var start = offset
   var size = length

   if (start < 0)
   {
      size += start
      start = 0
   }

   if (start >= this.size)
   {
      return false
   }

   size = min(this.size - start, size)

   if (size <= 0)
   {
      return false
   }

   for (index in start until start + size)
   {
      if (this[index] > ' ')
      {
         return false
      }
   }

   return true
}

fun String.parseColor() = this.substring(1, this.length).toInt(16)