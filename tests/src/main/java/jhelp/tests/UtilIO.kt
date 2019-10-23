package jhelp.tests

import java.io.File
import java.util.Stack

const val ANDROID_MANIFEST = "AndroidManifest.xml"
const val APK_RESOURCES = "res"
const val APK_ASSETS = "assets"

internal lateinit var callerClass: Class<*>

val outsideDirectory by lazy {
   val url = callerClass.getResource("").toString().substring("file:".length)
   val jarIndex = url.indexOf(".jar!")

   if (jarIndex > 0)
   {
      File(url.substring(0, url.lastIndexOf('/', jarIndex)))
   }
   else
   {
      val callerName = callerClass.name
      var file = File(url)
      var index = callerName.indexOf('.')

      while (index >= 0)
      {
         file = file.parentFile!!
         index = callerName.indexOf('.', index + 1)
      }

      file
   }
}

val androidManifestFile by lazy { searchAndroidFile(ANDROID_MANIFEST) }

val androidMainDirectory by lazy { searchAndroidFile("main") }

val androidResourcesDirectory by lazy {
   androidMainDirectory.searchInChildren(APK_RESOURCES)
      ?: File(outsideDirectory, APK_RESOURCES)
}

val androidAssetsDirectory by lazy {
   androidMainDirectory.searchInChildren(APK_ASSETS)
      ?: File(androidMainDirectory, APK_ASSETS)
}

private fun searchAndroidFile(name: String): File
{
   var file = outsideDirectory.searchInChildren(name)

   if (file != null)
   {
      return file
   }

   file = outsideDirectory.searchInParent(name)

   if (file != null)
   {
      return file
   }

   return File(outsideDirectory, name)
}

fun File.searchInChildren(fileName: String): File?
{
   val stack = Stack<File>()
   stack.push(this)

   while (stack.isNotEmpty())
   {
      val file = stack.pop()

      if (file.name == fileName)
      {
         return file
      }

      if (file.isDirectory)
      {
         file.listFiles()?.forEach { stack.push(it) }
      }
   }

   return null
}

private fun File.searchInParent(fileName: String): File?
{
   var file: File? = this

   while (file != null)
   {
      if (file.name == fileName)
      {
         return file
      }

      file = file.parentFile

      if (file != null)
      {
         val child = file.searchInChildren(fileName)

         if (child != null)
         {
            return child
         }
      }
   }

   return null
}

fun File.createDirectory() = if (this.exists()) this.isDirectory else this.mkdirs()

fun File.createFile() =
   when
   {
      this.exists() -> this.isFile
      this.parentFile?.createDirectory()
         ?: false   -> this.createNewFile()
      else          -> false
   }

fun File.erase(): Boolean
{
   if (!this.exists())
   {
      return true
   }

   if (this.isDirectory)
   {
      for (file in this.listFiles()
         ?: emptyArray())
      {
         if (!file.erase())
         {
            return false
         }
      }
   }

   return this.delete()
}
