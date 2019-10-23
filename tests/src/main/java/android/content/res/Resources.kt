package android.content.res

import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.DummyDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.FractionRes
import androidx.annotation.LayoutRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import jhelp.tests.androidResourcesDirectory
import jhelp.tests.manifest.AccessR
import jhelp.tests.parseColor
import jhelp.tests.xml.StartElement
import jhelp.tests.xml.TextElement
import jhelp.tests.xml.XMLPullParser
import java.io.File

val theResources = Resources()

open class Resources
{
   companion object
   {
      internal val colors = HashMap<Int, Int>()
      internal val texts = HashMap<Int, String>()
      internal val typeFaces = HashMap<Int, Typeface>()
      internal val plurals = HashMap<Int, Plural>()
      internal val textArrays = HashMap<Int, Array<CharSequence>>()
      internal val intArrays = HashMap<Int, IntArray>()
      internal val typedArrays = HashMap<Int, TypedArray>()
      internal val dimensions = HashMap<Int, Float>()
      internal val dimensionPixelOffsets = HashMap<Int, Int>()
      internal val dimensionPixelSizes = HashMap<Int, Int>()
      internal val layouts =  HashMap<Int, File>()

      init
      {
         val valueDirectory = File(androidResourcesDirectory, "values")

         if (valueDirectory.exists())
         {
            val children = valueDirectory.listFiles()

            if (children != null)
            {
               for (child in children)
               {
                  parseXMLresource(child)
               }
            }
         }

         val layoutDirectory  = File(androidResourcesDirectory, "layout")

         if(layoutDirectory.exists())
         {
            val children = layoutDirectory.listFiles()

            if (children != null)
            {
               for (child in children)
               {
                  var name = child.name
                  name = name.substring(0, name.length-4)
                  val reference = AccessR.obtainLayoutReference(name)
                  layouts[reference] = child
               }
            }
         }
      }

      private fun parseXMLresource(resourceFile: File)
      {
         val pullParser = XMLPullParser(resourceFile)
         var element = pullParser.next()
         var currentType = ""
         var currentReference = 0

         while (element != null)
         {
            when (element)
            {
               is StartElement ->
               {
                  currentType = element.qName

                  when (currentType)
                  {
                     "string" -> currentReference = AccessR.obtainStringReference(element.values["name", ""])
                     "color"  -> currentReference = AccessR.obtainColorReference(element.values["name", ""])
                  }
               }
               is TextElement  ->
               {
                  when (currentType)
                  {
                     "string" -> texts[currentReference] = element.text()
                     "color"  -> colors[currentReference] = element.text().parseColor()
                  }

                  currentType = ""
               }
            }

            element = pullParser.next()
         }
      }

      private val systemResouces = Resources()

      @JvmStatic
      fun getSystem() = systemResouces

      class NotFoundException(name: String, cause: Exception?) : RuntimeException(name, cause)
      {
         constructor() : this("", null)

         constructor(name: String) : this(name, null)
      }
   }

   //TODO implements Theme
   class Theme
   {
      fun obtainStyledAttributes(attrs: IntArray) = TypedArray(theResources)

      fun obtainStyledAttributes(res: Int, attrs: IntArray) = TypedArray(theResources)

      fun obtainStyledAttributes(set: AttributeSet?, attrs: IntArray, i1: Int, i2: Int) = TypedArray(theResources)
   }

   @Throws(NotFoundException::class)
   fun getText(@StringRes id: Int): CharSequence =
      texts[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getFont(@FontRes id: Int): Typeface =
      typeFaces[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getQuantityText(@PluralsRes id: Int, quantity: Int): CharSequence =
      plurals[id]?.computeText(quantity)
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getString(@StringRes id: Int) =
      this.getText(id).toString()

   @Throws(NotFoundException::class)
   fun getString(@StringRes id: Int, vararg formatArgs: Any) =
      this.getString(id).format(*formatArgs)

   @Throws(NotFoundException::class)
   fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any) =
      this.getQuantityString(id, quantity).format(*formatArgs)

   @Throws(NotFoundException::class)
   fun getQuantityString(@PluralsRes id: Int, quantity: Int) =
      this.getQuantityText(id, quantity).toString()

   fun getText(@StringRes id: Int, def: CharSequence): CharSequence =
      try
      {
         this.getText(id)
      }
      catch (_: Exception)
      {
         def
      }

   @Throws(NotFoundException::class)
   fun getTextArray(@ArrayRes id: Int): Array<CharSequence> =
      textArrays[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getStringArray(@ArrayRes id: Int): Array<String>
   {
      val array = this.getTextArray(id)
      return Array<String>(array.size) { array[it].toString() }
   }

   @Throws(NotFoundException::class)
   fun getIntArray(@ArrayRes id: Int): IntArray =
      intArrays[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun obtainTypedArray(@ArrayRes id: Int): TypedArray =
      typedArrays[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getDimension(@DimenRes id: Int): Float =
      dimensions[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getDimensionPixelOffset(@DimenRes id: Int): Int =
      dimensionPixelOffsets[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getDimensionPixelSize(@DimenRes id: Int): Int =
      dimensionPixelSizes[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getLayoutFile(@LayoutRes id: Int): File =
      layouts[id]
         ?: throw NotFoundException("$id not found")

   fun getFraction(@FractionRes id: Int, base: Int, pbase: Int) = 0f

   @Throws(NotFoundException::class)
   open fun getDrawable(@DrawableRes id: Int): Drawable = DummyDrawable

   @Throws(NotFoundException::class)
   open fun getDrawable(@DrawableRes id: Int, theme: Theme?): Drawable = DummyDrawable

   @Throws(NotFoundException::class)
   open fun getDrawableForDensity(@DrawableRes id: Int, density: Int): Drawable = DummyDrawable

   open fun getDrawableForDensity(@DrawableRes id: Int, density: Int, theme: Theme?): Drawable? = DummyDrawable

   @Throws(NotFoundException::class)
   fun getColor(@ColorRes id: Int): Int =
      colors[id]
         ?: throw NotFoundException("$id not found")

   @Throws(NotFoundException::class)
   fun getColor(@ColorRes id: Int, theme: Theme?) =
      this.getColor(id)

   fun getConfiguration(): Configuration
   {
      val configuration = Configuration()
      if (VERSION.SDK_INT >= VERSION_CODES.O)
      {
         configuration.colorMode = PixelFormat.RGBA_8888
      }
      configuration.densityDpi = 1
      configuration.fontScale = 1f
      // ...
      return configuration
   }

   fun getColorStateList(id: Int, theme: Theme) = ColorStateList.valueOf(0xFFFFFFFF.toInt())


   // TODO other methods
}

fun Resources.layoutFile(layoutID:Int) : File = this.getLayoutFile(layoutID)