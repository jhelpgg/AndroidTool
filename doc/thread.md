# Thread management

1. [Pools](#pools)

1. [Promise and FutureResult](#promise-and-futureresult)

1. [TaskFlow](#taskflow)

1. [Observer and observable](#observer-and-observable)

Those tools are just helpers for thread management.
Each of them responds to a situation, none is perfect.
Its up to developer to choose which one is adapt to the situation. 
(Use the right tool for solve the problem, don't adapt the solution to your tool)

### Pools

Pools are task queue, each pool decide when launch the task and limit the number of parallel task in same time.
The library let opportunity to use pre-build pools or create custom ones.
Pre-build pools :
* ```jhelp.thread.pools.PoolGlobal```  : The default pool of this library. Use it to launch task in separate thread.
* ```jhelp.thread.pools.PoolNetwork``` : Recommended pool for any network operation.
* ```jhelp.thread.pools.PoolUI``` : Specific Android pool for any UI operation. Do only UI operation inside and no heavy operation.

For create custom pool, create an instance of ```jhelp.thread.pools.Pool```. 
Don't be too greedy with the number maximum parallel task, else may experience other pools be starved.
We recommend a number between 1 to 4. 

Use ```jhelp.thread.pools.PoolManager``` methods or one of associated Kotlin extension :

```
+---------------+-----------+------------------------------------------------------------------------------+
|    Method     | Extension |                                 Description                                  |
+===============+===========+==============================================================================+
| post          | parallel  | Post the task in given pool or the global one if not specified               |
+---------------+-----------+------------------------------------------------------------------------------+
| postUI        | uiThread  | Same as above in UI thread                                                   | 
+---------------+-----------+------------------------------------------------------------------------------+
|               |           | For post in given delayed time.                                              | 
| postDelayed   | delayed   | Use given pool or the global one if not specified.                           | 
|               |           | It returns a CancelableTask to be able cancel the post before delay expires. | 
|               |           | Can be used, by example, for a timeout.                                      |
+---------------+-----------+------------------------------------------------------------------------------+
| postUIDelayed | delayedUI | Same as above in UI Thread                                                   |
+---------------+-----------+------------------------------------------------------------------------------+
|               |           | Repeat a task every given time                                               |
| repeat        | repeat    | Use given pool or the global one if not specified                            | 
|               |           | It returns a CancelableTask to be able cancel the repetition.                |
+---------------+-----------+------------------------------------------------------------------------------+
| repeatUI      | repeatUI  | Do as aboove in UI thread                                                    |
+---------------+-----------+------------------------------------------------------------------------------+
```

Examples : 

```kotlin
this::requestNetworkSettings.parallel(PoolNetwork)
// ...
{ this.textViewTitle.setText(newTitle) }.uiThread()
// ...
this::computeSomethingHeavy.delayed(1000L)
// ...
val cancelableLogReport = this::reportLogToServer.repeat(1000L, 60_000L, PoolNetwork)
```

### Promise and FutureResult

The idea behind promise and future, is when do an operation in a thread give an object that wil contains the future result.
With this object the receiver have possibility to chain other tasks just after the result is known, react to errors or try cancel the current task, if possible.

In an other words, chain a list of tasks as if we already know the result, and they are played when the result is effectively known.

For example, imagine we have the function ```fun requestTheNumberOfServerActiveUser():Int``` that request to a server to number of active user.
Then withe the result print it in our application ```fun printNumberOfActiveUser(numberOfActiveUser:Int)``` 

Now we want combine those methods to print the active users when the request result in known.

We can't write something like :

```kotlin
printNumberOfActiveUser(requestTheNumberOfServerActiveUser())
```

because ```printNumberOfActiveUser``` must be called in UI thread and ```requestTheNumberOfServerAcitiveUser```
can't be. Because Network operation not allowed in UI thread, and if we create a mechanism that wait the answer,
we may experience an ANR if the request takes too long. 

It is possible to play with callbacks, listeners or [Pools](#pools), but the code becomes complex and not easy to guess the chaining.

Here we show solution using promise and future.

In a method that call requestTheNumberOfServerActiveUser we create a ```Promise```,
for say : "I don't know the result now, but when I know it, I promise to give the result"

```kotlin
   fun doRequestTheNumberOfServerActiveUser(): FutureResult<Int>
   {
      val promise = Promise<Int>()

      ({
         try
         {
            promise.result(this.requestTheNumberOfServerActiveUser())
         }
         catch (exception: Exception)
         {
            promise.error(exception)
         }
      }).parallel()

      return promise.futureResult
   }
```

and chain with

```kotlin
      this.doRequestTheNumberOfServerActiveUser()
         .then({ this.printNumberOfActiveUser(it) }, PoolUI)
```

We specify to use the ```PoolUI``` in ```then```, because it is required for ```printNumberOfActiveUser``` to be played in UI thread. 

By default the first then is played in same pool as where the ```Promise.result``` is called.
If chain by more ```then```, the second is by default in same pool as first, third same as second, ...

So, if want chain a task with an operation should not play in UI thread after one played in it, have to specify the pool.

```FutureResult``` offer the possibility to react to errors with ```FutureResult.onError``` method.

If one of chaining task failed on error, all the rest following tasks are on error too.

If the promise creator registered to cancel events and react to it, cancel a ```FutreResult```, can cacnel it while it executing.
Else cancel just not call the following tasks. 

It is possible to know a ```FutureResult``` at any time. But it is not recommended to loop on it.
Prefer use ```invoke```, ```error``` or ```waitResult``` to block current thread until response is known. 

### TaskFlow

Task flow just chain tasks and ply them at any time. 
While promise and future are played for one result, when it known the all process not repeat, 
here the task flow can be played any number of times.

Create a task flow can be from a method that take zero or one parameter. 
If take one parameter, have to specify it later before execute the task flow.

Examples :

```kotlin
      taskFlow(this::computePI).then(Double::toString).then(textView::setText, PoolUI).execute()
      // ...
      val taskFlow = taskFlow(this::computeFibonachi).then(Long::toString).then(textView::setText, PoolUI)
      // ...
      taskFlow.parameter(42)
      // ...
      taskFlow.execute()
      // ....
      taskFlow.execute(73)
```

For thread safety reason, the method parameter can be called just once, after it is ignored (or ignored after execute with parameter)
To change after value use execute with parameter.

As ```FutureResult```, the pool execution by default on then is the same as the previous one. 
First then depends by default to pool given at  task flow creation.
By default, task flow use global pool. 

### Observer and observable

An observable is an object that may change value, and alert its observers for any change or specific ones.

To create an obserbale just extends ```Observable``` class.

Example:

```kotlin
import jhelp.thread.observer.Observable

class StringObservable : Observable<StringObservable>()
{
   var value = ""
      private set

   override fun copy(): StringObservable
   {
      val copy = StringObservable()
      copy.value = this.value
      return copy
   }

   fun value(value: String)
   {
      if (this.value != value)
      {
         this.value = value
         this.fireChange()
      }
   }
}
```

The method to override is ```copy```. 
It is really important here to copy at least all information that observers may use.
It is highly recommended to return a new instance from copy method.

For alert listener to change, call the protected method ```fireChange``` for any change.
The method is protected by purpose to avoid an external calls and keep control.

Warning:
> If make this method public in implementation. 
> It exposes the possibility to call listeners from anywhere at anytime. 
> So it may lead to have lot off callback for nothing.

To be alert on change register an ```Observer```. 
It can be created on implements ```Observer``` interface 
or be any void method (or lambda) that consume an ```Observable``` 

When register an observer to ```observe``` an observable, it is possible to associate a ```MatcherObservable```.
It act like a filter. When observable change and if the result state validate the ```MatcherObservable```, 
the observer is called.

An example of this mechanism can be found in [Observable unit test](../thread/src/test/java/jhelp/thread/observer/ObservableTests.kt)

