package jhelp.sets

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalStateException

class QueueTests
{
   @Test
   fun queueNormalUsage()
   {
      val queue = Queue<String>()
      Assert.assertTrue(queue.empty)
      Assert.assertFalse(queue.notEmpty)

      queue.inqueue("one")
      Assert.assertFalse(queue.empty)
      Assert.assertTrue(queue.notEmpty)

      Assert.assertEquals("one", queue.outqueue())
      Assert.assertTrue(queue.empty)
      Assert.assertFalse(queue.notEmpty)

      queue.inqueue("one")
      queue.inqueue("two")
      queue.inqueue("three")
      Assert.assertFalse(queue.empty)
      Assert.assertTrue(queue.notEmpty)

      Assert.assertEquals("one", queue.outqueue())
      Assert.assertFalse(queue.empty)
      Assert.assertTrue(queue.notEmpty)

      queue.inqueue("four")
      Assert.assertFalse(queue.empty)
      Assert.assertTrue(queue.notEmpty)

      Assert.assertEquals("two", queue.outqueue())
      Assert.assertFalse(queue.empty)
      Assert.assertTrue(queue.notEmpty)

      Assert.assertEquals("three", queue.outqueue())
      Assert.assertFalse(queue.empty)
      Assert.assertTrue(queue.notEmpty)

      Assert.assertEquals("four", queue.outqueue())
      Assert.assertTrue(queue.empty)
      Assert.assertFalse(queue.notEmpty)
   }

   @Test
   fun queueException()
   {
      val queue = Queue<String>()

      try
      {
         queue.outqueue()
         Assert.fail("Get element from empty queue should throw IllegalStateException")
      }
      catch (_: IllegalStateException)
      {
         // Expected go there
      }

      queue.inqueue("one")
      queue.outqueue()

      try
      {
         queue.outqueue()
         Assert.fail("Get element from empty queue should throw IllegalStateException")
      }
      catch (_: IllegalStateException)
      {
         // Expected go there
      }

      queue.inqueue("one")
      queue.inqueue("two")
      queue.inqueue("three")
      queue.outqueue()
      queue.outqueue()
      queue.outqueue()

      try
      {
         queue.outqueue()
         Assert.fail("Get element from empty queue should throw IllegalStateException")
      }
      catch (_: IllegalStateException)
      {
         // Expected go there
      }
   }
}