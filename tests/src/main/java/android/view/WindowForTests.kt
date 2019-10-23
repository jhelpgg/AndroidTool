package android.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.InputQueue.Callback
import android.view.SurfaceHolder.Callback2
import android.view.ViewGroup.LayoutParams

class WindowForTests(context:Context) : Window(context)
{
   init
   {
      println(this.javaClass.name)
   }

   override fun superDispatchTrackballEvent(event: MotionEvent?): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setNavigationBarColor(color: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun onConfigurationChanged(newConfig: Configuration?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun peekDecorView(): View
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setFeatureDrawableUri(featureId: Int, uri: Uri?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setVolumeControlStream(streamType: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setBackgroundDrawable(drawable: Drawable?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun takeKeyEvents(get: Boolean)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getNavigationBarColor(): Int
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun superDispatchGenericMotionEvent(event: MotionEvent?): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun superDispatchKeyEvent(event: KeyEvent?): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getLayoutInflater(): LayoutInflater
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun performContextMenuIdentifierAction(id: Int, flags: Int): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setStatusBarColor(color: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun togglePanel(featureId: Int, event: KeyEvent?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun performPanelIdentifierAction(featureId: Int, id: Int, flags: Int): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun closeAllPanels()
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun superDispatchKeyShortcutEvent(event: KeyEvent?): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun superDispatchTouchEvent(event: MotionEvent?): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setDecorCaptionShade(decorCaptionShade: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun takeInputQueue(callback: android.view.InputQueue.Callback?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setResizingCaptionDrawable(drawable: Drawable?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun performPanelShortcut(featureId: Int, keyCode: Int, event: KeyEvent?, flags: Int): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setFeatureDrawable(featureId: Int, drawable: Drawable?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun saveHierarchyState(): Bundle
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun addContentView(view: View?, params: LayoutParams?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun invalidatePanelMenu(featureId: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setTitle(title: CharSequence?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setChildDrawable(featureId: Int, drawable: Drawable?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun closePanel(featureId: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun restoreHierarchyState(savedInstanceState: Bundle?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun onActive()
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getDecorView(): View
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setTitleColor(textColor: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setContentView(layoutResID: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setContentView(view: View?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setContentView(view: View?, params: LayoutParams?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getVolumeControlStream(): Int
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getCurrentFocus(): View?
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun getStatusBarColor(): Int
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun isShortcutKey(keyCode: Int, event: KeyEvent?): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setFeatureDrawableAlpha(featureId: Int, alpha: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun isFloating(): Boolean
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setFeatureDrawableResource(featureId: Int, resId: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setFeatureInt(featureId: Int, value: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun setChildInt(featureId: Int, value: Int)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun takeSurface(callback: Callback2?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun openPanel(featureId: Int, event: KeyEvent?)
   {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }
}