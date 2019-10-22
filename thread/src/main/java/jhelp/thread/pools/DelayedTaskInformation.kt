package jhelp.thread.pools

internal data class DelayedTaskInformation(val timeWhenPlay: Long, val task: () -> Unit, val poolType: PoolType) :
   Comparable<DelayedTaskInformation> , CancelableTask
{
   var canceled = false

   override fun compareTo(other: DelayedTaskInformation): Int
   {
      val comparison = this.timeWhenPlay - other.timeWhenPlay

      return when
      {
         comparison < 0 -> -1
         comparison > 0 -> 1
         else           -> 0
      }
   }

   override fun cancel()
   {
      this.canceled = true
   }
}
