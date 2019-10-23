package jhelp.tests

import android.os.Parcel
import kotlin.math.max
import kotlin.math.min

class ArrayBytes
{
   var read = 0
      private set
   var size = 0
      private set
   private var data = ByteArray(4096)

   val bytes get() = this.data.copyOf(this.size)

   private fun append(more: Int)
   {
      if (this.size + more >= this.data.size)
      {
         var newSize = this.size + more
         newSize += newSize shl 3
         this.data = this.data.copyOf(newSize)
      }
   }

   val available get() = this.size - this.read

   fun write(byteArray: ByteArray)
   {
      val size = byteArray.size
      this.append(size)
      System.arraycopy(byteArray, 0, this.data, this.size, size)
      this.size += size
   }

   fun read(size: Int): ByteArray
   {
      val length = max(0, min(size, this.available))
      val array = ByteArray(length)

      if (length > 0)
      {
         System.arraycopy(this.data, this.read, array, 0, length)
         this.read += length
      }

      return array
   }

   fun resetRead()
   {
      this.read = 0
   }

   fun clear()
   {
      this.size = 0
      this.read = 0
   }

   fun readPosition(position: Int)
   {
      this.read = max(0, min(this.size, position))
   }
}