package jhelp.tests.xml

import org.xml.sax.Attributes

class XMLValues(attributes: Attributes)
{
   private val values = HashMap<String, String>()

   init
   {
      for (index in 0 until attributes.length)
      {
         val key = attributes.getQName(index)
         val value = attributes.getValue(index)
         this.values[key] = value
      }
   }

   operator fun get(key: String, defaultValue: Int): Int
   {
      val value = this.values[key]
         ?: return defaultValue
      return try
      {
         value.toInt()
      }
      catch (_: Exception)
      {
         defaultValue
      }
   }

   operator fun get(key: String, defaultValue: String) =
      this.values[key]
         ?: defaultValue

   override fun toString() = this.values.toString()
}