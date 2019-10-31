package jhelp.thread.observer

import android.os.Build.VERSION_CODES
import jhelp.tests.initializeTests
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class ObservableTests
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

   val allStrings = ArrayList<String>()
   val startsWithTests = ArrayList<String>()
   val endsWithTests = ArrayList<String>()

   fun startsWithTest(stringObservable: StringObservable) = stringObservable.value.startsWith("Test")

   fun endsWithTest(stringObservable: StringObservable) = stringObservable.value.endsWith("Test")

   fun collectAll(stringObservable: StringObservable)
   {
      synchronized(this.allStrings) { this.allStrings.add(stringObservable.value) }
   }

   fun collectStartsWithTest(stringObservable: StringObservable)
   {
      synchronized(this.startsWithTests) { this.startsWithTests.add(stringObservable.value) }
   }

   fun collectEndsWithTest(stringObservable: StringObservable)
   {
      synchronized(this.endsWithTests) { this.endsWithTests.add(stringObservable.value) }
   }

   @Test
   fun observableTest()
   {
      val stringObservable = StringObservable()
      stringObservable.observe(this::collectAll)
      stringObservable.observe(this::collectStartsWithTest, this::startsWithTest)
      stringObservable.observe(this::collectEndsWithTest, this::endsWithTest)
      stringObservable.value("Test")
      stringObservable.value("Test - 1")
      stringObservable.value("1 - Test")
      stringObservable.value("Something")
      stringObservable.value("Test - 2")
      stringObservable.value("2 - Test")

      // Wait a little to be sure all reported
      Thread.sleep(512)

      Assert.assertEquals(7, this.allStrings.size)
      Assert.assertEquals(3, this.startsWithTests.size)
      Assert.assertEquals(3, this.endsWithTests.size)

      // The order can't be guaranteed, so use contains to be sure all their
      Assert.assertTrue(this.allStrings.contains(""))
      Assert.assertTrue(this.allStrings.contains("Test"))
      Assert.assertTrue(this.allStrings.contains("Test - 1"))
      Assert.assertTrue(this.allStrings.contains("1 - Test"))
      Assert.assertTrue(this.allStrings.contains("Something"))
      Assert.assertTrue(this.allStrings.contains("Test - 2"))
      Assert.assertTrue(this.allStrings.contains("2 - Test"))

      //
      Assert.assertTrue(this.startsWithTests.contains("Test"))
      Assert.assertTrue(this.startsWithTests.contains("Test - 1"))
      Assert.assertTrue(this.startsWithTests.contains("Test - 2"))

      Assert.assertFalse(this.startsWithTests.contains(""))
      Assert.assertFalse(this.startsWithTests.contains("Something"))
      Assert.assertFalse(this.startsWithTests.contains("1 - Test"))

      //
      Assert.assertTrue(this.endsWithTests.contains("Test"))
      Assert.assertTrue(this.endsWithTests.contains("1 - Test"))
      Assert.assertTrue(this.endsWithTests.contains("2 - Test"))

      Assert.assertFalse(this.endsWithTests.contains(""))
      Assert.assertFalse(this.startsWithTests.contains("Something"))
      Assert.assertFalse(this.endsWithTests.contains("Test - 2"))
   }
}