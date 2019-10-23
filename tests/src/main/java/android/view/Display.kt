package android.view

import android.graphics.ColorSpace
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.util.DisplayMetrics

class Display
{
   companion object
   {
      @JvmStatic
      val FLAG_SUPPORTS_PROTECTED_BUFFERS = 0b00001
      @JvmStatic
      val FLAG_SECURE =                     0b00010
      @JvmStatic
      val FLAG_PRIVATE =                    0b00100
      @JvmStatic
      val FLAG_PRESENTATION =               0b01000
      @JvmStatic
      val FLAG_ROUND =                      0b10000

      @JvmStatic
      val STATE_UNKNOWN = 0
      @JvmStatic
      val STATE_OFF = 1
      @JvmStatic
      val STATE_ON = 2
      @JvmStatic
      val STATE_DOZE = 3
      @JvmStatic
      val STATE_DOZE_SUSPEND = 4
      @JvmStatic
      val STATE_VR = 5
      @JvmStatic
      val STATE_ON_SUSPEND = 6


      data class Mode(val modeID:Int, val width:Int, val height:Int, val refreshRate:Float)
      {
         fun getPhysicalWidth() = this.width

         fun getPhysicalHeight() =this.height
      }

      data class HdrCapabilities(val desiredMaxLuminance:Float,
                                 val desiredMaxAverageLuminance:Float,
                                 val desiredMinLuminance:Float)
   }

   var width = 1024
   var height = 2048
   var flags = FLAG_SUPPORTS_PROTECTED_BUFFERS or FLAG_SECURE
   var state = Display.STATE_ON

   init
   {
      println(this.javaClass.name)
   }

   fun getDisplayId() = 73

   fun getUniqueId() = "Magic number"

   fun isValid() = true

   fun getName() = "Display for tests"

   fun getSize(outSize:Point)
   {
      outSize.set(this.width, this.height)
   }

   fun getRectSize(outSize:Rect)
   {
      outSize.set(0,0,this.width,this.height)
   }

   fun getCurrentSizeRange(outSmallestSize:Point, outLargestSize:Point)
   {
      outSmallestSize.set(this.width,this.height)
      outLargestSize.set(this.width, this.height)
   }

   fun getRotation() = Surface.ROTATION_0

   fun getOrientation() = this.getRotation()

   fun getCutout() : DisplayCutout? = null

   fun getPixelFormat() = PixelFormat.RGBA_8888

   fun getRefreshRate() = 25f

   fun getSupportedRefreshRates() = floatArrayOf(25f)

   fun getMode() = Mode(73, this.width, this.height, 25f)

   fun getSupportedModes() = arrayOf(Mode(73, 1024,2048,25f))

   fun getHdrCapabilities() : HdrCapabilities? = null

   fun isHdr() = false

   fun isWideColorGamut() = false

   fun getPreferredWideGamutColorSpace(): ColorSpace?= null

   fun getSupportedColorModes() = intArrayOf(PixelFormat.RGBA_8888)

   fun getAppVsyncOffsetNanos() = 0L

   fun getPresentationDeadlineNanos() = 0L

   fun getMetrics(outMetrics: DisplayMetrics)
   {
      outMetrics.density=1f
      outMetrics.densityDpi=1
      outMetrics.heightPixels = this.height
      outMetrics.scaledDensity=1f
      outMetrics.widthPixels = this.width
      outMetrics.xdpi=1f
      outMetrics.ydpi=1f
   }

   fun getRealSize(outSize:Point)=
      this.getSize(outSize)

   fun getRealMetrics(outMetrics: DisplayMetrics) =
      this.getMetrics(outMetrics)

   override fun toString()=
      "Display : ${this.getDisplayId()} : ${this.width}x${this.height} state=${this.state} flags=${this.flags}"
}