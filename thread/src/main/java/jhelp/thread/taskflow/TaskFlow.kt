package jhelp.thread.taskflow

import jhelp.thread.pools.PoolGlobal
import jhelp.thread.pools.PoolType
import jhelp.thread.pools.delayed
import jhelp.thread.pools.parallel
import jhelp.thread.pools.repeat
import jhelp.thread.promise.future
import java.util.concurrent.atomic.AtomicBoolean

object NO_PARAMETER

class TaskFlow<P : Any, R : Any> internal constructor(private val task: (P) -> R, private val poolType: PoolType)
{
   private val parameterSet = AtomicBoolean(false)
   private lateinit var parameter: P
   val isParameterSet get() = this.parameterSet.get()
   private var errorTask: ((Exception) -> Unit)? = null
   private val lock = Object()

   fun parameter(parameter: P)
   {
      synchronized(this.lock)
      {
         if (!this.parameterSet.getAndSet(true))
         {
            this.parameter = parameter
         }
      }
   }

   fun onError(errorTask: (Exception) -> Unit): TaskFlow<P, R>
   {
      this.errorTask = errorTask
      return this
   }

   fun <R1 : Any> then(continuation: (R) -> R1, poolType: PoolType = this.poolType): TaskFlow<P, R1>
   {
      val taskFlow = TaskFlow({ parameter: P ->
                                 this.task.future(this.poolType)(parameter)
                                    .then(continuation, poolType)
                                    .onError { this@TaskFlow.errorTask?.invoke(it) }()
                              }, poolType)

      if (this.parameterSet.get())
      {
         taskFlow.parameter(this.parameter)
      }

      taskFlow.errorTask = this.errorTask
      return taskFlow
   }

   fun execute(parameter: P)
   {
      synchronized(this.lock)
      {
         this.parameterSet.set(false)
         this.parameter(parameter)
         this.execute()
      }
   }

   fun execute()
   {
      check(this.parameterSet.get()) { "Parameter is not set" }

      synchronized(this.lock)
      {
         val parameter = this.parameter

         {
            try
            {
               this.task(parameter)
            }
            catch (exception: Exception)
            {
               this.errorTask?.invoke(exception)
            }

            Unit
         }.parallel(this.poolType)
      }
   }

   fun delayed(delay: Long) = { this.execute() }.delayed(delay)

   fun delayed(parameter: P, delay: Long) = { this.execute(parameter) }.delayed(delay)

   fun repeat(delay: Long) = { this.execute() }.repeat(delay, delay)

   fun repeat(parameter: P, delay: Long) = { this.execute(parameter) }.repeat(delay, delay)
}

fun <R : Any> taskFlow(task: () -> R, poolType: PoolType = PoolGlobal): TaskFlow<NO_PARAMETER, R>
{
   val taskFlow = TaskFlow({ _: NO_PARAMETER -> task() }, poolType)
   taskFlow.parameter(NO_PARAMETER)
   return taskFlow
}

fun <P : Any, R : Any> taskFlow(task: (P) -> R, poolType: PoolType = PoolGlobal) = TaskFlow(task, poolType)
