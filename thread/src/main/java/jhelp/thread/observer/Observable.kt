package jhelp.thread.observer

import jhelp.sets.removeOn
import jhelp.thread.pools.parallel
import jhelp.thread.promise.FutureResult
import jhelp.thread.promise.Promise
import jhelp.thread.taskflow.TaskFlow

abstract class Observable<O : Observable<O>>
{
   private val observers = ArrayList<Pair<MatcherObservable<O>, Observer<O>>>()

   fun observe(observer: (O) -> Unit, matchObservable: (O) -> Boolean = { true }) =
      this.observe(observer.observer(), matchObservable.matcher())

   fun observe(observer: Observer<O>, matchObservable: MatcherObservable<O> = matchAll())
   {
      val copy = this.copy()

      if (matchObservable.match(copy))
      {
         { observer.observableChanged(copy) }.parallel()
      }

      synchronized(this.observers) {
         this.observers.add(Pair(matchObservable, observer))
      }
   }

   fun stopObserve(observer: Observer<O>)
   {
      synchronized(this.observers) {
         this.observers.removeOn { it.first == observer }
      }
   }

   /**
    * Call it to signal any change
    */
   protected fun fireChange()
   {
      val copy = this.copy()

      ({
         synchronized(this.observers) {
            for ((matcher, observer) in this.observers)
            {
               if (matcher.match(copy))
               {
                  { observer.observableChanged(copy) }.parallel()
               }
            }
         }
      }).parallel()
   }

   abstract fun copy(): O

   fun oneTime(matchObservable: (O) -> Boolean) = this.oneTime(matchObservable.matcher())

   fun oneTime(matchObservable: MatcherObservable<O> = matchAll()): FutureResult<O>
   {
      val promise = Promise<O>()
      val observer = object : Observer<O>
      {
         override fun observableChanged(observable: O)
         {
            this@Observable.stopObserve(this)
            promise.result(observable)
         }
      }
      this.observe(observer, matchObservable)
      return promise.futureResult
   }

   fun <R : Any> eachTime(taskFlow: TaskFlow<O, R>, matchObservable: (O) -> Boolean) =
      this.eachTime(taskFlow, matchObservable.matcher())

   fun <R : Any> eachTime(taskFlow: TaskFlow<O, R>, matchObservable: MatcherObservable<O> = matchAll()): Observer<O>
   {
      val observer = object : Observer<O>
      {
         override fun observableChanged(observable: O)
         {
            taskFlow.execute(observable)
         }
      }

      this.observe(observer, matchObservable)
      return observer
   }
}