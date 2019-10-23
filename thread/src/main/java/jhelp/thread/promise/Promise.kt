package jhelp.thread.promise

import jhelp.thread.pools.parallel

class Promise<R : Any>
{
   val futureResult = FutureResult(this)
   var canceled = false
      private set
   private var finished = false
   private var cancelReason = ""
   private val cancelListeners = ArrayList<PromiseCancelListener<R>>()
   private val lock = Object()

   internal fun cancel(reason: String)
   {
      synchronized(this.lock)
      {
         this.canceled = true
         this.cancelReason = reason
         this.finished = true
      }

      ({
         synchronized(this.cancelListeners) {
            for (promiseCancelListener in this.cancelListeners)
            {
               promiseCancelListener.promiseCanceled(this, reason)
            }

            this.cancelListeners.clear()
            this.cancelListeners.trimToSize()
         }
      }).parallel()
   }

   fun result(result: R)
   {
      synchronized(this.lock) { this.finished = true }

      ({ this.futureResult.result(result) }).parallel()
   }

   fun error(exception: Exception)
   {
      synchronized(this.lock) { this.finished = true }

      ({ this.futureResult.error(exception) }).parallel()
   }

   fun registerPromiseCancelListener(promiseCancelListener: (promise: Promise<R>, reason: String) -> Unit) =
      this.registerPromiseCancelListener(object : PromiseCancelListener<R>
                                         {
                                            override fun promiseCanceled(promise: Promise<R>, reason: String)
                                            {
                                               promiseCancelListener(promise, reason)
                                            }
                                         })

   fun registerPromiseCancelListener(promiseCancelListener: PromiseCancelListener<R>)
   {
      synchronized(this.lock) {
         if (this.canceled)
         {
            ({ promiseCancelListener.promiseCanceled(this, this.cancelReason) }).parallel()
            return
         }

         if (this.finished)
         {
            return
         }
      }

      synchronized(this.cancelListeners) {
         if (!this.cancelListeners.contains(promiseCancelListener))
         {
            this.cancelListeners.add(promiseCancelListener)
         }
      }
   }
}