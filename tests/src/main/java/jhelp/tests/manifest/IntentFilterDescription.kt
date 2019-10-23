package jhelp.tests.manifest

import jhelp.tests.xml.EndElement
import jhelp.tests.xml.StartElement
import jhelp.tests.xml.XMLPullParser

const val VALUE_ACTION_MAIN = "android.intent.action.MAIN"
const val VALUE_CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER"

class IntentFilterDescription(xmlPullParser: XMLPullParser)
{
   var action = ""
      private set
   var category = ""
      private set

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
                  TAG_ACTION   -> this.action = element.values[ATTRIBUTE_NAME, ""]
                  TAG_CATEGORY -> this.category = element.values[ATTRIBUTE_NAME, ""]
               }
            is EndElement   ->
               if (element.qName == TAG_INTENT_FILTER)
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

   override fun toString() = "action=${this.action} category=${this.category}"

   fun isMain() = VALUE_ACTION_MAIN == this.action

   fun isLauncher() = VALUE_CATEGORY_LAUNCHER == this.category
}