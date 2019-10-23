package jhelp.tests.xml

import org.xml.sax.Attributes

sealed class XMLElement

object StartDocument : XMLElement()
{
   override fun toString() = "StartDocument"
}

object EndDocument : XMLElement()
{
   override fun toString() = "EndDocument"
}

data class StartElement(val uri: String, val localName: String, val qName: String, val values: XMLValues) :
   XMLElement()

data class EndElement(val uri: String, val localName: String, val qName: String) : XMLElement()

class TextElement(private val stringBuilder: StringBuilder = StringBuilder()) : XMLElement()
{
   internal fun append(ch: CharArray, start: Int, length: Int)
   {
      this.stringBuilder.append(ch, start, length)
   }

   fun text() = this.stringBuilder.toString().trim()

   override fun toString(): String
   {
      return "Text:${this.stringBuilder}"
   }
}