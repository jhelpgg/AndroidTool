package jhelp.thread.pools

internal class PoolManagerTask(private val runnable: Runnable) : Runnable
{
   override fun run()
   {
      var runnable: Runnable? = this.runnable

      while (runnable != null)
      {
         try
         {
            runnable.run()
         }
         finally
         {
            runnable = PoolManager.oneThreadFinished()
         }
      }
   }
}