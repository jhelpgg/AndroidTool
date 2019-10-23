package jhelp.tests.xml

import android.util.Log
import jhelp.sets.Queue
import jhelp.tests.hasOnlyBlank
import org.xml.sax.Attributes
import org.xml.sax.SAXParseException
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import javax.xml.parsers.SAXParserFactory

class XMLPullParser(file: File) : DefaultHandler()
{
   private val path = file.absolutePath
   private val elements = Queue<XMLElement>()

   init
   {
      SAXParserFactory.newInstance().newSAXParser().parse(file, this)
   }

   override fun startDocument()
   {
      this.elements.inqueue(StartDocument)
   }

   override fun endDocument()
   {
      this.elements.inqueue(EndDocument)
   }

   override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes)
   {
      this.elements.inqueue(StartElement(uri, localName, qName, XMLValues(attributes)))
   }

   override fun endElement(uri: String, localName: String, qName: String)
   {
      this.elements.inqueue(EndElement(uri, localName, qName))
   }

   override fun error(e: SAXParseException)
   {
      Log.e("XMLPullParser", "Issue while parse ${this.path}", e)
   }

   override fun characters(ch: CharArray, start: Int, length: Int)
   {
      if (ch.hasOnlyBlank(start, length))
      {
         return
      }

      val topElement = if (this.elements.empty) this.elements.peek() else null

      if (topElement == null || topElement !is TextElement)
      {
         val textElement = TextElement()
         textElement.append(ch, start, length)
         this.elements.inqueue(textElement)
         return
      }

      topElement.append(ch, start, length)
   }

   override fun warning(e: SAXParseException)
   {
      Log.w("XMLPullParser", "Issue while parse ${this.path}", e)
   }

   override fun fatalError(e: SAXParseException)
   {
      Log.wtf("XMLPullParser", "Issue while parse ${this.path}", e)
   }

   fun next(): XMLElement?
   {
      if (this.elements.empty)
      {
         return null
      }

      return this.elements.outqueue()
   }
}