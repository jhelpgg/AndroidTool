package jhelp.thread.pools

import android.os.Handler
import android.os.Looper
import jhelp.sets.Queue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

const val MAXIMUM_NUMBER_THREAD = 8

object PoolManager
{
   private val uiHandler = Handler(Looper.getMainLooper())
   private val lock = Object()
   private val numberActiveThreads = AtomicInteger(0)
   private val pool = Queue<PoolManagerTask>()

   internal fun oneThreadFinished(): Runnable?
   {
      synchronized(this.lock) {
         if (this.pool.notEmpty)
         {
            return this.pool.outqueue()
         }

         this.numberActiveThreads.decrementAndGet()
         return null
      }
   }

   internal fun postInternal(runnable: Runnable)
   {
      synchronized(this.lock) {
         this.pool.inqueue(PoolManagerTask(runnable))

         if (this.numberActiveThreads.get() < MAXIMUM_NUMBER_THREAD)
         {
            this.numberActiveThreads.incrementAndGet()
            Thread(this.pool.outqueue()).start()
         }
      }
   }

   fun post(task: () -> Unit, poolType: PoolType = PoolGlobal)
   {
      when (poolType)
      {
         PoolUI  -> this.uiHandler.post(task)

         is Pool -> poolType.post(task)
      }
   }

   fun postUI(task: () -> Unit) = this.post(task, PoolUI)

   fun postDelayed(task: () -> Unit, delayMilliseconds: Long, poolType: PoolType = PoolGlobal) =
      when (poolType)
      {
         PoolUI ->
         {
            this.uiHandler.postDelayed(task, delayMilliseconds)

            object : CancelableTask
            {
               override fun cancel()
               {
                  this@PoolManager.uiHandler.removeCallbacks(task)
               }
            }
         }

         else   -> DelayedTaskManager.delayTask(task, poolType, delayMilliseconds)
      }


   fun postUIDelayed(task: () -> Unit, delayMilliseconds: Long) =
      this.postDelayed(task, delayMilliseconds, PoolUI)

   fun repeat(
      task: () -> Unit,
      delayMilliseconds: Long,
      repeatDelay: Long,
      poolType: PoolType = PoolGlobal
             ): CancelableTask
   {
      val repeatableTask = RepeatableTask(task, poolType, max(1, repeatDelay))
      this.postDelayed(repeatableTask::run, delayMilliseconds)
      return repeatableTask
   }


   fun repeatUI(
      task: () -> Unit,
      delayMilliseconds: Long,
      repeatDelay: Long
               ) = this.repeat(task, delayMilliseconds, repeatDelay, PoolUI)
}

fun (() -> Unit).parallel(poolType: PoolType = PoolGlobal) = PoolManager.post(this, poolType)

fun (() -> Unit).uiThread() = this.parallel(PoolUI)

fun (() -> Unit).delayed(delayMilliseconds: Long, poolType: PoolType = PoolGlobal) =
   PoolManager.postDelayed(this, delayMilliseconds, poolType)

fun (() -> Unit).delayedUI(delayMilliseconds: Long) = this.delayed(delayMilliseconds, PoolUI)

fun (() -> Unit).repeat(delayMilliseconds: Long, repeatDelay: Long, poolType: PoolType = PoolGlobal) =
   PoolManager.repeat(this, delayMilliseconds, repeatDelay, poolType)

fun (() -> Unit).repeatUI(delayMilliseconds: Long, repeatDelay: Long) =
   this.repeat(delayMilliseconds, repeatDelay, PoolUI)
