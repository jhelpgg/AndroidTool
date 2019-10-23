package jhelp.thread.taskflow

import android.os.Build.VERSION_CODES
import jhelp.tests.initializeTests
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class TaskFlowTests
{
   companion object
   {
      @JvmStatic
      @BeforeClass
      fun initialize()
      {
         initializeTests(VERSION_CODES.KITKAT)
      }
   }

   @Test
   fun thenErrorFirstTaskOnErrorFirst()
   {
      val errorDetected = AtomicBoolean(false)
      val taskFlow = taskFlow({ divisor: Int -> 3 / divisor })
      taskFlow.onError { errorDetected.set(true) }
         .then({ i: Int -> i - 1 })
         .execute(0)
      Thread.sleep(512)
      Assert.assertTrue(errorDetected.get())
   }

   @Test
   fun thenErrorFirstTaskOnErrorSecond()
   {
      val errorDetected = AtomicBoolean(false)
      val taskFlow = taskFlow({ divisor: Int -> 3 / divisor })
      taskFlow.then({ i: Int -> i - 1 })
         .onError { errorDetected.set(true) }
         .execute(0)
      Thread.sleep(512)
      Assert.assertTrue(errorDetected.get())
   }

   @Test
   fun thenErrorSecondTaskOnErrorFirst()
   {
      val errorDetected = AtomicBoolean(false)
      val taskFlow = taskFlow({ i: Int -> i - 1 })
      taskFlow.onError { errorDetected.set(true) }
         .then({ i: Int -> 4 / i })
         .execute(1)
      Thread.sleep(512)
      Assert.assertTrue(errorDetected.get())
   }

   @Test
   fun thenErrorSecondTaskOnErrorSecond()
   {
      val errorDetected = AtomicBoolean(false)
      val taskFlow = taskFlow({ i: Int -> i - 1 })
      taskFlow.then({ i: Int -> 4 / i })
         .onError { errorDetected.set(true) }
         .execute(1)
      Thread.sleep(512)
      Assert.assertTrue(errorDetected.get())
   }
}