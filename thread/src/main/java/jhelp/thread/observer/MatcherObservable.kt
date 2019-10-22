package jhelp.thread.observer

interface MatcherObservable<O : Observable<O>>
{
   fun match(observable: O): Boolean
}

fun <O : Observable<O>> ((O) -> Boolean).matcher() =
   object : MatcherObservable<O>
   {
      override fun match(observable: O) = this@matcher(observable)
   }

fun <O : Observable<O>> matchAll() = { _: O -> true }.matcher()