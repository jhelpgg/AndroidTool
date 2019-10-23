package android.app

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.assist.AssistContent
import android.content.BroadcastReceiver
import android.content.ComponentCallbacks2
import android.content.ComponentName
import android.content.Context
import android.content.ContextForTests
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.ServiceConnection
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.layoutFile
import android.content.res.theResources
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.session.MediaController
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.os.IBinder
import android.os.PersistableBundle
import android.os.UserHandle
import android.transition.Scene
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.ActionMode
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Display
import android.view.DragAndDropPermissions
import android.view.DragEvent
import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.LayoutInflater
import android.view.LayoutInflaterForTests
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.SearchEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowForTests
import android.view.WindowManager
import android.view.WindowManagerForTests
import android.view.accessibility.AccessibilityEvent
import androidx.fragment.app.Fragment
import jhelp.tests.atLeastNougat_24
import jhelp.tests.manifest.AccessR
import jhelp.tests.theApplication
import jhelp.tests.xml.StartElement
import jhelp.tests.xml.XMLPullParser
import java.io.FileDescriptor
import java.io.InputStream
import java.io.PrintWriter
import java.util.Collections
import java.util.function.Consumer

open class Activity(private val context: Context) : Context(), ComponentCallbacks2
{
   private lateinit var intent: Intent
   internal var parent: Activity? = null
   private val window = WindowForTests(this)
   private val loaderManager = LoaderManagerForTests(this)
   private val layoutInflater = LayoutInflaterForTests(this)
   private val menuInflater = MenuInflater(this)
   private var title: CharSequence = "Actvity for test"
   private var titleColor = 0xFF000000.toInt()
   private var volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
   private var mediaController: MediaController? = null
   private val views = HashMap<Int, View>()

   init
   {
      println(this.javaClass.name)
   }

   constructor() : this(ContextForTests())

   fun getIntent() = this.intent

   fun setIntent(newIntent: Intent)
   {
      this.intent = newIntent
   }

   fun getApplication() = theApplication

   fun isChild() = this.parent != null

   fun getParent() = this.parent

   fun getWindowManager() = WindowManagerForTests

   fun getWindow(): Window = this.window

   fun getLoaderManager(): LoaderManager = this.loaderManager

   fun getCurrentFocus(): View? = null

   fun registerActivityLifecycleCallbacks(activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks)
   {
      theApplication.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
   }

