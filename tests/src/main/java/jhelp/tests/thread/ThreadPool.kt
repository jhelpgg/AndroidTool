package jhelp.tests.thread

import jhelp.sets.Queue

const val MAXIMUM_THREAD_NUMBER = 16

object ThreadPool
{
   private var numberRunningThread = 0
   private val lock = Object()
   private val tasks = Queue<Runnable>()

   internal fun aThreadIsFree(): Runnable?
   {
      synchronized(this.lock)
      {
         if (this.tasks.empty)
         {
            this.numberRunningThread--
            return null
         }

         return this.tasks.outqueue()
      }
   }

   fun execute(task: Runnable)
   {
      synchronized(this.lock)
      {
         if (this.numberRunningThread < MAXIMUM_THREAD_NUMBER)
         {
            this.numberRunningThread++
            ThreadTask(task).start()
            return
         }

         this.tasks.inqueue(task)
      }
   }

   fun execute(task: ()->Unit) =
      this.execute(Runnable { task() })
}