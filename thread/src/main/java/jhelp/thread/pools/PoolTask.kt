package jhelp.thread.pools

internal class PoolTask(private val pool: Pool, private val task: () -> Unit) : Runnable
{
   override fun run()
   {
      try
      {
         this.task()
      }
      finally
      {
         this.pool.oneTaskFinished()
      }
   }
}