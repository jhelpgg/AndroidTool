package jhelp.thread.observer

import jhelp.sets.removeOn
import jhelp.thread.pools.parallel
import jhelp.thread.promise.FutureResult
import jhelp.thread.promise.Promise
import jhelp.thread.taskflow.TaskFlow

/**
 * An [Observable] its an object that represents a value which can change.
 *
 * [Observer] can [Observable.observe] to be alert on any change or specific change.
 *
 * Implementation should care :
 * * [Observable.copy] : Must copy at least all information that [Observer]s may use
 * * Call [Observable.fireChange] each time an inside value change.
 *   It can be also called regularly if need. It designed to be thread-safe.
 * ]
 */
abstract class Observable<O : Observable<O>>
{
   /**Registered [Observer]s and their associated [MatcherObservable]*/
   private val observers = ArrayList<Pair<MatcherObservable<O>, Observer<O>>>()

   /**
    * Register an [Observer] called if by given [MatcherObservable] satisfied
    *
    * @param observer [Observer] registered to changes
    * @param matchObservable Determine if [Observer] called for specific change.
    *                        If not given, [Observer] will be called for any change
    */
   fun observe(observer: (O) -> Unit, matchObservable: (O) -> Boolean = { true }) =
      this.observe(observer.observer(), matchObservable.matcher())

   /**
    * Register an [Observer] called if by given [MatcherObservable] satisfied
    *
    * @param observer [Observer] registered to changes
    * @param matchObservable Determine if [Observer] called for specific change.
    *                        If not given, [Observer] will be called for any change
    */
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

   /**
    * Un register an [Observer]. It will be never more called
    */
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

   /**
    * Copy the [Observable]
    */
   abstract fun copy(): O

   /**
    * Register for next time a given condition match.
    *
    * It returns a [FutureResult] to be able receive the matching state and do action with it when it happen.
    *
    * Note:
    * > It will be called immediately, if [Observable] already match tne condition
    */
   fun oneTime(matchObservable: (O) -> Boolean) = this.oneTime(matchObservable.matcher())

   /**
    * Register for next time a given condition match.
    *
    * It returns a [FutureResult] to be able receive the matching state and do action with it when it happen.
    *
    * Note:
    * > It will be called immediately, if [Observable] already match tne condition
    */
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

   /**
    * Register a [TaskFlow] to play each time given condition match.
    *
    * It returns an observer to be able to stop be call again with [Observable.stopObserve]
    */
   fun <R : Any> eachTime(taskFlow: TaskFlow<O, R>, matchObservable: (O) -> Boolean) =
      this.eachTime(taskFlow, matchObservable.matcher())


   /**
    * Register a [TaskFlow] to play each time given condition match.
    *
    * It returns an observer to be able to stop be call again with [Observable.stopObserve]
    */
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