package jhelp.thread.promise

import android.os.Build.VERSION_CODES
import jhelp.tests.initializeTests
import jhelp.thread.pools.delayed
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class PromiseFutureTests
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
   fun resultTest()
   {
      val promise = Promise<String>()
      val future = promise.futureResult

      { promise.result("Youpi") }.delayed(1024)

      Assert.assertEquals(FutureResultStatus.COMPUTING, future.futureResultStatus)
      Assert.assertEquals("Youpi", future())
      Assert.assertEquals(FutureResultStatus.SUCCEED, future.futureResultStatus)
   }

   @Test
   fun thenTest()
   {
      val future1 = { 73 }.then({ it.toString() })
      Assert.assertEquals("73", future1())

      val future2 = { value: Int -> value + 1 }.then({ it.toString() })
      Assert.assertEquals("42", future2(41)())
      Assert.assertEquals("666", future2(665)())

      val future3 = 68.future().thenUnwrap(future2)
      Assert.assertEquals("69", future3())
   }

   @Test
   fun cancelTest()
   {
      var cancelReason = ""
      val promise = Promise<String>()
      val future1 = promise.futureResult

      promise.registerPromiseCancelListener { _, reason ->
         cancelReason = reason + "1"
      }

      future1.cancel("Reason")
      Assert.assertEquals("Reason1", cancelReason)

      val future2 = { Thread.sleep(4096) }.future { reason -> cancelReason = reason + "2" }
      future2.cancel("Youpi")
      Assert.assertEquals("Youpi2", cancelReason)
   }

   @Test
   fun errorTest()
   {
      val future1 = { x: Int -> 3 / x }.future()
      Assert.assertEquals(3, future1(1)())

      try
      {
         future1(3).error()
         Assert.fail("Should throw an exception")
      }
      catch (_: Exception)
      {
         //Perfect
      }

      var result = future1(3).result()
      Assert.assertNotNull(result)
      Assert.assertEquals(1, result)

      result = future1(0).result()
      Assert.assertNull(result)

      val illegalArgumentException = IllegalArgumentException("Test")
      val futureException = futureFailed<Int>(illegalArgumentException)
      Assert.assertEquals(illegalArgumentException, futureException.error())

      val futureCanceled = futureCanceled<Boolean>("Some reason")
      val cancelException = futureCanceled.error()
      Assert.assertTrue(cancelException.javaClass.name, cancelException is FutureCanceledException)
      Assert.assertEquals("Future canceled because : Some reason", cancelException.message)

      try
      {
         future1(0)()
         Assert.fail("Should throw an ArithmeticException")
      }
      catch (exception: ArithmeticException)
      {
         // Perfect
      }

      future1(0).onError {
         Assert.assertTrue(
            "Should be an ArithmeticException, not ${it.javaClass.name}",
            it is ArithmeticException
                          )
      }.waitResult()

      val exception = future1(0).error()
      Assert.assertTrue(
         "Should be an ArithmeticException, not ${exception.javaClass.name}",
         exception is ArithmeticException
                       )

   }
}