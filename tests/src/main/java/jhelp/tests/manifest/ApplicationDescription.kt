package jhelp.tests.manifest

import jhelp.tests.xml.EndElement
import jhelp.tests.xml.StartElement
import jhelp.tests.xml.XMLPullParser

class ApplicationDescription(val name:String="")
{
   private val activities = ArrayList<ActivityDescription>()
   private val services = ArrayList<ServiceDescription>()
   private val receivers = ArrayList<ReceiverDescription>()

   constructor(header: StartElement, xmlPullParser: XMLPullParser):this(header.values[ATTRIBUTE_NAME, ""])
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
                  TAG_ACTIVITY -> this.activities.add(ActivityDescription(element, xmlPullParser))
                  TAG_SERVICE  -> this.services.add(ServiceDescription(element, xmlPullParser))
                  TAG_RECEIVER -> this.receivers.add(ReceiverDescription(element, xmlPullParser))
               }
            is EndElement   ->
               if (element.qName == TAG_APPLICATION)
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

   fun activities() = this.activities.toTypedArray()

   fun services() = this.services.toTypedArray()

   fun receivers() = this.receivers.toTypedArray()

   fun mainLauncherActivity() =
      this.activities.firstOrNull { it.isMain() && it.isLaucher() }

   override fun toString() =
      """
            Application
            {
               name=${this.name}
               activities=${this.activities}
               services=${this.services}
               receivers=${this.receivers} 
            }
      """
}