   fun unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks)
   {
      theApplication.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
   }

   open fun onCreate(savedInstanceState: Bundle?) = Unit

   open fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?)
   {
      this.onCreate(savedInstanceState)
   }

   // TODO
   fun performRestoreInstanceState(savedInstanceState: Bundle) = Unit

   // TODO
   fun performRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) = Unit

   open fun onRestoreInstanceState(savedInstanceState: Bundle) = Unit

   open fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) = Unit

   open fun onPostCreate(savedInstanceState: Bundle?) = Unit

   open fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) = Unit

   open fun onStart() = Unit

   open fun onRestart() = Unit

   open fun onStateNotSaved() = Unit

   open fun onResume() = Unit

   open fun onPostResume() = Unit

   open fun onTopResumedActivityChanged(isTopResumedActivity: Boolean, reson: String) = Unit

   fun isVoiceInteraction() = false

   fun isVoiceInteractionRoot() = false

   fun getVoiceInteractor(): VoiceInteractor? = null

   fun isLocalVoiceInteractionSupported() = false

   fun startLocalVoiceInteraction(privateOptions: Bundle) = Unit

   open fun onLocalVoiceInteractionStarted() = Unit

   open fun onLocalVoiceInteractionStopped() = Unit

   fun stopLocalVoiceInteraction() = Unit

   open fun onNewIntent(intent: Intent) = Unit

   open fun onSaveInstanceState(outState: Bundle) = Unit

   open fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) = Unit

   open fun onPause() = Unit

   open fun onUserLeaveHint() = Unit

   open fun onCreateThumbnail(outBitmap: Bitmap, canvas: Canvas) = false

   open fun onCreateDescription(): CharSequence? = null

   open fun onProvideAssistData(data: Bundle) = Unit

   open fun onProvideAssistContent(outContent: AssistContent) = Unit

   @SuppressLint("NewApi")
   open fun onGetDirectActions(cancellationSignal: CancellationSignal, callback: Consumer<List<DirectAction>>)
   {
      if (atLeastNougat_24)
      {
         callback.accept(Collections.emptyList())
      }
   }

   open fun onPerformDirectAction(
      actionId: String,
      arguments: Bundle,
      cancellationSignal: CancellationSignal,
      resultListener: Consumer<Bundle>
                                 ) = Unit

   fun requestShowKeyboardShortcuts() = Unit

   fun dismissKeyboardShortcutsHelper() = Unit

   open fun onProvideKeyboardShortcuts(data: List<KeyboardShortcutGroup>, menu: Menu, deviceId: Int) = Unit

   fun showAssist(arguments: Bundle) = false

   open fun onStop() = Unit

   open fun onDestroy() = Unit

   fun reportFullyDrawn() = Unit

   open fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration) =
      this.onMultiWindowModeChanged(isInMultiWindowMode)

   open fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) = Unit

   fun isInMultiWindowMode() = false

   open fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) =
      this.onPictureInPictureModeChanged(isInPictureInPictureMode)

   open fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) = Unit

   fun isInPictureInPictureMode() = false

   fun enterPictureInPictureMode() = Unit

   fun enterPictureInPictureMode(args: PictureInPictureParams) = Unit

   fun setPictureInPictureParams(args: PictureInPictureParams) = Unit

   fun getMaxNumPictureInPictureActions() = 4

   override fun onConfigurationChanged(newConfig: Configuration) = Unit

   fun getChangingConfigurations() = 0

   // TODO
   fun getLastNonConfigurationInstance(): Any? = null

   open fun onRetainNonConfigurationInstance(): Any? = null

   override fun onLowMemory() = Unit

   override fun onTrimMemory(level: Int) = Unit

   fun getFragmentManager(): FragmentManager = FragmentManagerForTests

   open fun onAttachFragment(fragment: Fragment) = Unit

   fun managedQuery(uri: Uri, projection: Array<String>?, selection: String?, sortOrder: String?) =
      this.getContentResolver().query(uri, projection, selection, null, sortOrder)

   fun managedQuery(
      uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?,
      sortOrder: String?
                   ) =
      this.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder)

   fun startManagingCursor(cursor: Cursor) = Unit

   fun stopManagingCursor(cursor: Cursor) = Unit

   open fun <V : View> findViewById(id: Int): V? = this.views[id] as? V

   fun <V : View> requireViewById(id: Int) =
      this.findViewById<V>(id)
         ?: throw IllegalArgumentException("Id $id not corresponds to a view")

   fun getActionBar(): ActionBar? = null

   fun setActionBar(actionBar: ActionBar?) = Unit

   // TODO Get the XML from resources (Must implements the method first), parse it to extract view and  id, with this
   //  info, implements findViewById
   open fun setContentView(layoutResID: Int)
   {
      this.views.clear()
      val layoutFile = theResources.layoutFile(layoutResID)
      val pullParser = XMLPullParser(layoutFile)
      var element = pullParser.next()

      while (element != null)
      {
         when (element)
         {
            is StartElement ->
            {
               val name = element.qName
               val completeName =
                  if (name.contains('.'))
                  {
                     name
                  }
                  else
                  {
                     "android.widget.$name"
                  }

               val idName = element.values["android:id", ""]

               if (idName.isNotEmpty())
               {
                  val indexSlash = idName.indexOf('/')
                  val id = AccessR.obtainIdReference(idName.substring(indexSlash + 1))

                  val view =
                     Class.forName(completeName).getDeclaredConstructor(Context::class.java).newInstance(this) as View
                  view.setId(id)
                  this.views[id] = view
               }
            }
         }

         element = pullParser.next()
      }
   }

   open fun setContentView(view: View)
   {
      this.views.clear()
      this.addContentView(view, null)
   }

   open fun setContentView(view: View, params: ViewGroup.LayoutParams?) = this.setContentView(view)

   // TODO
   open fun addContentView(view: View, params: ViewGroup.LayoutParams?)
   {
      TODO()
   }

   fun getContentTransitionManager(): TransitionManager? = null

   fun setContentTransitionManager(transitionManager: TransitionManager) = Unit

   fun getContentScene(): Scene? = null

   fun setFinishOnTouchOutside(finish: Boolean) = Unit

   fun setDefaultKeyMode(mode: Int) = Unit

   open fun onKeyDown(keyCode: Int, event: KeyEvent) = false

   open fun onKeyLongPress(keyCode: Int, event: KeyEvent) = false

   open fun onKeyUp(keyCode: Int, event: KeyEvent) = false

   open fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent) = false

   open fun onBackPressed() = Unit

   open fun onKeyShortcut(keyCode: Int, event: KeyEvent) = false

   open fun onTouchEvent(event: MotionEvent) = false

   open fun onTrackballEvent(event: MotionEvent) = false

   open fun onGenericMotionEvent(event: MotionEvent) = false

   open fun onUserInteraction() = Unit

   open fun onWindowAttributesChanged(params: WindowManager.LayoutParams) = Unit

   open fun onContentChanged() = Unit

   open fun onWindowFocusChanged(hasFocus: Boolean) = Unit

   open fun onAttachedToWindow() = Unit

   open fun onDetachedFromWindow() = Unit

   // TODO
   open fun hasWindowFocus() = true

   open fun onWindowDismissed(finishTask: Boolean, suppressWindowTransition: Boolean) = Unit

   fun toggleFreeformWindowingMode() = Unit

   fun enterPictureInPictureModeIfPossible() = Unit

   fun dispatchKeyEvent(event: KeyEvent) = Unit

   fun dispatchKeyShortcutEvent(event: KeyEvent) = Unit

   fun dispatchTouchEvent(event: MotionEvent) = Unit

   fun dispatchTrackballEvent(event: MotionEvent) = Unit

   fun dispatchGenericMotionEvent(event: MotionEvent) = Unit

   fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent) = Unit

   open fun onCreatePanelView(featureId: Int): View? = null

   open fun onCreatePanelMenu(featureId: Int, menu: Menu) = false

   open fun onPreparePanel(featureId: Int, view: View?, menu: Menu) = false

   open fun onMenuOpened(featureId: Int, menu: Menu) = true

   open fun onMenuItemSelected(featureId: Int, item: MenuItem) = false

   open fun onPanelClosed(featureId: Int, menu: Menu) = Unit

   open fun invalidateOptionsMenu() = Unit

   open fun onCreateOptionsMenu(menu: Menu) = true

   open fun onPrepareOptionsMenu(menu: Menu) = true

   open fun onOptionsItemSelected(item: MenuItem) = false

   open fun onNavigateUp() = false

   open fun onNavigateUpFromChild(child: Activity) =
      this.onNavigateUp()

   open fun onCreateNavigateUpTaskStack(builder: TaskStackBuilder) = Unit

   open fun onPrepareNavigateUpTaskStack(builder: TaskStackBuilder) = Unit

   open fun onOptionsMenuClosed(menu: Menu) = Unit

   open fun openOptionsMenu() = Unit

   open fun closeOptionsMenu() = Unit

   open fun onCreateContextMenu(menu: ContextMenu, view: View, menuInfo: ContextMenuInfo) = Unit

   fun registerForContextMenu(view: View) = Unit

   fun unregisterForContextMenu(view: View) = Unit

   fun openContextMenu(view: View) = Unit

   fun closeContextMenu() = Unit

   open fun onContextItemSelected(menu: Menu) = false

   open fun onContextMenuClosed(menu: Menu) = Unit

   open fun onCreateDialog(id: Int): Dialog? = null

   open fun onCreateDialog(id: Int, bundle: Bundle): Dialog? = null

   open fun onPrepareDialog(id: Int, dialog: Dialog) = Unit

   fun showDialog(id: Int) = Unit

   fun showDialog(id: Int, bundle: Bundle) = Unit

   fun dismissDialog(id: Int) = Unit

   fun removeDialog(id: Int) = Unit

   open fun onSearchRequested(searchEvent: SearchEvent?) = Unit

   open fun onSearchRequested() = Unit

   fun getSearchEvent(): SearchEvent? = null

   fun startSearch(
      initialQuery: String?, selectInitialQuery: Boolean,
      appSearchData: Bundle?, globalSearch: Boolean
                  ) = Unit

   fun triggerSearch(query: String, appSearchData: Bundle?) = Unit

   fun takeKeyEvents(get: Boolean) = Unit

   fun requestWindowFeature(featureId: Int) =
      this.window.requestFeature(featureId)

   fun setFeatureDrawableResource(featureId: Int, resId: Int) =
      this.window.setFeatureDrawableResource(featureId, resId)

   fun setFeatureDrawableUri(featureId: Int, uri: Uri) = Unit

   fun setFeatureDrawable(featureId: Int, drawable: Drawable) = Unit

   fun setFeatureDrawableAlpha(featureId: Int, alpha: Int) = Unit

   fun getLayoutInflater(): LayoutInflater = this.layoutInflater

   open fun getMenuInflater() = this.menuInflater

   override fun setTheme(theme: Int) = this.context.setTheme(theme)

   open fun onApplyThemeResource(theme: Resources.Theme, resid: Int) = Unit

   // TODO Permissions
   fun requestPermissions(permissions: Array<String>, requestCode: Int) = Unit

   // TODO Permissions
   open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) = Unit

   // TODO Permissions
   fun shouldShowRequestPermissionRationale(permission: String) = false

   open fun startActivityForResult(intent: Intent, requestCode: Int) =
      this.startActivityForResult(intent, requestCode, null)

   //TODO launch activity
   open fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) = Unit


   fun isActivityTransitionRunning() = false

   open fun startActivityForResultAsUser(intent: Intent, requestCode: Int, user: UserHandle) =
      this.startActivityForResultAsUser(intent, requestCode, null, user)

   open fun startActivityForResultAsUser(intent: Intent, requestCode: Int, options: Bundle?, user: UserHandle) =
      this.startActivityForResultAsUser(intent, "Test", requestCode, options, user)

   //TODO launch activity
   open fun startActivityForResultAsUser(
      intent: Intent,
      requestWho: String, requestCode: Int,
      options: Bundle?, user: UserHandle
                                        ) = Unit

   open fun startActivityAsUser(intent: Intent, user: UserHandle) =
      this.startActivityAsUser(intent, null, user)

   //TODO launch activity
   open fun startActivityAsUser(intent: Intent, options: Bundle?, user: UserHandle) = Unit

   //TODO launch activity
   open fun startActivityAsCaller(
      intent: Intent, options: Bundle?,
      permissionToken: IBinder, ignoreTargetSecurity: Boolean, userId: Int
                                 ) = Unit

   //TODO launch activity
   open fun startIntentSenderForResult(
      intent: IntentSender, requestCode: Int,
      fillInIntent: Intent, flagsMask: Int, flagsValues: Int, extraFlags: Int
                                      ) = Unit

   //TODO launch activity
   open fun startIntentSenderForResult(
      intent: IntentSender, requestCode: Int,
      fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int,
      options: Bundle?
                                      ) = Unit

   override fun startActivity(intent: Intent) =
      this.context.startActivity(intent)

   //TODO launch activity
   override fun startActivity(intent: Intent, options: Bundle?) =
      this.context.startActivity(intent, options)

   override fun startActivities(intents: Array<Intent>) =
      this.context.startActivities(intents)

   override fun startActivities(intents: Array<Intent>, options: Bundle?) =
      this.context.startActivities(intents, options)

   override fun startIntentSender(
      intent: IntentSender,
      fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int
                                 ) =
      this.context.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags)


   //TODO launch activity
   override fun startIntentSender(
      intent: IntentSender,
      fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int,
      options: Bundle?
                                 ) =
      this.context.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options)

   open fun startActivityIfNeeded(intent: Intent, requestCode: Boolean) =
      this.startActivityIfNeeded(intent, requestCode, null)

   //TODO launch activity
   open fun startActivityIfNeeded(intent: Intent, requestCode: Boolean, options: Bundle?) = false

   fun startNextMatchingActivity(intent: Intent) =
      this.startNextMatchingActivity(intent, null)

   //TODO launch activity
   fun startNextMatchingActivity(intent: Intent, options: Bundle?) = false

   open fun startActivityFromChild(child: Activity, intent: Intent, requestCode: Int) =
      this.startActivityFromChild(child, intent, requestCode, null)

   //TODO launch activity
   open fun startActivityFromChild(child: Activity, intent: Intent, requestCode: Int, options: Bundle?) = Unit

   open fun startActivityFromFragment(fragment: Fragment, intent: Intent, requestCode: Int) =
      this.startActivityFromFragment(fragment, intent, requestCode, null)

   //TODO launch activity
   open fun startActivityFromFragment(fragment: Fragment, intent: Intent, requestCode: Int, options: Bundle?) = Unit

   fun startIntentSenderFromChild(
      child: Activity, intent: IntentSender,
      requestCode: Int, fillInIntent: Intent,
      flagsMask: Int, flagsValues: Int,
      extraFlags: Int
                                 ) =
      this.startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, null)

   //TODO launch activity
   fun startIntentSenderFromChild(
      child: Activity, intent: IntentSender,
      requestCode: Int, fillInIntent: Intent,
      flagsMask: Int, flagsValues: Int,
      extraFlags: Int, options: Bundle?
                                 ) = Unit

   fun overridePendingTransition(enterAnim: Int, exitAnim: Int) = Unit

   // TODO
   fun setResult(resultCode: Int) = Unit

   // TODO
   fun setResult(resultCode: Int, data: Intent) = Unit

   fun getReferrer(): Uri? = null

   fun onProvideReferrer(): Uri? = null

   fun getCallingPackage(): String? = null

   fun getCallingActivity(): ComponentName? = null

   fun setVisible(visible: Boolean) = Unit

   // TODO Activity life cycle
   fun isFinishing() = false

   // TODO Activity life cycle
   fun isDestroyed() = false

   fun isChangingConfigurations() = false

   // TODO Activity life cycle
   fun recreate() = Unit

   // TODO Activity life cycle
   fun finish() = Unit

   fun finishAffinity() = Unit

   fun finishFromChild(child: Activity) = this.finish()

   fun finishAfterTransition() = this.finish()

   // TODO Activity life cycle
   fun finishActivity(requestCode: Int) = Unit

   fun finishActivityFromChild(child: Activity, requestCode: Int) = this.finishActivity(requestCode)

   fun finishAndRemoveTask() = this.finish()

   // TODO Activity life cycle
   fun releaseInstance(): Boolean = false

   open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) = Unit

   open fun onActivityReenter(resultCode: Int, data: Intent) = Unit

   //TODO
   fun createPendingResult(requestCode: Int, data: Intent, flags: Int): PendingIntent? = null

   fun setRequestedOrientation(requestedOrientation: Int) = Unit

   fun getRequestedOrientation() = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

   fun getTaskId() = 42

   fun isTaskRoot() = true

   fun moveTaskToBack(nonRoot: Boolean) = false

   fun getLocalClassName() = this.javaClass.simpleName

   fun getComponentName() = ComponentName(this.javaClass.`package`!!.name, this.javaClass.simpleName)

   fun getPreferences(mode: Int) =
      this.context.getSharedPreferences(this.getLocalClassName(), mode)

   override fun getSystemService(name: String) =
      when (name)
      {
         Context.WINDOW_SERVICE -> WindowManagerForTests
         Context.SEARCH_SERVICE -> null
         else                   -> this.context.getSystemService(name)
      }

   fun setTitle(title: CharSequence)
   {
      if (this.title != title)
      {
         this.title = title
         this.onTitleChanged(this.title, this.titleColor)
         this.parent?.onChildTitleChanged(this, this.title)
      }
   }

   fun setTitle(titleId: Int) = Unit

   fun setTitleColor(textColor: Int)
   {
      if (this.titleColor != textColor)
      {
         this.titleColor = textColor
         this.onTitleChanged(this.title, this.titleColor)
      }
   }

   fun getTitle(): CharSequence = this.title

   fun getTitleColor(): Int = this.titleColor

   open fun onTitleChanged(title: CharSequence, color: Int) = Unit

   open fun onChildTitleChanged(childActivity: Activity, title: CharSequence) = Unit

   fun setTaskDescription(taskDescription: ActivityManager.TaskDescription) = Unit

   fun setProgressBarVisibility(visible: Boolean) = Unit

   fun setProgressBarIndeterminateVisibility(visible: Boolean) = Unit

   fun setProgressBarIndeterminate(indeterminate: Boolean) = Unit

   fun setProgress(progress: Int) = Unit

   fun setSecondaryProgress(secondaryProgress: Int) = Unit

   fun setVolumeControlStream(streamType: Int)
   {
      this.volumeControlStream = streamType
   }

   fun getVolumeControlStream() = this.volumeControlStream

   fun setMediaController(controller: MediaController)
   {
      this.mediaController = controller
   }

   fun getMediaController() = this.mediaController

   fun runOnUiThread(action: Runnable) = action.run()

   open fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? = null

   open fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? = null

   open fun dump(prefix: String, fd: FileDescriptor?, writer: PrintWriter, args: Array<String>?) = Unit

   fun isImmersive() = false

   fun requestVisibleBehind(visible: Boolean) = false

   open fun onVisibleBehindCanceled() = Unit

   fun isBackgroundVisibleBehind() = false

   open fun onBackgroundVisibleBehindChanged(visible: Boolean) = Unit

   open fun onEnterAnimationComplete() = Unit

   fun setImmersive(i: Boolean) = Unit

   fun setVrModeEnabled(enabled: Boolean, requestedComponent: ComponentName) = Unit

   fun startActionMode(callback: ActionMode.Callback): ActionMode? = null

   fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode? = null

   open fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? = null

   open fun onWindowStartingActionMode(callback: ActionMode.Callback, type: Int): ActionMode? = null

   open fun onActionModeStarted(mode: ActionMode) = Unit

   open fun onActionModeFinished(mode: ActionMode) = Unit

   fun shouldUpRecreateTask(targetIntent: Intent) = false

   fun navigateUpTo(upIntent: Intent) = false

   fun navigateUpToFromChild(child: Activity, upIntent: Intent) =
      this.navigateUpTo(upIntent)

   fun getParentActivityIntent() = this.parent?.intent

   fun setEnterSharedElementCallback(callback: SharedElementCallback) = Unit

   fun setExitSharedElementCallback(callback: SharedElementCallback) = Unit

   fun postponeEnterTransition() = Unit

   fun startPostponedEnterTransition() = Unit

   fun requestDragAndDropPermissions(event: DragEvent): DragAndDropPermissions? = null

   fun startLockTask() = Unit

   fun stopLockTask() = Unit

   fun showLockTaskEscapeMessage() = Unit

   fun setShowWhenLocked(showWhenLocked: Boolean) = Unit

   fun setInheritShowWhenLocked(inheritShowWhenLocked: Boolean) = Unit

   fun setTurnScreenOn(turnScreenOn: Boolean) = Unit

   override fun getApplicationContext() = theApplication

   override fun setWallpaper(bitmap: Bitmap) =
      this.context.setWallpaper(bitmap)

   override fun setWallpaper(data: InputStream) =
      this.context.setWallpaper(data)

   @SuppressLint("MissingPermission")
   override fun removeStickyBroadcastAsUser(intent: Intent, user: UserHandle) =
      this.context.removeStickyBroadcastAsUser(intent, user)

   override fun checkCallingOrSelfPermission(permission: String) =
      this.context.checkCallingOrSelfPermission(permission)

   override fun getClassLoader() = this.javaClass.classLoader!!

   override fun checkCallingOrSelfUriPermission(uri: Uri, modeFlags: Int) =
      this.context.checkCallingOrSelfUriPermission(uri, modeFlags)

   override fun getObbDir() =
      this.context.getObbDir()

   override fun checkUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int) =
      this.context.checkUriPermission(uri, pid, uid, modeFlags)

   override fun checkUriPermission(
      uri: Uri?,
      readPermission: String?,
      writePermission: String?,
      pid: Int,
      uid: Int,
      modeFlags: Int
                                  ) =
      this.context.checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags)

   override fun getExternalFilesDirs(type: String) =
      this.context.getExternalFilesDirs(type)

   override fun getPackageResourcePath() =
      this.context.getPackageResourcePath()

   @TargetApi(VERSION_CODES.N)
   override fun deleteSharedPreferences(name: String) =
      this.context.deleteSharedPreferences(name)

   override fun checkPermission(permission: String, pid: Int, uid: Int) =
      this.context.checkPermission(permission, pid, uid)

   override fun getSharedPreferences(name: String, mode: Int) =
      this.context.getSharedPreferences(name, mode)

   @SuppressLint("MissingPermission")
   override fun sendStickyBroadcastAsUser(intent: Intent, user: UserHandle) =
      this.context.sendStickyBroadcastAsUser(intent, user)

   @TargetApi(VERSION_CODES.N)
   override fun getDataDir() =
      this.context.getDataDir()

   override fun getWallpaper() =
      this.context.getWallpaper()

   @TargetApi(VERSION_CODES.N)
   fun isDeviceStorage() =
      this.context.isDeviceProtectedStorage()

   override fun getExternalFilesDir(type: String?) =
      this.context.getExternalFilesDir(type)

   @SuppressLint("MissingPermission")
   override fun sendBroadcastAsUser(intent: Intent, user: UserHandle) =
      this.context.sendBroadcastAsUser(intent, user)

   @SuppressLint("MissingPermission")
   override fun sendBroadcastAsUser(intent: Intent, user: UserHandle, receiverPermission: String?) =
      this.context.sendBroadcastAsUser(intent, user, receiverPermission)

   override fun getExternalCacheDir() =
      this.context.getExternalCacheDir()

   override fun getDatabasePath(name: String) =
      this.context.getDatabasePath(name)

   override fun getFileStreamPath(name: String) =
      this.context.getFileStreamPath(name)

   override fun stopService(service: Intent) =
      this.context.stopService(service)

   @TargetApi(VERSION_CODES.M)
   override fun checkSelfPermission(permission: String) =
      this.context.checkSelfPermission(permission)

   override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter) =
      this.context.registerReceiver(receiver, filter)

   @TargetApi(VERSION_CODES.O)
   override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter, flags: Int) =
      this.context.registerReceiver(receiver, filter, flags)

   override fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter,
      broadcastPermission: String?,
      scheduler: Handler?
                                ) =
      this.context.registerReceiver(receiver, filter, broadcastPermission, scheduler)

   @TargetApi(VERSION_CODES.O)
   override fun registerReceiver(
      receiver: BroadcastReceiver?,
      filter: IntentFilter,
      broadcastPermission: String?,
      scheduler: Handler?,
      flags: Int
                                ) =
      this.context.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags)

   @TargetApi(VERSION_CODES.M)
   override fun getSystemServiceName(serviceClass: Class<*>) =
      this.context.getSystemServiceName(serviceClass)

   override fun getMainLooper() =
      this.context.getMainLooper()

   override fun enforceCallingOrSelfPermission(permission: String, message: String?) =
      this.context.enforceCallingOrSelfPermission(permission, message)

   override fun getPackageCodePath() =
      this.context.getPackageCodePath()

   override fun checkCallingUriPermission(uri: Uri, modeFlags: Int) =
      this.context.checkCallingUriPermission(uri, modeFlags)

   override fun getWallpaperDesiredMinimumWidth() =
      this.context.getWallpaperDesiredMinimumWidth()

   @TargetApi(VERSION_CODES.N)
   fun createDeviceStorageContext() =
      this.context.createDeviceProtectedStorageContext()

   override fun openFileInput(name: String) =
      this.context.openFileInput(name)

   @TargetApi(VERSION_CODES.LOLLIPOP)
   override fun getCodeCacheDir() =
      this.context.getCodeCacheDir()

   override fun bindService(service: Intent, conn: ServiceConnection, flags: Int) =
      this.context.bindService(service, conn, flags)

   override fun deleteDatabase(name: String) =
      this.context.deleteDatabase(name)

   override fun getAssets() =
      this.context.getAssets()

   @TargetApi(VERSION_CODES.LOLLIPOP)
   override fun getNoBackupFilesDir() =
      this.context.getNoBackupFilesDir()

   override fun getResources() =
      this.context.getResources()

   override fun fileList() =
      this.context.fileList()

   override fun unregisterReceiver(receiver: BroadcastReceiver) =
      this.context.unregisterReceiver(receiver)

   override fun enforcePermission(permission: String, pid: Int, uid: Int, message: String?) =
      this.context.enforcePermission(permission, pid, uid, message)

   override fun openFileOutput(name: String, mode: Int) =
      this.context.openFileOutput(name, mode)

   @SuppressLint("MissingPermission")
   override fun sendStickyOrderedBroadcast(
      intent: Intent,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                          ) =
      this.context.sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData, initialExtras)

   override fun createConfigurationContext(overrideConfiguration: Configuration) =
      this.context.createConfigurationContext(overrideConfiguration)

   override fun getFilesDir() =
      this.context.getFilesDir()

   override fun sendBroadcast(intent: Intent) =
      this.context.sendBroadcast(intent)

   override fun sendBroadcast(intent: Intent, receiverPermission: String?) =
      this.context.sendBroadcast(intent, receiverPermission)

   @SuppressLint("MissingPermission")
   override fun sendOrderedBroadcastAsUser(
      intent: Intent,
      user: UserHandle,
      receiverPermission: String?,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                          ) =
      this.context.sendOrderedBroadcastAsUser(
         intent,
         user,
         receiverPermission,
         resultReceiver,
         scheduler,
         initialCode,
         initialData,
         initialExtras
                                             )

   override fun grantUriPermission(toPackage: String, uri: Uri, modeFlags: Int) =
      this.context.grantUriPermission(toPackage, uri, modeFlags)

   override fun enforceCallingUriPermission(uri: Uri, modeFlags: Int, message: String?) =
      this.context.enforceCallingUriPermission(uri, modeFlags, message)

   override fun getCacheDir() =
      this.context.getCacheDir()

   override fun clearWallpaper() =
      this.context.clearWallpaper()

   @SuppressLint("MissingPermission")
   override fun sendStickyOrderedBroadcastAsUser(
      intent: Intent,
      user: UserHandle,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                                ) =
      this.context.sendStickyOrderedBroadcastAsUser(
         intent,
         user,
         resultReceiver,
         scheduler,
         initialCode,
         initialData,
         initialExtras
                                                   )

   override fun getPackageManager() =
      this.context.getPackageManager()

   override fun openOrCreateDatabase(name: String, mode: Int, factory: CursorFactory?) =
      this.context.openOrCreateDatabase(name, mode, factory)

   override fun openOrCreateDatabase(
      name: String,
      mode: Int,
      factory: CursorFactory?,
      errorHandler: DatabaseErrorHandler?
                                    ) =
      this.context.openOrCreateDatabase(name, mode, factory, errorHandler)

   override fun deleteFile(name: String) =
      this.context.deleteFile(name)

   override fun startService(service: Intent) =
      this.context.startService(service)

   override fun revokeUriPermission(uri: Uri, modeFlags: Int) =
      this.context.revokeUriPermission(uri, modeFlags)

   @TargetApi(VERSION_CODES.O)
   override fun revokeUriPermission(toPackage: String, uri: Uri, modeFlags: Int) =
      this.context.revokeUriPermission(toPackage, uri, modeFlags)

   @TargetApi(VERSION_CODES.N)
   override fun moveDatabaseFrom(sourceContext: Context, name: String) =
      this.context.moveDatabaseFrom(sourceContext, name)

   override fun startInstrumentation(className: ComponentName, profileFile: String?, arguments: Bundle?) =
      this.context.startInstrumentation(className, profileFile, arguments)

   override fun sendOrderedBroadcast(intent: Intent, receiverPermission: String?) =
      this.context.sendOrderedBroadcast(intent, receiverPermission)

   override fun sendOrderedBroadcast(
      intent: Intent,
      receiverPermission: String?,
      resultReceiver: BroadcastReceiver?,
      scheduler: Handler?,
      initialCode: Int,
      initialData: String?,
      initialExtras: Bundle?
                                    ) =
      this.context.sendOrderedBroadcast(
         intent,
         receiverPermission,
         resultReceiver,
         scheduler,
         initialCode,
         initialData,
         initialExtras
                                       )

   override fun unbindService(conn: ServiceConnection) =
      this.context.unbindService(conn)

   override fun getApplicationInfo() =
      this.context.getApplicationInfo()

   override fun getWallpaperDesiredMinimumHeight() =
      this.context.getWallpaperDesiredMinimumHeight()

   override fun createDisplayContext(display: Display) =
      this.context.createDisplayContext(display)

   @TargetApi(VERSION_CODES.O)
   override fun createContextForSplit(splitName: String) =
      this.context.createContextForSplit(splitName)

   override fun getTheme(): Resources.Theme =
      this.context.getTheme()

   override fun getPackageName() =
      this.context.getPackageName()

   override fun getContentResolver() =
      this.context.getContentResolver()

   override fun getObbDirs() =
      this.context.getObbDirs()

   override fun enforceCallingOrSelfUriPermission(uri: Uri, modeFlags: Int, message: String?) =
      this.context.enforceCallingOrSelfUriPermission(uri, modeFlags, message)

   @TargetApi(VERSION_CODES.N)
   override fun moveSharedPreferencesFrom(sourceContext: Context, name: String) =
      this.context.moveSharedPreferencesFrom(sourceContext, name)

   @TargetApi(VERSION_CODES.LOLLIPOP)
   override fun getExternalMediaDirs() =
      this.context.getExternalMediaDirs()

   override fun checkCallingPermission(permission: String) =
      this.context.checkCallingPermission(permission)

   override fun getExternalCacheDirs() =
      this.context.getExternalCacheDirs()

   @SuppressLint("MissingPermission")
   override fun sendStickyBroadcast(intent: Intent) =
      this.context.sendStickyBroadcast(intent)

   override fun enforceCallingPermission(permission: String, message: String?) =
      this.context.enforceCallingPermission(permission, message)

   override fun peekWallpaper() =
      this.context.peekWallpaper()

   @TargetApi(VERSION_CODES.O)
   override fun startForegroundService(service: Intent) =
      this.context.startForegroundService(service)

   override fun getDir(name: String, mode: Int) =
      this.context.getDir(name, mode)

   override fun databaseList() =
      this.context.databaseList()

   override fun createPackageContext(packageName: String, flags: Int) =
      this.context.createPackageContext(packageName, flags)

   override fun enforceUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int, message: String?) =
      this.context.enforceUriPermission(uri, pid, uid, modeFlags, message)

   override fun enforceUriPermission(
      uri: Uri?,
      readPermission: String?,
      writePermission: String?,
      pid: Int,
      uid: Int,
      modeFlags: Int,
      message: String?
                                    ) =
      this.context.enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message)

   @SuppressLint("MissingPermission")
   override fun removeStickyBroadcast(intent: Intent) =
      this.context.removeStickyBroadcast(intent)

   override fun isDeviceProtectedStorage() = false

   @TargetApi(VERSION_CODES.N)
   override fun createDeviceProtectedStorageContext() = this.context.createDeviceProtectedStorageContext()
}