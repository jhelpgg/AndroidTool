package jhelp.thread.promise

interface FutureResultListener<R:Any>
{
   fun futureFinished(futureResult: FutureResult<R>)
}