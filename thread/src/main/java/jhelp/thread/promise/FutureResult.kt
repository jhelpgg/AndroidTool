package jhelp.thread.promise

import jhelp.thread.pools.PoolGlobal
import jhelp.thread.pools.PoolType
import jhelp.thread.pools.PoolUI
import jhelp.thread.pools.parallel
import java.util.concurrent.atomic.AtomicBoolean

private fun <R : Any, R1 : Any> continuationFutureResultListener(
   futureResult: FutureResult<R>,
   continuation: (R) -> R1,
   promiseWhereReport: Promise<R1>
                                                                ) =
   when (futureResult.futureResultStatus)
   {
      FutureResultStatus.SUCCEED ->
         try
         {
            promiseWhereReport.result(continuation(futureResult()))
         }
         catch (exception: Exception)
         {
            promiseWhereReport.error(exception)
         }
      FutureResultStatus.FAILED  -> promiseWhereReport.error(futureResult.error())
      else                       -> Unit
   }

class FutureResult<R : Any> internal constructor(val promise: Promise<R>)
{
   var futureResultStatus = FutureResultStatus.COMPUTING
      private set
   private val lock = Object()
   private val atLeastOneWaiter = AtomicBoolean(false)
   private lateinit var result: R
   private lateinit var exception: Exception
   private val futureResultListeners = ArrayList<FutureResultListener<R>>()

   private fun futureFinished()
   {
      synchronized(this.lock) {
         if (this.atLeastOneWaiter.getAndSet(false))
         {
            this.lock.notifyAll()
         }

         synchronized(this.futureResultListeners) {
            for (futureResultListener in this.futureResultListeners)
            {
               futureResultListener.futureFinished(this)
            }

            this.futureResultListeners.clear()
            this.futureResultListeners.trimToSize()
         }
      }
   }

   internal fun result(result: R)
   {
      synchronized(this.lock) {
         if (this.futureResultStatus == FutureResultStatus.COMPUTING)
         {
            this.futureResultStatus = FutureResultStatus.SUCCEED
            this.result = result
            this.futureFinished()
         }
      }
   }

   internal fun error(exception: Exception)
   {
      synchronized(this.lock) {
         if (this.futureResultStatus == FutureResultStatus.COMPUTING)
         {
            this.futureResultStatus = FutureResultStatus.FAILED
            this.exception = exception
            this.futureFinished()
         }
      }
   }

   fun cancel(reason: String)
   {
      synchronized(this.lock) {
         if (this.futureResultStatus == FutureResultStatus.COMPUTING)
         {
            this.futureResultStatus = FutureResultStatus.CANCELED
            this.exception = FutureCanceledException(reason)
            this.promise.cancel(reason)
            this.futureFinished()
         }
      }
   }

   /**
    * Must be called inside ```synchronized(this.lock) {``` block
    */
   private fun waitingFinished()
   {
      if (this.futureResultStatus == FutureResultStatus.COMPUTING)
      {
         this.atLeastOneWaiter.set(true)
         this.lock.wait()
      }
   }

   @Throws(Exception::class)
   operator fun invoke(): R
   {
      synchronized(this.lock) {
         this.waitingFinished()

         when (this.futureResultStatus)
         {
            FutureResultStatus.SUCCEED -> return this.result
            else                       -> throw this.exception
         }
      }
   }

   @Throws(Exception::class)
   fun error(): Exception
   {
      synchronized(this.lock) {
         this.waitingFinished()

         return when (this.futureResultStatus)
         {
            FutureResultStatus.SUCCEED -> throw Exception("Not in error")
            else                       -> this.exception
         }
      }
   }

   fun waitResult(): FutureResultStatus
   {
      synchronized(this.lock) {
         this.waitingFinished()
         return this.futureResultStatus
      }
   }

   fun result() =
      try
      {
         this()
      }
      catch (_: Exception)
      {
         null
      }

   fun registerFutureResultListener(futureResultListener: (FutureResult<R>) -> Unit) =
      this.registerFutureResultListener(object : FutureResultListener<R>
                                        {
                                           override fun futureFinished(futureResult: FutureResult<R>)
                                           {
                                              futureResultListener(futureResult)
                                           }
                                        })

   fun registerFutureResultListener(futureResultListener: FutureResultListener<R>)
   {
      synchronized(this.lock) {
         if (this.futureResultStatus != FutureResultStatus.COMPUTING)
         {
            futureResultListener.futureFinished(this)
            return
         }

         synchronized(this.futureResultListeners) {
            if (!this.futureResultListeners.contains(futureResultListener))
            {
               this.futureResultListeners.add(futureResultListener)
            }
         }
      }
   }

