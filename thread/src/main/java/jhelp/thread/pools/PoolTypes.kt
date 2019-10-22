package jhelp.thread.pools

import jhelp.sets.Queue
import java.util.concurrent.atomic.AtomicInteger

sealed class PoolType

object PoolUI : PoolType()

val PoolGlobal = Pool("Global")
val PoolIO = Pool("IO", 1)

data class Pool(val name: String, val maximumNumberThread: Int = 16) : PoolType()
{
   private val lock = Object()
   private val numberActiveThreads = AtomicInteger(0)
   private val pool = Queue<PoolTask>()

   /**
    * Must be called inside synchronized(this.lock) {.. } block
    */
   private fun postNext()
   {
      if (this.pool.notEmpty && this.numberActiveThreads.get() < this.maximumNumberThread)
      {
         this.numberActiveThreads.incrementAndGet()
         PoolManager.postInternal(this.pool.outqueue())
      }
   }

   internal fun oneTaskFinished()
   {
      synchronized(this.lock) {
         this.numberActiveThreads.decrementAndGet()
         this.postNext()
      }
   }

   fun post(task: () -> Unit)
   {
      synchronized(this.lock) {
         this.pool.inqueue(PoolTask(this, task))
         this.postNext()
      }
   }
}