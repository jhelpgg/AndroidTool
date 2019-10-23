package jhelp.tests.thread

internal class ThreadTask(private var task: Runnable?) : Thread()
{
   override fun run()
   {
      while (this.task != null)
      {
         try
         {
            this.task?.run()
         }
         catch (_: Exception)
         {
         }

         this.task = ThreadPool.aThreadIsFree()
      }
   }
}