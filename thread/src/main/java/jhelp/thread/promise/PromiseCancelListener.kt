package jhelp.thread.promise

interface PromiseCancelListener<R : Any>
{
   fun promiseCanceled(promise: Promise<R>, reason: String)
}