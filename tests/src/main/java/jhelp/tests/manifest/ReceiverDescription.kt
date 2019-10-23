package jhelp.tests.manifest

import jhelp.tests.xml.StartElement
import jhelp.tests.xml.XMLPullParser

class ReceiverDescription(header: StartElement, xmlPullParser: XMLPullParser)
{
   val name = header.values[ATTRIBUTE_NAME, ""]

   override fun toString() =
      """
                  Receiver
                  {
                     name=${this.name}
                  }
      """
}