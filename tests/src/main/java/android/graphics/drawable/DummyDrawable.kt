package android.graphics.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import kotlin.math.max
import kotlin.math.min

object DummyDrawable : Drawable()
{
   private var alphaValue = 255

   init
   {
      println(this.javaClass.name)
   }

   override fun draw(canvas: Canvas) = Unit

   override fun setAlpha(alpha: Int)
   {
      this.alphaValue = min(255, max(0, alpha))
   }

   override fun getOpacity() = PixelFormat.TRANSLUCENT

   override fun setColorFilter(colorFilter: ColorFilter?)
   {
      // Don't care
   }
}