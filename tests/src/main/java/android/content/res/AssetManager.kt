package android.content.res

import android.os.ParcelFileDescriptor
import jhelp.tests.androidAssetsDirectory
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader

val theAssetManager = AssetManager()

class AssetManager : AutoCloseable
{
   companion object
   {
      @JvmStatic
      val ACCESS_UNKNOWN = 0
      @JvmStatic
      val ACCESS_RANDOM = 1
      @JvmStatic
      val ACCESS_STREAMING = 2
      @JvmStatic
      val ACCESS_BUFFER = 3
   }

   override fun close() = Unit

   private fun getFile(fileName: String) = File(androidAssetsDirectory, fileName)

   fun open(fileName: String): InputStream =
      FileInputStream(this.getFile(fileName))

   fun open(fileName: String, accessMode: Int): InputStream =
      FileInputStream(this.getFile(fileName))

   fun openFd(fileName: String): AssetFileDescriptor
   {
      val parcelFileDescriptor = ParcelFileDescriptor.open(this.getFile(fileName), ACCESS_STREAMING)
      return AssetFileDescriptor(parcelFileDescriptor, 0, AssetFileDescriptor.UNKNOWN_LENGTH)
   }

   fun list(path: String): Array<String>?
   {
      val file = this.getFile(path)

      if (!file.exists() || !file.isDirectory)
      {
         return null
      }

      val children = file.list()
         ?: return null
      return Array(children.size) { "$path/${children[it]}" }
   }

   fun openNonAssetFd(fileName: String) =
      this.openFd(fileName)

   fun openNonAssetFd(cookie: Int, fileName: String) =
      this.openFd(fileName)

   fun openXmlResourceParser(fileName: String): XmlResourceParser
   {
      val factory = XmlPullParserFactory.newInstance()
      val pullParser = factory.newPullParser()
      pullParser.setInput(InputStreamReader(this.open(fileName)))
      return XmlResourceParserForTests(pullParser)
   }

   fun openXmlResourceParser( cookie:Int, fileName:String) =
      this.openXmlResourceParser(fileName)
}