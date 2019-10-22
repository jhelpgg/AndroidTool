package jhelp.thread.pools

import java.util.PriorityQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

internal object DelayedTaskManager : Runnable
{
   private val waiting = AtomicBoolean(false)
   private val lock = Object()
   private val priorityQueue = PriorityQueue<DelayedTaskInformation>()
   private val running = AtomicBoolean(false)

   fun delayTask(task: () -> Unit, poolType: PoolType, delayMilliseconds: Long): CancelableTask
   {
      val delayedTaskInformation = DelayedTaskInformation(
         System.currentTimeMillis() + max(1L, delayMilliseconds),
         task,
         poolType
                                                         )
      synchronized(this.priorityQueue) {
         this.priorityQueue.offer(delayedTaskInformation)
      }

      if (this.running.getAndSet(true))
      {
         synchronized(this.lock) {
            if (this.waiting.get())
            {
               this.lock.notify()
            }
         }
      }
      else
      {
         PoolManager.postInternal(this)
      }

      return delayedTaskInformation
   }

   override fun run()
   {
      var delayedTaskInformation: DelayedTaskInformation?

      do
      {
         delayedTaskInformation = synchronized(this.priorityQueue) {
            if (this.priorityQueue.isEmpty())
            {
               null
            }
            else
            {
               this.priorityQueue.peek()
            }
         }

         if (delayedTaskInformation != null)
         {
            if (delayedTaskInformation.canceled)
            {
               synchronized(this.priorityQueue) { this.priorityQueue.remove(delayedTaskInformation) }
            }
            else
            {
               val timeLeft = delayedTaskInformation.timeWhenPlay - System.currentTimeMillis()

               if (timeLeft <= 0)
               {
                  synchronized(this.priorityQueue) { this.priorityQueue.remove(delayedTaskInformation) }
                  PoolManager.post(delayedTaskInformation.task, delayedTaskInformation.poolType)
               }
               else
               {
                  synchronized(this.lock) {
                     this.waiting.set(true)
                     this.lock.wait(timeLeft)
                     this.waiting.set(false)
                  }
               }
            }
         }
      }
      while (delayedTaskInformation != null)

      this.running.set(false)
   }
}