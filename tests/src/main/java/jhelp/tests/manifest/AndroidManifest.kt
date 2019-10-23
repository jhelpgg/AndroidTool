package jhelp.tests.manifest

import android.app.Application
import jhelp.tests.androidManifestFile
import jhelp.tests.xml.StartElement
import jhelp.tests.xml.XMLPullParser
import java.util.TreeSet

const val TAG_MANIFEST = "manifest"
const val TAG_USES_SDK = "uses-sdk"
const val TAG_USES_PERMISSION = "uses-permission"
const val TAG_APPLICATION = "application"
const val TAG_ACTIVITY = "activity"
const val TAG_SERVICE = "service"
const val TAG_RECEIVER = "receiver"
const val TAG_INTENT_FILTER = "intent-filter"
const val TAG_ACTION = "action"
const val TAG_CATEGORY = "category"

const val ATTRIBUTE_PACKAGE = "package"
const val ATTRIBUTE_VERSION_CODE = "android:versionCode"
const val ATTRIBUTE_VERSION_NAME = "android:versionName"
const val ATTRIBUTE_MIN_SDK_VERSION = "android:minSdkVersion"
const val ATTRIBUTE_TARGET_SDK_VERSION = "android:targetSdkVersion"
const val ATTRIBUTE_NAME = "android:name"

object AndroidManifest
{
   var packageName = ""
      private set
   var versionCode = 0
      private set
   var versionName = ""
      private set
   var minSdkVersion = 0
      private set
   var targetSdkVersion = 0
      private set
   private val permissions = TreeSet<String>()
   lateinit var application: ApplicationDescription
      private set

   internal fun parseAndroidManifest()
   {
      val pullParser = XMLPullParser(androidManifestFile)
      var xmlElement = pullParser.next()
      var createDefaultApplication = true

      while (xmlElement != null)
      {
         when (xmlElement)
         {
            is StartElement ->
               when (xmlElement.qName)
               {
                  TAG_MANIFEST        ->
                  {
                     this.packageName = xmlElement.values[ATTRIBUTE_PACKAGE, ""]
                     this.versionCode = xmlElement.values[ATTRIBUTE_VERSION_CODE, 0]
                     this.versionName = xmlElement.values[ATTRIBUTE_VERSION_NAME, ""]
                  }
                  TAG_USES_SDK        ->
                  {
                     this.minSdkVersion = xmlElement.values[ATTRIBUTE_MIN_SDK_VERSION, 0]
                     this.targetSdkVersion = xmlElement.values[ATTRIBUTE_TARGET_SDK_VERSION, 128]
                  }
                  TAG_USES_PERMISSION ->
                  {
                     val permission = xmlElement.values[ATTRIBUTE_NAME, ""]

                     if (permission.isNotEmpty())
                     {
                        this.permissions.add(permission)
                     }
                  }
                  TAG_APPLICATION     ->
                  {
                     createDefaultApplication = false
                     this.application = ApplicationDescription(xmlElement, pullParser)
                  }
               }
         }

         xmlElement = pullParser.next()
      }

      if(createDefaultApplication)
      {
         this.application = ApplicationDescription()
      }
   }

   fun permissions() = this.permissions.toTypedArray()

   fun mainLauncherActivity() = this.application.mainLauncherActivity()

   override fun toString() =
      """
         AndroidManifest
         {
            minSDK=${this.minSdkVersion}
            target=${this.targetSdkVersion}
            permissions=${this.permissions}
            ${this.application}
         }
      """.trimIndent()
}