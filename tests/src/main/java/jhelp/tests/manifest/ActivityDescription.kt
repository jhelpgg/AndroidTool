package jhelp.tests.manifest

import jhelp.tests.xml.EndElement
import jhelp.tests.xml.StartElement
import jhelp.tests.xml.XMLPullParser


class ActivityDescription(header: StartElement, xmlPullParser: XMLPullParser)
{
   val name = header.values[ATTRIBUTE_NAME, ""]
   private val intentFilters = ArrayList<IntentFilterDescription>()

   init
   {
      var element = xmlPullParser.next()
      var readNext = true

      while (element != null && readNext)
      {
         when (element)
         {
            is StartElement ->
               when (element.qName)
               {
                  TAG_INTENT_FILTER -> this.intentFilters.add(IntentFilterDescription(xmlPullParser))
               }
            is EndElement   ->
               if (element.qName == TAG_ACTIVITY)
               {
                  readNext = false
               }
         }

         if (readNext)
         {
            element = xmlPullParser.next()
         }
      }

   }

   fun intentFilters() = this.intentFilters.toTypedArray()

   fun isMain() = this.intentFilters.any { it.isMain() }

   fun isLaucher() = this.intentFilters.any { it.isLauncher() }

   override fun toString() =
      """
                  Activity
                  {
                     name=${this.name}
                     filters=${this.intentFilters}
                  }
      """
}