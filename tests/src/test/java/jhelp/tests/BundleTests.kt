package jhelp.tests

import android.os.Build
import android.os.Bundle
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class BundleTests
{
   companion object
   {
      @JvmStatic
      @BeforeClass
      fun initialize()
      {
         initializeTests(Build.VERSION_CODES.KITKAT)
      }
   }

   @Test
   fun primitives()
   {
      println(Build.VERSION.SDK_INT)
      val bundle = Bundle()
      bundle.putBoolean("boolean", true)
      bundle.putChar("char", '$')
      bundle.putByte("byte", 42.toByte())
      bundle.putShort("short", 73.toShort())
      bundle.putInt("int", 666)
      bundle.putLong("long", 69L)
      bundle.putFloat("float", 1234567879f)
      bundle.putDouble("double", 0.123456789)

      Assert.assertEquals(true, bundle.getBoolean("boolean"))
      Assert.assertEquals(false, bundle.getBoolean(""))
      Assert.assertEquals('$', bundle.getChar("char"))
      Assert.assertEquals(0.toChar(), bundle.getChar(""))
      Assert.assertEquals(42.toByte(), bundle.getByte("byte"))
      Assert.assertEquals(0.toByte(), bundle.getByte(""))
      Assert.assertEquals(73.toShort(), bundle.getShort("short"))
      Assert.assertEquals(0.toShort(), bundle.getShort(""))
      Assert.assertEquals(666, bundle.getInt("int"))
      Assert.assertEquals(0, bundle.getInt(""))
      Assert.assertEquals(69L, bundle.getLong("long"))
      Assert.assertEquals(0L, bundle.getLong(""))
      Assert.assertEquals(1234567879f, bundle.getFloat("float"), 0.1f)
      Assert.assertEquals(0f, bundle.getFloat(""), 0.1f)
      Assert.assertEquals(0.123456789, bundle.getDouble("double"), 0.0000000001)
      Assert.assertEquals(0.0, bundle.getDouble(""), 0.0000000001)
   }
}