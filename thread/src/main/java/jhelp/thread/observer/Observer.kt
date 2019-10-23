package jhelp.thread.observer

/**
 * Observe an [Observable] changes.
 * The calling to this listener can be restraint to some condition with a [MatcherObservable]
 */
interface Observer<O : Observable<O>>
{
   /**
    * Called each time [Observable] changes in a matching condition
    */
   fun observableChanged(observable: O)
}

/**
 * Transform a function that consume an [Observable] to an [Observer]
 */
fun <O : Observable<O>> ((O) -> Unit).observer() =
   object : Observer<O>
   {
      override fun observableChanged(observable: O)
      {
         this@observer(observable)
      }
   }
