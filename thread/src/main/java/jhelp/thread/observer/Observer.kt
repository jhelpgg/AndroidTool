package jhelp.thread.observer

import jhelp.thread.promise.FutureResult

interface Observer<O : Observable<O>>
{
   fun observableChanged(observable: O)
}

fun <O : Observable<O>> ((O) -> Unit).observer() =
   object : Observer<O>
   {
      override fun observableChanged(observable: O)
      {
         this@observer(observable)
      }
   }