   /**
    * The execution thread of the task is :
    * * If the future is direct from a promise: The tread where the result is pushed.
    * * If the future comes from a then, use the same thread used for compute previous result
    */
   fun <R1 : Any> then(task: (R) -> R1): FutureResult<R1>
   {
      val promise = Promise<R1>()
      promise.registerPromiseCancelListener { _, reason -> this.cancel(reason) }
      this.registerFutureResultListener { continuationFutureResultListener(it, task, promise) }
      return promise.futureResult
   }

   /**
    * Change the thread where the then will be computed.
    *
    * Use [PoolUI] is safe if the given task respects the UI thread rule : only do UI change, no heavy operation.
    * In that case, if something else follow by a ```then```, it is recommended to change thread by using this method
    * version with, by example, [PoolGlobal]
    */
   fun <R1 : Any> then(task: (R) -> R1, poolType: PoolType): FutureResult<R1>
   {
      val promise = Promise<R1>()
      promise.registerPromiseCancelListener { _, reason -> this.cancel(reason) }
      this.registerFutureResultListener { { continuationFutureResultListener(it, task, promise) }.parallel(poolType) }
      return promise.futureResult
   }

   fun <R1 : Any> thenUnwrap(task: (R) -> FutureResult<R1>) = this.then(task).unwrap()

   fun <R1 : Any> thenUnwrap(task: (R) -> FutureResult<R1>, poolType: PoolType) =
      this.then(task, poolType).unwrap()

   fun onError(task: (Exception) -> Unit): FutureResult<R>
   {
      this.registerFutureResultListener { futureResult ->
         if (futureResult.futureResultStatus == FutureResultStatus.FAILED)
         {
            task(futureResult.exception)
         }
      }

      return this
   }
}

fun <T : Any> futureFailed(exception: Exception): FutureResult<T>
{
   val promise = Promise<T>()
   promise.error(exception)
   return promise.futureResult
}

fun <T : Any> futureCanceled(reason: String): FutureResult<T>
{
   val promise = Promise<T>()
   promise.futureResult.cancel(reason)
   return promise.futureResult
}

fun <T : Any> T.future(): FutureResult<T>
{
   val promise = Promise<T>()
   promise.result(this)
   return promise.futureResult
}

fun <T : Any> (() -> T).future(poolType: PoolType = PoolGlobal, cancelListener: (String) -> Unit = {}): FutureResult<T>
{
   val promise = Promise<T>()
   promise.registerPromiseCancelListener { _, reason -> cancelListener(reason) }

   ({
      try
      {
         promise.result(this())
      }
      catch (exception: Exception)
      {
         promise.error(exception)
      }
   }).parallel(poolType)

   return promise.futureResult
}

fun <P, R : Any> ((P) -> R).future(
   poolType: PoolType = PoolGlobal,
   cancelListener: (String) -> Unit = {}
                                  ): (P) -> FutureResult<R> =
   { parameter ->
      val promise = Promise<R>()
      promise.registerPromiseCancelListener { _, reason -> cancelListener(reason) }

      ({
         try
         {
            promise.result(this(parameter))
         }
         catch (exception: Exception)
         {
            promise.error(exception)
         }
      }).parallel(poolType)

      promise.futureResult
   }

fun <R : Any> FutureResult<FutureResult<R>>.unwrap(): FutureResult<R>
{
   val promise = Promise<R>()

   promise.registerPromiseCancelListener { _, reason ->
      this.cancel(reason)
   }

   this.then { future ->
      try
      {
         promise.result(future())
      }
      catch (exception: Exception)
      {
         promise.error(exception)
      }
   }

   return promise.futureResult
}

fun <R1 : Any, R2 : Any> (() -> R1).then(
   continuation: (R1) -> R2,
   poolType: PoolType = PoolGlobal,
   cancelListener: (String) -> Unit = {}
                                        ) =
   this.future(poolType, cancelListener).then(continuation)

fun <P, R1 : Any, R2 : Any> ((P) -> R1).then(
   continuation: (R1) -> R2,
   poolType: PoolType = PoolGlobal,
   cancelListener: (String) -> Unit = {}
                                            ) =
   { parameter: P -> this.future(poolType, cancelListener)(parameter).then(continuation) }