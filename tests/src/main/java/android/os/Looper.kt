package android.os

import java.util.*

class Looper()
{
   companion object
   {
      val loopers = TreeMap<Long, Looper>()
      val mainLoooper = Looper(true)

      @JvmStatic
      fun getMainLooper() = this.mainLoooper

      @JvmStatic
      fun prepare()
      {
         val threadID =
              Thread.currentThread()
                 .id

         if (loopers[threadID] != null)
         {
            throw RuntimeException("Only one Looper may be created per thread")
         }

         loopers[threadID] = Looper(true)
      }

      @JvmStatic
      fun myLooper(): Looper?
      {
         val threadID =
              Thread.currentThread()
                 .id
         return loopers[threadID]
      }

      @JvmStatic
      fun loop() = null
   }

   init
   {
      println(this.javaClass.name)
   }

   private constructor(quitAllowed: Boolean) : this()

   private val currentThread = Thread.currentThread()

   fun quit() = Unit

   fun quitSafely() = Unit

   fun getThread() = this.currentThread

   fun isCurrentThread() = Thread.currentThread() == this.currentThread

   override fun toString(): String
   {
      return "Looper (${this.currentThread.name}, tid ${this.currentThread.id}) {${Integer.toHexString(
           System.identityHashCode(this))}"
   }
}