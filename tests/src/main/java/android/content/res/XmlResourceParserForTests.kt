package android.content.res

import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.io.Reader

class XmlResourceParserForTests(private val xmlPullParser: XmlPullParser) : XmlResourceParser
{
   override fun isWhitespace() =
      this.xmlPullParser.isWhitespace

   override fun getAttributePrefix(index: Int) =
      this.xmlPullParser.getAttributePrefix(index)

   override fun getText() =
      this.xmlPullParser.text

   override fun getColumnNumber() =
      this.xmlPullParser.columnNumber

   override fun getNamespaceUri(pos: Int) =
      this.xmlPullParser.getNamespaceUri(pos)

   override fun setProperty(name: String?, value: Any?) =
      this.xmlPullParser.setProperty(name, value)

   override fun getNamespaceCount(depth: Int) =
      this.xmlPullParser.getNamespaceCount(depth)

   override fun getLineNumber() =
      this.xmlPullParser.lineNumber

   override fun require(type: Int, namespace: String?, name: String?) =
      this.xmlPullParser.require(type, namespace, name)

   override fun isEmptyElementTag() =
      this.xmlPullParser.isEmptyElementTag

   override fun nextText() =
      this.xmlPullParser.nextText()

   override fun isAttributeDefault(index: Int) =
      this.xmlPullParser.isAttributeDefault(index)

   override fun getPrefix() =
      this.xmlPullParser.prefix

   override fun getAttributeName(index: Int) =
      this.xmlPullParser.getAttributeName(index)

   override fun defineEntityReplacementText(entityName: String?, replacementText: String?) =
      this.xmlPullParser.defineEntityReplacementText(entityName, replacementText)

   override fun getPositionDescription() =
      this.xmlPullParser.positionDescription

   override fun setInput(`in`: Reader?) =
      this.xmlPullParser.setInput(`in`)

   override fun setInput(inputStream: InputStream?, inputEncoding: String?) =
      this.xmlPullParser.setInput(inputStream, inputEncoding)

   override fun getName() =
      this.xmlPullParser.name

   override fun getFeature(name: String?) =
      this.xmlPullParser.getFeature(name)

   override fun getNamespacePrefix(pos: Int) =
      this.xmlPullParser.getNamespacePrefix(pos)

   override fun getProperty(name: String?) =
      this.xmlPullParser.getProperty(name)

   override fun next() =
      this.xmlPullParser.next()

   override fun getAttributeType(index: Int) =
      this.xmlPullParser.getAttributeType(index)

   override fun getEventType() =
      this.xmlPullParser.eventType

   override fun nextTag() =
      this.xmlPullParser.nextTag()

   override fun getAttributeCount() =
      this.xmlPullParser.attributeCount

   override fun getInputEncoding() =
      this.xmlPullParser.inputEncoding

   override fun getTextCharacters(holderForStartAndLength: IntArray?) =
      this.xmlPullParser.getTextCharacters(holderForStartAndLength)

   override fun nextToken() =
      this.xmlPullParser.nextToken()

   override fun getDepth() =
      this.xmlPullParser.depth

   override fun getAttributeValue(index: Int) =
      this.xmlPullParser.getAttributeValue(index)

   override fun getAttributeValue(namespace: String?, name: String?) =
      this.xmlPullParser.getAttributeValue(namespace, name)

   override fun setFeature(name: String?, state: Boolean) =
      this.xmlPullParser.setFeature(name, state)

   override fun getNamespace(prefix: String?) =
      this.xmlPullParser.getNamespace(prefix)

   override fun getNamespace() =
      this.xmlPullParser.namespace

   override fun getAttributeUnsignedIntValue(namespace: String?, attribute: String?, defaultValue: Int): Int
   {
      val value = this.getAttributeValue(namespace, attribute)
         ?: return defaultValue

      return try
      {
         value.toInt() and 0x7FFFFFFF
      }
      catch (_: Exception)
      {
         defaultValue
      }
   }

   override fun getAttributeUnsignedIntValue(index: Int, defaultValue: Int): Int
   {
      val value = this.getAttributeValue(index)
         ?: return defaultValue

      return try
      {
         value.toInt() and 0x7FFFFFFF
      }
      catch (_: Exception)
      {
         defaultValue
      }
   }

   override fun getAttributeIntValue(namespace: String?, attribute: String?, defaultValue: Int): Int
   {
      val value = this.getAttributeValue(namespace, attribute)
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

   override fun getAttributeIntValue(index: Int, defaultValue: Int): Int
   {
      val value = this.getAttributeValue(index)
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

   override fun getAttributeListValue(
      namespace: String?,
      attribute: String?,
      options: Array<out String>,
      defaultValue: Int
                                     ): Int
   {
      val value = this.getAttributeValue(namespace, attribute)
         ?: return defaultValue
      val index = options.indexOf(value)

      return if (index < 0) defaultValue else index
   }

   override fun getAttributeListValue(index: Int, options: Array<out String>, defaultValue: Int): Int
   {
      val value = this.getAttributeValue(index)
         ?: return defaultValue
      val index = options.indexOf(value)

      return if (index < 0) defaultValue else index
   }

   override fun getIdAttributeResourceValue(defaultValue: Int) =
      this.getAttributeIntValue(null, "id", defaultValue)

   override fun getAttributeFloatValue(namespace: String?, attribute: String?, defaultValue: Float): Float
   {
      val value = this.getAttributeValue(namespace, attribute)
         ?: return defaultValue

      return try
      {
         value.toFloat()
      }
      catch (_: Exception)
      {
         defaultValue
      }
   }

   override fun getAttributeFloatValue(index: Int, defaultValue: Float): Float
   {
      val value = this.getAttributeValue(index)
         ?: return defaultValue

      return try
      {
         value.toFloat()
      }
      catch (_: Exception)
      {
         defaultValue
      }
   }

   override fun getStyleAttribute() =
      this.getAttributeIntValue(null, "style", 0)

   override fun getClassAttribute() =
      this.getAttributeValue(null, "class")

   override fun getAttributeBooleanValue(namespace: String?, attribute: String?, defaultValue: Boolean): Boolean
   {
      val value = this.getAttributeValue(namespace, attribute)
         ?: return defaultValue

      return try
      {
         value.toBoolean()
      }
      catch (_: Exception)
      {
         defaultValue
      }
   }

   override fun getAttributeBooleanValue(index: Int, defaultValue: Boolean): Boolean
   {
      val value = this.getAttributeValue(index)
         ?: return defaultValue

      return try
      {
         value.toBoolean()
      }
      catch (_: Exception)
      {
         defaultValue
      }
   }

   override fun getAttributeResourceValue(namespace: String?, attribute: String?, defaultValue: Int) =
      this.getAttributeIntValue(namespace, attribute, defaultValue)

   override fun getAttributeResourceValue(index: Int, defaultValue: Int) =
      this.getAttributeIntValue(index, defaultValue)

   override fun getAttributeNameResource(index: Int) =
      this.getAttributeIntValue(index, 0)

   override fun getIdAttribute() =
      this.getAttributeValue(null, "id")

   override fun close() = Unit

   override fun getAttributeNamespace(index: Int) =
      this.getAttributePrefix(index)
}