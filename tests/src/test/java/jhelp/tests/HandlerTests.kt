package jhelp.tests

import android.os.Handler
import org.junit.Assert
import org.junit.Test

class HandlerTests
{
   private var value = 0

   @Test
   fun postTest()
   {
      this.value = 0
      val handler = Handler()
      handler.post(Runnable { this.value = 42 })
      Assert.assertEquals(42, this.value)
   }
}