package jhelp.thread.observer

/**
 * [Observer] will be triggered only if observed [Observable] match this condition
 */
interface MatcherObservable<O : Observable<O>>
{
   /**
    * Indicates if given [Observable] match the condition
    */
   fun match(observable: O): Boolean
}

/**
 * Transform a function that takes an [Observable] as parameter and returns a boolean to a [MatcherObservable]
 */
fun <O : Observable<O>> ((O) -> Boolean).matcher() =
   object : MatcherObservable<O>
   {
      override fun match(observable: O) = this@matcher(observable)
   }

/**
 * For match all change.
 * In other words, the [Observer] will be alert for every change
 */
fun <O : Observable<O>> matchAll() = { _: O -> true }.matcher()