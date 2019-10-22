package jhelp.thread.pools

internal class RepeatableTask(private val task: () -> Unit, private val poolType: PoolType, private val delay: Long) :
   CancelableTask
{
   private var cancelableTaskDelayed: CancelableTask? = null
   private var canceled = false

   fun run()
   {
      this.cancelableTaskDelayed = null
      
      if (this.canceled)
      {
         return
      }

      PoolManager.post({
                          try
                          {
                             this.task()
                          }
                          catch (_: Exception)
                          {
                          }

                          if (!this.canceled)
                          {
                             this.cancelableTaskDelayed = PoolManager.postDelayed(this::run, this.delay)
                          }
                       }, this.poolType)
   }

   override fun cancel()
   {
      this.canceled = true
      this.cancelableTaskDelayed?.cancel()
   }
}