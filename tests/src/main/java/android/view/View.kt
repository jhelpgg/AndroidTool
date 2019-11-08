package android.view

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.DummyDrawable
import android.util.AttributeSet
import android.util.LayoutDirection
import android.view.ContextMenu.ContextMenuInfo
import androidx.annotation.AttrRes

open class View(private val context: Context, private var attributes: AttributeSet?, private var defStyleAttr: Int)
{
   companion object
   {
       class DragShadowBuilder(view:View?)
       {
           private val view = WeakReference<View>(view)

           constructor() : this(null)

           fun getView() = this.view.get()

           fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point)
           {
               val view = this.view.get()

               if(view == null)
               {
                   Log.e("View", "Asked for drag thumb metrics but no view")
                   return
               }

               outShadowSize.set(view.getWidth(), view.getHeight())
               outShadowTouchPoint.set(outShadowSize.x / 2, outShadowSize.y / 2)
           }

           fun onDrawShadow(canvas: Canvas)
           {
               val view = this.view.get()

               if(view == null)
               {
                   Log.e("View", "Asked for drag thumb metrics but no view")
                   return
               }

               view.draw(canvas)
           }
       }

       @JvmStatic
      protected val VIEW_LOG_TAG = "View"

      @JvmStatic
      val NO_ID = -1

      @JvmStatic
      val NOT_FOCUSABLE = 0x00000000
      @JvmStatic
      val FOCUSABLE = 0x00000001
      @JvmStatic
      val FOCUSABLE_AUTO = 0x00000010

      @JvmStatic
      val VISIBLE = 0x00000000
      @JvmStatic
      val INVISIBLE = 0x00000004
      @JvmStatic
      val GONE = 0x00000008

      @JvmStatic
      val AUTOFILL_HINT_EMAIL_ADDRESS = "emailAddress"
      @JvmStatic
      val AUTOFILL_HINT_NAME = "name"
      @JvmStatic
      val AUTOFILL_HINT_USERNAME = "username"
      @JvmStatic
      val AUTOFILL_HINT_PASSWORD = "password"
      @JvmStatic
      val AUTOFILL_HINT_PHONE = "phone"
      @JvmStatic
      val AUTOFILL_HINT_POSTAL_ADDRESS = "postalAddress"
      @JvmStatic
      val AUTOFILL_HINT_POSTAL_CODE = "postalCode"
      @JvmStatic
      val AUTOFILL_HINT_CREDIT_CARD_NUMBER = "creditCardNumber"
      @JvmStatic
      val AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode"
      @JvmStatic
      val AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate"
      @JvmStatic
      val AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth"
      @JvmStatic
      val AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear"
      @JvmStatic
      val AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay"

      @JvmStatic
      val AUTOFILL_TYPE_NONE = 0
      @JvmStatic
      val AUTOFILL_TYPE_TEXT = 1
      @JvmStatic
      val AUTOFILL_TYPE_TOGGLE = 2
      @JvmStatic
      val AUTOFILL_TYPE_LIST = 3
      @JvmStatic
      val AUTOFILL_TYPE_DATE = 4

      @JvmStatic
      val IMPORTANT_FOR_AUTOFILL_AUTO = 0x0
      @JvmStatic
      val IMPORTANT_FOR_AUTOFILL_YES = 0x1
      @JvmStatic
      val IMPORTANT_FOR_AUTOFILL_NO = 0x2
      @JvmStatic
      val IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS = 0x4
      @JvmStatic
      val IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS = 0x8

      @JvmStatic
      val AUTOFILL_FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 0x1

      @JvmStatic
      val DRAWING_CACHE_QUALITY_LOW = 0x00080000
      @JvmStatic
      val DRAWING_CACHE_QUALITY_HIGH = 0x00100000
      @JvmStatic
      val DRAWING_CACHE_QUALITY_AUTO = 0x00000000

      @JvmStatic
      val SCROLLBARS_INSIDE_OVERLAY = 0
      @JvmStatic
      val SCROLLBARS_INSIDE_INSET = 0x01000000
      @JvmStatic
      val SCROLLBARS_OUTSIDE_OVERLAY = 0x02000000
      @JvmStatic
      val SCROLLBARS_OUTSIDE_INSET = 0x03000000

      @JvmStatic
      val KEEP_SCREEN_ON = 0x04000000
      @JvmStatic
      val SOUND_EFFECTS_ENABLED = 0x08000000
      @JvmStatic
      val HAPTIC_FEEDBACK_ENABLED = 0x10000000

      @JvmStatic
      val FOCUSABLES_ALL = 0x00000000
      @JvmStatic
      val FOCUSABLES_TOUCH_MODE = 0x00000001

      @JvmStatic
      val FOCUS_BACKWARD = 0x00000001
      @JvmStatic
      val FOCUS_FORWARD = 0x00000002
      @JvmStatic
      val FOCUS_LEFT = 0x00000011
      @JvmStatic
      val FOCUS_UP = 0x00000021
      @JvmStatic
      val FOCUS_RIGHT = 0x00000042
      @JvmStatic
      val FOCUS_DOWN = 0x00000082

      @JvmStatic
      val MEASURED_SIZE_MASK = 0x00ffffff;
      @JvmStatic
      val MEASURED_STATE_MASK = -0x1000000
      @JvmStatic
      val MEASURED_HEIGHT_STATE_SHIFT = 16
      @JvmStatic
      val MEASURED_STATE_TOO_SMALL = 0x01000000

      @JvmStatic
      val LAYOUT_DIRECTION_LTR = LayoutDirection.LTR
      @JvmStatic
      val LAYOUT_DIRECTION_RTL = LayoutDirection.RTL
      @JvmStatic
      val LAYOUT_DIRECTION_INHERIT = LayoutDirection.INHERIT
      @JvmStatic
      val LAYOUT_DIRECTION_LOCALE = LayoutDirection.LOCALE

      @JvmStatic
      val TEXT_DIRECTION_INHERIT = 0
      @JvmStatic
      val TEXT_DIRECTION_FIRST_STRONG = 1
      @JvmStatic
      val TEXT_DIRECTION_ANY_RTL = 2
      @JvmStatic
      val TEXT_DIRECTION_LTR = 3
      @JvmStatic
      val TEXT_DIRECTION_RTL = 4
      @JvmStatic
      val TEXT_DIRECTION_LOCALE = 5
      @JvmStatic
      val TEXT_DIRECTION_FIRST_STRONG_LTR = 6
      @JvmStatic
      val TEXT_DIRECTION_FIRST_STRONG_RTL = 7

      @JvmStatic
      val TEXT_ALIGNMENT_INHERIT = 0
      @JvmStatic
      val TEXT_ALIGNMENT_GRAVITY = 1
      @JvmStatic
      val TEXT_ALIGNMENT_TEXT_START = 2
      @JvmStatic
      val TEXT_ALIGNMENT_TEXT_END = 3
      @JvmStatic
      val TEXT_ALIGNMENT_CENTER = 4
      @JvmStatic
      val TEXT_ALIGNMENT_VIEW_START = 5
      @JvmStatic
      val TEXT_ALIGNMENT_VIEW_END = 6

      @JvmStatic
      val IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0x00000000
      @JvmStatic
      val IMPORTANT_FOR_ACCESSIBILITY_YES = 0x00000001
      @JvmStatic
      val IMPORTANT_FOR_ACCESSIBILITY_NO = 0x00000002
      @JvmStatic
      val IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 0x00000004

      @JvmStatic
      val ACCESSIBILITY_LIVE_REGION_NONE = 0x00000000
      @JvmStatic
      val ACCESSIBILITY_LIVE_REGION_POLITE = 0x00000001
      @JvmStatic
      val ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 0x00000002

      val PFLAG3_VIEW_IS_ANIMATING_TRANSFORM = 0x1
      val PFLAG3_VIEW_IS_ANIMATING_ALPHA = 0x2
      val PFLAG3_IS_LAID_OUT = 0x4
      val PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT = 0x8
      val PFLAG3_CALLED_SUPER = 0x10
      val PFLAG3_APPLYING_INSETS = 0x20
      val PFLAG3_FITTING_SYSTEM_WINDOWS = 0x40
      val PFLAG3_NESTED_SCROLLING_ENABLED = 0x80
      val PFLAG3_SCROLL_INDICATOR_TOP = 0x0100
      val PFLAG3_SCROLL_INDICATOR_BOTTOM = 0x0200
      val PFLAG3_SCROLL_INDICATOR_LEFT = 0x0400
      val PFLAG3_SCROLL_INDICATOR_RIGHT = 0x0800
      val PFLAG3_SCROLL_INDICATOR_START = 0x1000
      val PFLAG3_SCROLL_INDICATOR_END = 0x2000

      internal val PFLAG2_DRAG_CAN_ACCEPT = 0x00000001
      internal val PFLAG2_DRAG_HOVERED = 0x00000002

      internal val DRAG_MASK = PFLAG2_DRAG_CAN_ACCEPT or PFLAG2_DRAG_HOVERED

      internal val SCROLL_INDICATORS_NONE = 0x0000
      internal val SCROLL_INDICATORS_PFLAG3_MASK = (PFLAG3_SCROLL_INDICATOR_TOP
           or PFLAG3_SCROLL_INDICATOR_BOTTOM or PFLAG3_SCROLL_INDICATOR_LEFT
           or PFLAG3_SCROLL_INDICATOR_RIGHT or PFLAG3_SCROLL_INDICATOR_START
           or PFLAG3_SCROLL_INDICATOR_END)
      val SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT = 8


      @JvmStatic
      val SCROLL_INDICATOR_TOP = PFLAG3_SCROLL_INDICATOR_TOP shr SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT
      @JvmStatic
      val SCROLL_INDICATOR_BOTTOM = PFLAG3_SCROLL_INDICATOR_BOTTOM shr SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT
      @JvmStatic
      val SCROLL_INDICATOR_LEFT = PFLAG3_SCROLL_INDICATOR_LEFT shr SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT
      @JvmStatic
      val SCROLL_INDICATOR_RIGHT = PFLAG3_SCROLL_INDICATOR_RIGHT shr SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT
      @JvmStatic
      val SCROLL_INDICATOR_START = PFLAG3_SCROLL_INDICATOR_START shr SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT
      @JvmStatic
      val SCROLL_INDICATOR_END = PFLAG3_SCROLL_INDICATOR_END shr SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT

      internal val PFLAG3_ASSIST_BLOCKED = 0x4000
      private val PFLAG3_CLUSTER = 0x8000

      @JvmStatic
      val OVER_SCROLL_ALWAYS = 0
      @JvmStatic
      val OVER_SCROLL_IF_CONTENT_SCROLLS = 1
      @JvmStatic
      val OVER_SCROLL_NEVER = 2
      @JvmStatic
      val SYSTEM_UI_FLAG_VISIBLE = 0
      @JvmStatic
      val SYSTEM_UI_FLAG_LOW_PROFILE = 0x00000001
      @JvmStatic
      val SYSTEM_UI_FLAG_HIDE_NAVIGATION = 0x00000002
      @JvmStatic
      val SYSTEM_UI_FLAG_FULLSCREEN = 0x00000004
      @JvmStatic
      val SYSTEM_UI_FLAG_LAYOUT_STABLE = 0x00000100
      @JvmStatic
      val SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION = 0x00000200
      @JvmStatic
      val SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN = 0x00000400
      @JvmStatic
      val SYSTEM_UI_FLAG_IMMERSIVE = 0x00000800
      @JvmStatic
      val SYSTEM_UI_FLAG_IMMERSIVE_STICKY = 0x00001000
      @JvmStatic
      val SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 0x00002000

      @JvmStatic
      val SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR = 0x00000010
      @JvmStatic
      val STATUS_BAR_HIDDEN = SYSTEM_UI_FLAG_LOW_PROFILE
      @JvmStatic
      val STATUS_BAR_VISIBLE = SYSTEM_UI_FLAG_VISIBLE

      @JvmStatic
      val SYSTEM_UI_LAYOUT_FLAGS = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

      @JvmStatic
      val FIND_VIEWS_WITH_TEXT = 0x00000001
      @JvmStatic
      val FIND_VIEWS_WITH_CONTENT_DESCRIPTION = 0x00000002
      @JvmStatic
      val SCREEN_STATE_OFF = 0x0
      @JvmStatic
      val SCREEN_STATE_ON = 0x1
      @JvmStatic
      val SCROLL_AXIS_NONE = 0
      @JvmStatic
      val SCROLL_AXIS_HORIZONTAL = 1 shl 0
      @JvmStatic
      val SCROLL_AXIS_VERTICAL = 1 shl 1

      @JvmStatic
      val DRAG_FLAG_GLOBAL = 1 shl 8
      @JvmStatic
      val DRAG_FLAG_GLOBAL_URI_READ = Intent.FLAG_GRANT_READ_URI_PERMISSION
      @JvmStatic
      val DRAG_FLAG_GLOBAL_URI_WRITE = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
      @JvmStatic
      val DRAG_FLAG_GLOBAL_PERSISTABLE_URI_PERMISSION = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
      @JvmStatic
      val DRAG_FLAG_GLOBAL_PREFIX_URI_PERMISSION = Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
      @JvmStatic
      val DRAG_FLAG_OPAQUE = 1 shl 9

      @JvmStatic
      val SCROLLBAR_POSITION_DEFAULT = 0
      @JvmStatic
      val SCROLLBAR_POSITION_LEFT = 1
      @JvmStatic
      val SCROLLBAR_POSITION_RIGHT = 2

      val LAYER_TYPE_NONE = 0
      @JvmStatic
      val LAYER_TYPE_SOFTWARE = 1
      @JvmStatic
      val LAYER_TYPE_HARDWARE = 2

       @JvmStatic
       protected fun mergeDrawableStates(baseState: IntArray, additionalState: IntArray): IntArray=IntArray(0)

       @JvmStatic
       fun getDefaultSize(size: Int, measureSpec: Int): Int {
           var result = size
           val specMode = MeasureSpec.getMode(measureSpec)
           val specSize = MeasureSpec.getSize(measureSpec)

           when (specMode) {
               MeasureSpec.UNSPECIFIED -> result = size
               MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> result = specSize
           }
           return result
       }

       @JvmStatic
       fun combineMeasuredStates(curState: Int, newState: Int)= curState or newState

       @JvmStatic
       fun resolveSize(size: Int, measureSpec: Int)=
           resolveSizeAndState(size, measureSpec, 0) and MEASURED_SIZE_MASK

       @JvmStatic
       fun resolveSizeAndState(size: Int, measureSpec: Int, childMeasuredState: Int): Int {
           val specMode = MeasureSpec.getMode(measureSpec)
           val specSize = MeasureSpec.getSize(measureSpec)
           val result: Int
           when (specMode) {
               MeasureSpec.AT_MOST -> if (specSize < size) {
                   result = specSize or MEASURED_STATE_TOO_SMALL
               } else {
                   result = size
               }
               MeasureSpec.EXACTLY -> result = specSize
               MeasureSpec.UNSPECIFIED -> result = size
               else -> result = size
           }
           return result or (childMeasuredState and MEASURED_STATE_MASK)
       }

       @JvmStatic
       fun inflate(context: Context, resource: Int, root: ViewGroup): View?=null

       private val nextGenerateID = AtomicInteger(1)

       @JvmStatic
       fun generateViewId(): Int = nextGenerateID.getAndIncrement()

       @JvmStatic
       val  ALPHA : Property<View, Float> =
       object : FloatProperty<View>("alpha") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setAlpha(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getAlpha()
           }
       }

       @JvmStatic
       val TRANSLATION_X: Property<View, Float> = object : FloatProperty<View>("translationX") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setTranslationX(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getTranslationX()
           }
       }

       @JvmStatic
       val TRANSLATION_Y: Property<View, Float> = object : FloatProperty<View>("translationY") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setTranslationY(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getTranslationY()
           }
       }

       @JvmStatic
       val TRANSLATION_Z: Property<View, Float> = object : FloatProperty<View>("translationZ") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setTranslationZ(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getTranslationZ()
           }
       }

       @JvmStatic
       val X: Property<View, Float> = object : FloatProperty<View>("x") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setX(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getX()
           }
       }

       @JvmStatic
       val Y: Property<View, Float> = object : FloatProperty<View>("y") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setY(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getY()
           }
       }

       @JvmStatic
       val Z: Property<View, Float> = object : FloatProperty<View>("z") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setZ(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getZ()
           }
       }

       @JvmStatic
       val ROTATION: Property<View, Float> = object : FloatProperty<View>("rotation") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setRotation(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getRotation()
           }
       }

       @JvmStatic
       val ROTATION_X: Property<View, Float> = object : FloatProperty<View>("rotationX") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setRotationX(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getRotationX()
           }
       }

       @JvmStatic
       val ROTATION_Y: Property<View, Float> = object : FloatProperty<View>("rotationY") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setRotationY(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getRotationY()
           }
       }

       @JvmStatic
       val SCALE_X: Property<View, Float> = object : FloatProperty<View>("scaleX") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setScaleX(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getScaleX()
           }
       }

       @JvmStatic
       val SCALE_Y: Property<View, Float> = object : FloatProperty<View>("scaleY") {
           override fun setValue(`object`: View, value: Float) {
               `object`.setScaleY(value)
           }

           override operator fun get(`object`: View): Float {
               return `object`.getScaleY()
           }
       }

       object MeasureSpec {
           private val MODE_SHIFT = 30
           private val MODE_MASK = 0x3 shl MODE_SHIFT

           val UNSPECIFIED = 0 shl MODE_SHIFT
           val EXACTLY = 1 shl MODE_SHIFT
           val AT_MOST = 2 shl MODE_SHIFT

           fun makeMeasureSpec(size: Int, mode: Int): Int {
               return if (sUseBrokenMakeMeasureSpec) {
                   size + mode
               } else {
                   size and MODE_MASK.inv() or (mode and MODE_MASK)
               }
           }
           fun makeSafeMeasureSpec(size: Int, mode: Int): Int {
               return if (sUseZeroUnspecifiedMeasureSpec && mode == UNSPECIFIED) {
                   0
               } else makeMeasureSpec(size, mode)
           }

           fun getMode(measureSpec: Int): Int {

               return measureSpec and MODE_MASK
           }

           fun getSize(measureSpec: Int): Int {
               return measureSpec and MODE_MASK.inv()
           }

           internal fun adjust(measureSpec: Int, delta: Int): Int {
               val mode = getMode(measureSpec)
               var size = getSize(measureSpec)
               if (mode == UNSPECIFIED) {
                   // No need to adjust size for UNSPECIFIED mode.
                   return makeMeasureSpec(size, UNSPECIFIED)
               }
               size += delta
               if (size < 0) {
                   Log.e(
                       VIEW_LOG_TAG, "MeasureSpec.adjust: new size would be negative! (" + size +
                               ") spec: " + toString(measureSpec) + " delta: " + delta
                   )
                   size = 0
               }
               return makeMeasureSpec(size, mode)
           }

           fun toString(measureSpec: Int): String {
               val mode = getMode(measureSpec)
               val size = getSize(measureSpec)

               val sb = StringBuilder("MeasureSpec: ")

               if (mode == UNSPECIFIED)
                   sb.append("UNSPECIFIED ")
               else if (mode == EXACTLY)
                   sb.append("EXACTLY ")
               else if (mode == AT_MOST)
                   sb.append("AT_MOST ")
               else
                   sb.append(mode).append(" ")

               sb.append(size)
               return sb.toString()
           }
       }

       open class BaseSavedState : AbsSavedState {

           // Flags that describe what data in this state is valid
           internal var mSavedData: Int = 0
           internal var mStartActivityRequestWhoSaved: String? = null
           internal var mIsAutofilled: Boolean = false
           internal var mAutofillViewId: Int = 0

           /**
            * Constructor used when reading from a parcel using a given class loader.
            * Reads the state of the superclass.
            *
            * @param source parcel to read from
            * @param loader ClassLoader to use for reading
            */
           @JvmOverloads
           constructor(source: Parcel, loader: ClassLoader? = null) : super(source, loader) {
               mSavedData = source.readInt()
               mStartActivityRequestWhoSaved = source.readString()
               mIsAutofilled = source.readBoolean()
               mAutofillViewId = source.readInt()
           }

           /**
            * Constructor called by derived classes when creating their SavedState objects
            *
            * @param superState The state of the superclass of this view
            */
           constructor(superState: Parcelable) : super(superState) {}

           override fun writeToParcel(out: Parcel, flags: Int) {
               super.writeToParcel(out, flags)

               out.writeInt(mSavedData)
               out.writeString(mStartActivityRequestWhoSaved)
               out.writeBoolean(mIsAutofilled)
               out.writeInt(mAutofillViewId)
           }

           companion object {
               internal val START_ACTIVITY_REQUESTED_WHO_SAVED = 1
               internal val IS_AUTOFILLED = 2
               internal val AUTOFILL_ID = 4

               val CREATOR: Parcelable.Creator<BaseSavedState> =
                   object : Parcelable.ClassLoaderCreator<BaseSavedState> {
                       override fun createFromParcel(`in`: Parcel): BaseSavedState {
                           return BaseSavedState(`in`)
                       }

                       override fun createFromParcel(
                           `in`: Parcel,
                           loader: ClassLoader
                       ): BaseSavedState {
                           return BaseSavedState(`in`, loader)
                       }

                       override fun newArray(size: Int): Array<BaseSavedState> {
                           return arrayOfNulls(size)
                       }
                   }
           }
       }


       open class AccessibilityDelegate {
           fun sendAccessibilityEvent(host: View, eventType: Int) {
               host.sendAccessibilityEventInternal(eventType)
           }

           fun performAccessibilityAction(host: View, action: Int, args: Bundle): Boolean {
               return host.performAccessibilityActionInternal(action, args)
           }

           fun sendAccessibilityEventUnchecked(host: View, event: AccessibilityEvent) {
               host.sendAccessibilityEventUncheckedInternal(event)
           }

           fun dispatchPopulateAccessibilityEvent(host: View, event: AccessibilityEvent): Boolean {
               return host.dispatchPopulateAccessibilityEventInternal(event)
           }

           fun onPopulateAccessibilityEvent(host: View, event: AccessibilityEvent) {
               host.onPopulateAccessibilityEventInternal(event)
           }

           fun onInitializeAccessibilityEvent(host: View, event: AccessibilityEvent) {
               host.onInitializeAccessibilityEventInternal(event)
           }

           fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
               host.onInitializeAccessibilityNodeInfoInternal(info)
           }

           fun addExtraDataToAccessibilityNodeInfo(
               host: View,
               info: AccessibilityNodeInfo, extraDataKey: String,
               arguments: Bundle?
           ) {
               host.addExtraDataToAccessibilityNodeInfo(info, extraDataKey, arguments)
           }

           fun onRequestSendAccessibilityEvent(
               host: ViewGroup, child: View,
               event: AccessibilityEvent
           ): Boolean {
               return host.onRequestSendAccessibilityEventInternal(child, event)
           }

           fun getAccessibilityNodeProvider(host: View): AccessibilityNodeProvider? {
               return null
           }

           fun createAccessibilityNodeInfo(host: View): AccessibilityNodeInfo {
               return host.createAccessibilityNodeInfoInternal()
           }
       }
   }

   interface OnScrollChangeListener
   {
      fun onScrollChange(v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int)
   }

  interface OnLayoutChangeListener
   {
      fun onLayoutChange(
         v: View, left: Int, top: Int, right: Int, bottom: Int,
         oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
                        )
   }
   interface OnKeyListener
   {
      fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean
   }

   interface OnUnhandledKeyEventListener
   {
      fun onUnhandledKeyEvent(v: View, event: KeyEvent): Boolean
   }

   interface OnTouchListener
   {
      fun onTouch(v: View, event: MotionEvent): Boolean
   }

   interface OnHoverListener
   {
      fun onHover(v: View, event: MotionEvent): Boolean
   }

   interface OnGenericMotionListener
   {
      fun onGenericMotion(v: View, event: MotionEvent): Boolean
   }

   interface OnLongClickListener
   {
      fun onLongClick(v: View): Boolean
   }

   interface OnDragListener
   {
      fun onDrag(v: View, event: DragEvent): Boolean
   }

   interface OnFocusChangeListener
   {
      fun onFocusChange(v: View, hasFocus: Boolean)
   }

   interface OnClickListener
   {
      fun onClick(v: View)
   }

   interface OnContextClickListener
   {
      fun onContextClick(v: View): Boolean
   }

   interface OnCreateContextMenuListener
   {
      fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo)
   }

   interface OnSystemUiVisibilityChangeListener
   {
      fun onSystemUiVisibilityChange(visibility: Int)
   }

   interface OnAttachStateChangeListener
   {
      fun onViewAttachedToWindow(v: View)

      fun onViewDetachedFromWindow(v: View)
   }

   interface OnApplyWindowInsetsListener
   {
      fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets
   }

    interface OnCapturedPointerListener
    {
        fun onCapturedPointer(view: View, event: MotionEvent): Boolean
    }

    private var defStyleRes = 0
   private var id = NO_ID
   private var verticalFadingEdgeLength = 0
   private var hoorizontalFadingEdgeLength = 0
   protected var mParent:ViewParent?=null
   protected var mLayoutParams: ViewGroup.LayoutParams? = null
   private val viewOverlay by lazy{ViewOverlay(this.context, this)}
    private val viewTreeObserver = ViewTreeObserver(this.context)
    private var tag:Any?=null
    private val tags = HashMap<Int, Any>()
    private val viewPropertyAnimator = ViewPropertyAnimator(this)

   constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
        this(context, attributes, defStyleAttr)
   {
      this.defStyleRes = defStyleRes
   }

   constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

   constructor(context: Context) : this(context, null, 0)


   open fun getId() = this.id

   open fun setId(id: Int)
   {
      this.id = id
   }

   open fun getAttributeResolutionStack(@AttrRes attribute: Int) =  IntArray(0)

   open fun getAttributeSourceResourceMap() : Map<Int, Int> = HashMap()

   open fun getExplicitStyle() = 0

   fun saveAttributeDataForStyleable(
      context: Context,
      styleable: IntArray, attrs: AttributeSet?,
      t: TypedArray,
      defStyleAttr: Int, defStyleRes: Int
                                    ) = Unit

   open fun getVerticalFadingEdgeLength() = this.verticalFadingEdgeLength

   open fun setFadingEdgeLength(length: Int) {this.verticalFadingEdgeLength=length}

   open fun getHorizontalFadingEdgeLength(): Int=this.hoorizontalFadingEdgeLength

   open fun getVerticalScrollbarWidth() = 16

    protected open fun getHorizontalScrollbarHeight() = 16

   open fun setVerticalScrollbarThumbDrawable(drawable: Drawable?) = Unit

   open fun setVerticalScrollbarTrackDrawable(drawable: Drawable?) = Unit

   open fun setHorizontalScrollbarThumbDrawable(drawable: Drawable?) = Unit

   open fun setHorizontalScrollbarTrackDrawable(drawable: Drawable?) = Unit

   open fun getVerticalScrollbarThumbDrawable(): Drawable? = DummyDrawable

   open fun getVerticalScrollbarTrackDrawable(): Drawable? = DummyDrawable

   open fun getHorizontalScrollbarThumbDrawable(): Drawable? = DummyDrawable

   open fun getHorizontalScrollbarTrackDrawable(): Drawable? = DummyDrawable

   open fun setVerticalScrollbarPosition(position: Int) = Unit

   open fun getVerticalScrollbarPosition(): Int = 0

   open fun setScrollIndicators(indicators: Int) = Unit

   open fun setScrollIndicators(indicators: Int, mask: Int) = Unit

   open fun getScrollIndicators(): Int = 0

   open fun setOnScrollChangeListener(l: OnScrollChangeListener) = Unit

   open fun setOnFocusChangeListener(l: OnFocusChangeListener)=Unit

   open fun addOnLayoutChangeListener(listener: OnLayoutChangeListener)=Unit

   open fun removeOnLayoutChangeListener(listener: OnLayoutChangeListener)=Unit

   open fun addOnAttachStateChangeListener(listener: OnAttachStateChangeListener)=Unit

   open fun removeOnAttachStateChangeListener(listener: OnAttachStateChangeListener) = Unit

   open fun getOnFocusChangeListener(): OnFocusChangeListener? = null

   open fun setOnClickListener(l: OnClickListener?) = Unit

   open fun hasOnClickListeners(): Boolean = false

   open fun setOnLongClickListener(l: OnLongClickListener?) = Unit

   open fun setOnContextClickListener(l: OnContextClickListener?)=Unit

   open fun setOnCreateContextMenuListener(l: OnCreateContextMenuListener) = Unit

   open fun performClick(): Boolean = false

   open fun callOnClick(): Boolean = false

   open fun performLongClick(): Boolean=false

   open fun performLongClick(x: Float, y: Float): Boolean=false

   open fun performContextClick(x: Float, y: Float): Boolean=false

   open fun performContextClick(): Boolean = false

   open fun showContextMenu(): Boolean = false

   open fun showContextMenu(x: Float, y: Float): Boolean = false

   open fun startActionMode(callback: ActionMode.Callback): ActionMode?=null

   open fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode?=null

   open fun setOnKeyListener(l: OnKeyListener) = Unit

   open fun setOnTouchListener(l: OnTouchListener) = Unit

    open fun setOnGenericMotionListener(l: OnGenericMotionListener) = Unit

    open fun setOnHoverListener(l: OnHoverListener)=Unit

    open fun setOnDragListener(l: OnDragListener)=Unit

    fun setRevealOnFocusHint(revealOnFocus: Boolean)=Unit

    fun getRevealOnFocusHint(): Boolean = false

    open fun requestRectangleOnScreen(rectangle: Rect): Boolean=false

    open fun requestRectangleOnScreen(rectangle: Rect, immediate: Boolean): Boolean=false

    open fun clearFocus() = Unit

    open fun hasFocus(): Boolean = false

    open fun hasFocusable(): Boolean=false

    open fun hasExplicitFocusable(): Boolean=false

    protected open fun onFocusChanged(gainFocus: Boolean,  direction: Int, previouslyFocusedRect: Rect?) = Unit

    open fun setAccessibilityPaneTitle(accessibilityPaneTitle: CharSequence?) = Unit

    open fun getAccessibilityPaneTitle(): CharSequence? = null

    open fun sendAccessibilityEvent(eventType: Int) = Unit

    open fun announceForAccessibility(text: CharSequence)=Unit

    open fun sendAccessibilityEventUnchecked(event: AccessibilityEvent)=Unit

    open fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean=true

    open fun onPopulateAccessibilityEvent(event: AccessibilityEvent)=Unit

    open fun onInitializeAccessibilityEvent(event: AccessibilityEvent)=Unit

    open fun createAccessibilityNodeInfo() = AccessibilityNodeInfo.obtain(this)

    open fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo)=Unit

    open fun getAccessibilityClassName(): CharSequence= View::class.java!!.getName()

    open fun onProvideStructure(structure: ViewStructure)=Unit

    open fun onProvideAutofillStructure(structure: ViewStructure, flags: Int)=Unit

    open fun onProvideVirtualStructure(structure: ViewStructure)=Unit

    open fun onProvideAutofillVirtualStructure(structure: ViewStructure, flags: Int)=Unit

    open fun autofill(value: AutofillValue)=Unit

    open fun autofill(values: SparseArray<AutofillValue>)=Uint

    fun getAutofillId()= AutofillId(this.id)

    open fun setAutofillId(id: AutofillId?)=Unit

    open fun getAutofillType(): Int = AUTOFILL_TYPE_NONE

    open fun getAutofillHints(): Array<String>?=null

    open fun getAutofillValue(): AutofillValue?=null

    open fun getImportantForAutofill(): Int =
        mPrivateFlags3 and PFLAG3_IMPORTANT_FOR_AUTOFILL_MASK shr PFLAG3_IMPORTANT_FOR_AUTOFILL_SHIFT

    open fun setImportantForAutofill(mode: Int)=Unit

    fun isImportantForAutofill(): Boolean = false

    open fun setContentCaptureSession(contentCaptureSession: ContentCaptureSession?) = Unit

    fun getContentCaptureSession(): ContentCaptureSession?=null

    open fun dispatchProvideStructure(structure: ViewStructure)=Unit

    open fun dispatchProvideAutofillStructure(structure: ViewStructure, flags: Int) = Unit

    open fun addExtraDataToAccessibilityNodeInfo(
        info: AccessibilityNodeInfo, extraDataKey: String,
        arguments: Bundle?
    )=Unit

    open fun isVisibleToUserForAutofill(virtualId: Int): Boolean=false

    open fun getAccessibilityDelegate(): AccessibilityDelegate?=null

    open fun setAccessibilityDelegate(delegate: AccessibilityDelegate?)=Unit

    open fun getAccessibilityNodeProvider(): AccessibilityNodeProvider?=null

    open fun getContentDescription(): CharSequence=""

    open fun setContentDescription(contentDescription: CharSequence?)=Unit

    open fun setAccessibilityTraversalBefore(beforeId: Int)=Unit

    open fun getAccessibilityTraversalBefore(): Int= NO_ID

    open fun setAccessibilityTraversalAfter(@IdRes afterId: Int)=Unit

    open fun getAccessibilityTraversalAfter(): Int= NO_ID

    open fun getLabelFor(): Int = NO_ID

    open fun setLabelFor(@IdRes id: Int)=Unit

    open fun isFocused(): Boolean=false

    open fun findFocus(): View?=null

    open fun isScrollContainer(): Boolean = false

    open fun setScrollContainer(isScrollContainer: Boolean)=Unit

    open fun getDrawingCacheQuality(): Int = DRAWING_CACHE_QUALITY_LOW

    open fun setDrawingCacheQuality(@DrawingCacheQuality quality: Int) = Unit

    open fun getKeepScreenOn(): Boolean = false

    open fun setKeepScreenOn(keepScreenOn: Boolean)=Unit

    open fun getNextFocusLeftId(): Int= NO_ID

    open fun setNextFocusLeftId(@IdRes nextFocusLeftId: Int)=Unit

    open fun getNextFocusRightId(): Int= NO_ID

    open fun setNextFocusRightId(@IdRes nextFocusRightId: Int)=Unit

    open fun getNextFocusUpId(): Int= NO_ID

    open fun setNextFocusUpId(@IdRes nextFocusUpId: Int) = Unit

    open fun getNextFocusDownId(): Int = NO_ID

    open fun setNextFocusDownId(nextFocusDownId: Int) = Unit

    open fun getNextFocusForwardId(): Int= NO_ID

    open fun setNextFocusForwardId(nextFocusForwardId: Int)=Unit

    open fun getNextClusterForwardId(): Int = NO_ID

    open fun setNextClusterForwardId(nextClusterForwardId: Int) = Unit

    open fun isShown(): Boolean=false

    protected open fun fitSystemWindows(insets: Rect): Boolean=false

    open fun onApplyWindowInsets(insets: WindowInsets): WindowInsets = insets

    open fun setOnApplyWindowInsetsListener(listener: OnApplyWindowInsetsListener)=Unit

    open fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets=insets

    open fun setWindowInsetsAnimationListener(listener: WindowInsetsAnimationListener)=Unit

    open fun setSystemGestureExclusionRects(rects: List<Rect>)=Unit

    open fun getSystemGestureExclusionRects(): List<Rect> = ArrayList<Rect>()

    open fun getLocationInSurface(@Size(2) location: IntArray) = Unit

    open fun getRootWindowInsets(): WindowInsets? = null

    open fun computeSystemWindowInsets(`in`: WindowInsets, outLocalInsets: Rect): WindowInsets=`ìn`

    open fun setFitsSystemWindows(fitSystemWindows: Boolean)=Unit

    open fun getFitsSystemWindows(): Boolean=true

    open fun requestFitSystemWindows()=Unit

    open fun requestApplyInsets()=Unit

    open fun getVisibility(): Int= VISIBLE

    open fun setVisibility(visibility: Int)=Unit

    open fun isEnabled(): Boolean=true

    open fun setEnabled(enabled: Boolean)=Unit

    open fun setFocusable(focusable: Boolean)=Unit

    open fun setFocusable(focusable: Int)=Unit

    open fun setFocusableInTouchMode(focusableInTouchMode: Boolean)=Unit

    open fun setAutofillHints(vararg autofillHints: String)=Unit

    open fun setSoundEffectsEnabled(soundEffectsEnabled: Boolean)=Unit

    open fun isSoundEffectsEnabled(): Boolean=false

    open fun setHapticFeedbackEnabled(hapticFeedbackEnabled: Boolean)=Unit

    open fun isHapticFeedbackEnabled(): Boolean=false

    open fun getRawLayoutDirection(): Int=LAYOUT_DIRECTION_LTR

    open fun setLayoutDirection(@LayoutDir layoutDirection: Int)=Unit

    open fun getLayoutDirection(): Int=LAYOUT_DIRECTION_LTR

    open fun hasTransientState(): Boolean=false

    open fun setHasTransientState(hasTransientState: Boolean)=Unit

    open fun isAttachedToWindow(): Boolean=false

    open fun isLaidOut(): Boolean=true

    open fun setWillNotDraw(willNotDraw: Boolean)=Unit

    open fun willNotDraw(): Boolean=true

    open fun setWillNotCacheDrawing(willNotCacheDrawing: Boolean)=Unit

    open fun willNotCacheDrawing(): Boolean=true

    open fun isClickable(): Boolean=false

    open fun setClickable(clickable: Boolean)=Unit

    open fun isLongClickable(): Boolean=false

    open fun setLongClickable(longClickable: Boolean)=Unit

    open fun isContextClickable(): Boolean=false

    open fun setContextClickable(contextClickable: Boolean)=Unit

    open fun setPressed(pressed: Boolean)=Unit

    protected open fun dispatchSetPressed(pressed: Boolean)=Unit

    open fun isPressed(): Boolean=false

    open fun isSaveEnabled(): Boolean=false

    open fun setSaveEnabled(enabled: Boolean)=Unit

    open fun getFilterTouchesWhenObscured(): Boolean=false

    open fun setFilterTouchesWhenObscured(enabled: Boolean)=Unit

    open fun isSaveFromParentEnabled(): Boolean=false

    open fun setSaveFromParentEnabled(enabled: Boolean)=Unit

    fun isFocusable(): Boolean=false

    open fun getFocusable(): Int = FOCUSABLE

    fun isFocusableInTouchMode(): Boolean=false

    open fun isScreenReaderFocusable(): Boolean=false

    open fun setScreenReaderFocusable(screenReaderFocusable: Boolean)=Unit

    open fun isAccessibilityHeading(): Boolean=false

    open fun setAccessibilityHeading(isHeading: Boolean)=Unit

    open fun focusSearch(@FocusRealDirection direction: Int): View?=null

    fun isKeyboardNavigationCluster(): Boolean=false

    open fun setKeyboardNavigationCluster(isCluster: Boolean)=Unit

    fun isFocusedByDefault(): Boolean=false

    open fun setFocusedByDefault(isFocusedByDefault: Boolean)=Unit

    open fun keyboardNavigationClusterSearch(currentCluster: View, direction: Int): View?=null

    open fun dispatchUnhandledMove(focused: View, @FocusRealDirection direction: Int): Boolean=false

    open fun setDefaultFocusHighlightEnabled(defaultFocusHighlightEnabled: Boolean)=Unit

    open fun getDefaultFocusHighlightEnabled(): Boolean=false

    open fun getFocusables(direction: Int)= ArrayList<View>()

    open fun addFocusables(views: ArrayList<View>, direction: Int)=Unit

    open fun addFocusables(views: ArrayList<View>, direction: Int, focusableMode: Int)=Unit

    open fun addKeyboardNavigationClusters(views: Collection<View>, direction: Int)=Unit

    open fun findViewsWithText(outViews: ArrayList<View>, searched: CharSequence, flags: Int)=Unit

    open fun getTouchables()= ArrayList<View>()

    open fun addTouchables(views: ArrayList<View>)=Unit

    open fun isAccessibilityFocused(): Boolean=false

    open fun requestAccessibilityFocus(): Boolean=false

    fun requestFocus(): Boolean=Unit

    open fun restoreDefaultFocus(): Boolean=false

    open fun requestFocus(direction: Int): Boolean=Unit

    open fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean=false

    open fun requestFocusFromTouch(): Boolean=false

    open fun getImportantForAccessibility(): Int=IMPORTANT_FOR_ACCESSIBILITY_NO

    open fun setAccessibilityLiveRegion(mode: Int)=Unit

    open fun getAccessibilityLiveRegion(): Int=ACCESSIBILITY_LIVE_REGION_NONE

    open fun setImportantForAccessibility(mode: Int)=Unit

    open fun isImportantForAccessibility(): Boolean=false

    open fun getParentForAccessibility(): ViewParent?=null

    open fun addChildrenForAccessibility(outChildren: ArrayList<View>)=Unit

    open fun setTransitionVisibility(visibility: Int)=Unit

    open fun dispatchNestedPrePerformAccessibilityAction(action: Int, arguments: Bundle): Boolean=false

    open fun performAccessibilityAction(action: Int, arguments: Bundle): Boolean=false

    fun isTemporarilyDetached(): Boolean=false

    open fun dispatchStartTemporaryDetach()=Unit

    open fun onStartTemporaryDetach()=Unit

    open fun dispatchFinishTemporaryDetach()=Unit

    open fun onFinishTemporaryDetach()=Unit

    open fun getKeyDispatcherState(): KeyEvent.DispatcherState?=null

    open fun dispatchKeyEventPreIme(event: KeyEvent): Boolean=false

    open fun dispatchKeyEvent(event: KeyEvent): Boolean=false

    open fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean=false

    open fun dispatchTouchEvent(event: MotionEvent): Boolean=false

    open fun onFilterTouchEventForSecurity(event: MotionEvent): Boolean=false

    open fun dispatchTrackballEvent(event: MotionEvent): Boolean=false

    open fun dispatchCapturedPointerEvent(event: MotionEvent): Boolean=false

    open fun dispatchGenericMotionEvent(event: MotionEvent): Boolean=false

    protected open fun dispatchHoverEvent(event: MotionEvent): Boolean=false

    protected open fun dispatchGenericPointerEvent(event: MotionEvent): Boolean=false

    protected  open fun dispatchGenericFocusedEvent(event: MotionEvent): Boolean=false

    open fun dispatchWindowFocusChanged(hasFocus: Boolean)=Unit

    open fun onWindowFocusChanged(hasWindowFocus: Boolean)=Unit

    open fun hasWindowFocus(): Boolean=false

    protected open fun dispatchVisibilityChanged(changedView: View, visibility: Int) = Unit

    protected open fun onVisibilityChanged(changedView: View, visibility: Int)=Unit

    open fun dispatchDisplayHint(@Visibility hint: Int)=Unit

    protected open fun onDisplayHint(hint: Int)=Unit

    open fun dispatchWindowVisibilityChanged(visibility: Int)=Unit

    protected open fun onWindowVisibilityChanged(@Visibility visibility: Int)=Unit

    open fun onVisibilityAggregated(isVisible: Boolean)=Unit

    open fun getWindowVisibility(): Int = VISIBLE

    open fun getWindowVisibleDisplayFrame(outRect: Rect)=Unit

    open fun dispatchConfigurationChanged(newConfig: Configuration)=Unit

    protected open fun onConfigurationChanged(newConfig: Configuration)=Unit

    open fun isInTouchMode(): Boolean=false

    fun getContext(): Context = this.context

    open fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean=false

    open override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean=false

    open fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean=false

    open fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean=false

    open fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent): Boolean=false

    open fun onKeyShortcut(keyCode: Int, event: KeyEvent): Boolean=false

    open fun onCheckIsTextEditor(): Boolean=false

    open fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection?=null

    open fun checkInputConnectionProxy(view: View): Boolean=false

    open fun createContextMenu(menu: ContextMenu)=Unit

    protected open fun getContextMenuInfo(): ContextMenuInfo?=null

    protected open fun onCreateContextMenu(menu: ContextMenu)=Unit

    open fun onTrackballEvent(event: MotionEvent): Boolean=false

    open fun onGenericMotionEvent(event: MotionEvent): Boolean=false

    open fun onHoverEvent(event: MotionEvent): Boolean=false

    open fun isHovered(): Boolean=false

    open fun setHovered(hovered: Boolean)=Unit

    open fun onHoverChanged(hovered: Boolean)=Unit

    open fun onTouchEvent(event: MotionEvent): Boolean=false

    open fun cancelLongPress()=Unit

    open fun setTouchDelegate(delegate: TouchDelegate)=Unit

    open fun getTouchDelegate(): TouchDelegate?=null

    fun requestUnbufferedDispatch(event: MotionEvent)=Unit

    open fun bringToFront()=Unit

    protected open fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int)=Unit

    protected open fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)=Unit

    protected open fun dispatchDraw(canvas: Canvas)=Unit

    fun getParent(): ViewParent? = null

    open fun setScrollX(value: Int)=Unit

    open fun setScrollY(value: Int)=Unit

    fun getScrollX(): Int=0

    fun getScrollY(): Int=0

    fun getWidth(): Int=16

    fun getHeight(): Int=16

    open fun getDrawingRect(outRect: Rect)=Unit

    fun getMeasuredWidth(): Int=16

    fun getMeasuredWidthAndState(): Int= MEASURED_STATE_TOO_SMALL

    fun getMeasuredHeight(): Int=16

    fun getMeasuredHeightAndState(): Int= MEASURED_STATE_TOO_SMALL

    fun getMeasuredState(): Int = MEASURED_STATE_TOO_SMALL

    open fun getMatrix(): Matrix=Matrix.IDENTITY_MATRIX

    open fun getCameraDistance(): Float=0f

    open fun setCameraDistance(distance: Float)=Unit

    open fun getRotation(): Float=0f

    open fun setRotation(rotation: Float)=Unit

    open fun getRotationY(): Float=0f

    open fun setRotationY(rotationY: Float)=Unit

    open fun getRotationX(): Float=0f

    open fun setRotationX(rotationX: Float)=Unit

    open fun getScaleX(): Float=1f

    open fun setScaleX(scaleX: Float)=Unit

    open fun getScaleY(): Float=1f

    open fun setScaleY(scaleY: Float)=Unit

    open fun getPivotX(): Float=0f

    open fun setPivotX(pivotX: Float)=Unit

    open fun getPivotY(): Float=0f

    open fun setPivotY(pivotY: Float)=Unit

    open fun isPivotSet(): Boolean=false

    open fun resetPivot()=Unit

    open fun getAlpha(): Float=1f

    open fun forceHasOverlappingRendering(hasOverlappingRendering: Boolean)=Unit

    open fun getHasOverlappingRendering(): Boolean=false

    open fun hasOverlappingRendering(): Boolean=true

    open fun setAlpha(alpha: Float)=Unit

    open fun setTransitionAlpha(alpha: Float)=Unit

    open fun getTransitionAlpha(): Float = 1f

    open fun setForceDarkAllowed(allow: Boolean)=Unit

    open fun isForceDarkAllowed(): Boolean=false

    fun getTop(): Int=0

    fun setTop(top: Int)=Unit

    fun getBottom(): Int=16

    open fun isDirty(): Boolean=false

    fun setBottom(bottom: Int)=Unit

    fun getLeft(): Int=0

    fun setLeft(left: Int)=Unit

    fun getRight(): Int=16

    fun setRight(right: Int)=Unit

    open fun getX(): Float=0f

    open fun setX(x: Float)=Unit

    open fun getY(): Float=0f

    open fun setY(y: Float)=Unit

    open fun getZ(): Float=0f

    open fun setZ(z: Float)=Unit

    open fun getElevation(): Float=0f

    open fun setElevation(elevation: Float)=Unit

    open fun getTranslationX(): Float=0f

    open fun setTranslationX(translationX: Float)=Unit

    open fun getTranslationY(): Float=0f

    open fun setTranslationY(translationY: Float)=Unit

    open fun getTranslationZ(): Float=0f

    open fun setTranslationZ(translationZ: Float)=Unit

    open fun setAnimationMatrix(matrix: Matrix?)=Unit

    open fun getAnimationMatrix(): Matrix?=null

    open fun getStateListAnimator(): StateListAnimator? = null

    open fun setStateListAnimator(stateListAnimator: StateListAnimator?)=Unit

    open fun getClipToOutline(): Boolean=true

    open fun setClipToOutline(clipToOutline: Boolean)=Unit

    open fun setOutlineProvider(provider: ViewOutlineProvider?)=Unit

    open fun getOutlineProvider(): ViewOutlineProvider?=null

    open fun invalidateOutline()=Unit

    open fun setOutlineSpotShadowColor(color: Int)=Unit

    open fun getOutlineSpotShadowColor(): Int=0xCAFEFFACE.toInt()

    open fun setOutlineAmbientShadowColor(color: Int)=Unit

    open fun getOutlineAmbientShadowColor(): Int =0xCAFEFFACE.toInt()

    open fun getHitRect(outRect: Rect)=Unit

    open fun getFocusedRect(r: Rect)=Unit

    open fun getGlobalVisibleRect(r: Rect, globalOffset: Point?): Boolean=false

    fun getGlobalVisibleRect(r: Rect): Boolean=false

    fun getLocalVisibleRect(r: Rect): Boolean=false

    open fun offsetTopAndBottom(offset: Int)=Unit

    open fun offsetLeftAndRight(offset: Int)=Unit

    open fun getLayoutParams(): ViewGroup.LayoutParams?=null

    open fun setLayoutParams(params: ViewGroup.LayoutParams)Unit

    open fun scrollTo(x: Int, y: Int)=Unit

    open fun scrollBy(x: Int, y: Int)=Unit

    protected open fun awakenScrollBars(): Boolean=false

    protected open fun awakenScrollBars(startDelay: Int): Boolean=false

    protected open fun awakenScrollBars(startDelay: Int, invalidate: Boolean): Boolean=false

    open fun invalidate(dirty: Rect)=Unit

    open fun invalidate(l: Int, t: Int, r: Int, b: Int)=Unit

    open fun invalidate()=Unit

    open fun isOpaque(): Boolean=true

    open fun getHandler(): Handler = mainHandler

    open fun post(action: Runnable): Boolean = mainHandler.post(action)

    open fun postDelayed(action: Runnable, delayMillis: Long): Boolean = mainHandler.postDelayed(action, delayMillis)

    open fun postOnAnimation(action: Runnable)=Unit

    open fun postOnAnimationDelayed(action: Runnable, delayMillis: Long)=Unit

    open fun removeCallbacks(action: Runnable): Boolean = mainHandler. removeCallbacks(action)

    open fun postInvalidate()=Unit

    open fun postInvalidate(left: Int, top: Int, right: Int, bottom: Int)=Unit

    open fun postInvalidateDelayed(delayMilliseconds: Long)=Unit

    open fun postInvalidateDelayed(delayMilliseconds: Long, left: Int, top: Int, right: Int, bottom: Int)=Unit

    open fun postInvalidateOnAnimation()=Unit

    open fun postInvalidateOnAnimation(left: Int, top: Int, right: Int, bottom: Int)=Unit

    open fun computeScroll()=Unit

    open fun isHorizontalFadingEdgeEnabled(): Boolean=false

    open fun setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled: Boolean)=Unit

    open fun isVerticalFadingEdgeEnabled(): Boolean = false

    open fun setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled: Boolean)=unit

    protected open fun getTopFadingEdgeStrength(): Float=0f

    protected open fun getBottomFadingEdgeStrength(): Float=0f

    protected open fun getLeftFadingEdgeStrength(): Float=0f

    protected open fun getRightFadingEdgeStrength(): Float=0f

    open fun isHorizontalScrollBarEnabled(): Boolean=false

    open fun setHorizontalScrollBarEnabled(horizontalScrollBarEnabled: Boolean)

    open fun isVerticalScrollBarEnabled(): Boolean=false

    open fun setVerticalScrollBarEnabled(verticalScrollBarEnabled: Boolean)=Unit

    open fun setScrollbarFadingEnabled(fadeScrollbars: Boolean)=Unit

    open fun isScrollbarFadingEnabled(): Boolean=false

    open fun getScrollBarDefaultDelayBeforeFade(): Int=0

    open fun setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade: Int)=Unit

    open fun getScrollBarFadeDuration(): Int=0

    open fun setScrollBarFadeDuration(scrollBarFadeDuration: Int)=Unit

    open fun getScrollBarSize(): Int=16

    open fun setScrollBarSize(scrollBarSize: Int)=Unit

    open fun setScrollBarStyle(@ScrollBarStyle style: Int)=Unit

    open fun getScrollBarStyle(): Int = SCROLLBARS_INSIDE_INSET

    protected open fun computeHorizontalScrollRange(): Int = this.getWidth()

    protected open fun computeHorizontalScrollOffset(): Int = 0

    protected open fun computeHorizontalScrollExtent(): Int = this.getWidth()

    protected open fun computeVerticalScrollRange(): Int=this.getHeight()

    protected open fun computeVerticalScrollOffset(): Int=0

    protected open fun computeVerticalScrollExtent(): Int=this.getHeight()

    open fun canScrollHorizontally(direction: Int): Boolean=false

    open fun canScrollVertically(direction: Int): Boolean=false

    protected fun onDrawScrollBars(canvas: Canvas)=Unit

    protected open fun onDraw(canvas: Canvas)=Unit

    protected open fun onAttachedToWindow()=Unit

    open fun onScreenStateChanged(screenState: Int)=Unit

    open fun onMovedToDisplay(displayId: Int, config: Configuration)=Unit

    open fun onRtlPropertiesChanged(layoutDirection: Int)=Unit

    open fun canResolveLayoutDirection(): Boolean=true

    open fun isLayoutDirectionResolved(): Boolean=true

    protected open fun onDetachedFromWindow()=Unit

    protected open fun getWindowAttachCount(): Int=0

    open fun getWindowToken(): IBinder?=null

    open fun getWindowId(): WindowId?=null

    open fun getApplicationWindowToken(): IBinder?=null

    open fun getDisplay(): Display?=null

    fun cancelPendingInputEvents()=Unit

    open fun onCancelPendingInputEvents()=Unit

    open fun saveHierarchyState(container: SparseArray<Parcelable>)=Unit

    protected open fun dispatchSaveInstanceState(container: SparseArray<Parcelable>)=Unit

    protected open fun onSaveInstanceState(): Parcelable?=BaseSavedState.EMPTY_STATE

    open fun restoreHierarchyState(container: SparseArray<Parcelable>)=Unit

    protected open fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>)=Unit

    protected open fun onRestoreInstanceState(state: Parcelable)=Unit

    open fun getDrawingTime(): Long=0L

    open fun setDuplicateParentStateEnabled(enabled: Boolean)=Unit

    open fun isDuplicateParentStateEnabled(): Boolean=false

    open fun setLayerType(layerType: Int, paint: Paint?)=Unit

    open fun setLayerPaint(paint: Paint?)=Unit

    open fun getLayerType(): Int=LAYER_TYPE_NONE

    open fun buildLayer()=Unit

    open fun setDrawingCacheEnabled(enabled: Boolean)=Unit

    open fun isDrawingCacheEnabled(): Boolean=false

    open fun getDrawingCache(): Bitmap?=null

    open fun getDrawingCache(autoScale: Boolean): Bitmap?=null

    open fun destroyDrawingCache()=Unit

    open fun setDrawingCacheBackgroundColor(color: Int)=Unit

    open fun getDrawingCacheBackgroundColor(): Int = 0xCAFEFACE.toInt()

    open fun buildDrawingCache()=Unit

    open fun buildDrawingCache(autoScale: Boolean)=Unit

    open fun isInEditMode(): Boolean=false

    protected open fun isPaddingOffsetRequired(): Boolean=false

    protected open fun getLeftPaddingOffset(): Int=0

    protected open fun getRightPaddingOffset(): Int=0

    protected open fun getTopPaddingOffset(): Int=0

    protected open fun getBottomPaddingOffset(): Int=0

    open fun isHardwareAccelerated(): Boolean=false

    open fun setClipBounds(clipBounds: Rect?)=Unit

    open fun getClipBounds(): Rect?=null

    open fun getClipBounds(outRect: Rect): Boolean=false

    open fun draw(canvas: Canvas)=Unit

    open fun getOverlay(): ViewOverlay = this.viewOverlay

    open fun getSolidColor(): Int=0

    open fun isLayoutRequested(): Boolean=false

    open fun layout(l: Int, t: Int, r: Int, b: Int)=Unit

    protected open fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)=Unit

    fun setLeftTopRightBottom(left: Int, top: Int, right: Int, bottom: Int)=Unit

    protected open fun onFinishInflate()=Unit

    open fun getResources(): Resources = theResources

    open public  fun invalidateDrawable(drawable: Drawable)=Unit

    open public fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) = Unit

    open public fun unscheduleDrawable(who: Drawable, what: Runnable)=Unit

    open fun unscheduleDrawable(who: Drawable)=Unit

    protected open fun verifyDrawable(who: Drawable): Boolean=false

    protected open fun drawableStateChanged()=Unit

    open fun drawableHotspotChanged(x: Float, y: Float)=Unit

    open fun dispatchDrawableHotspotChanged(x: Float, y: Float)=Unit

    open fun refreshDrawableState()=Unit

    fun getDrawableState(): IntArray= IntArray(0)

    protected open fun onCreateDrawableState(extraSpace: Int): IntArray=IntArray(0)

    open fun jumpDrawablesToCurrentState()=Unit

    open fun setBackgroundColor(color: Int)=Unit

    open fun setBackgroundResource(resid: Int)=Unit

    open fun setBackground(background: Drawable?)=Unit

    open fun setBackgroundDrawable(background: Drawable)=Unit

    open fun getBackground(): Drawable = DummyDrawable

    open fun setBackgroundTintList(tint: ColorStateList?)=Unit

    open fun getBackgroundTintList(): ColorStateList?=null

    open fun setBackgroundTintMode(tintMode: PorterDuff.Mode?)=Unit

    open fun setBackgroundTintBlendMode(blendMode: BlendMode?)=Unit

    open fun getBackgroundTintMode(): PorterDuff.Mode?=null

    open fun getBackgroundTintBlendMode(): BlendMode?=null

    open fun getForeground(): Drawable?=null

    open fun setForeground(foreground: Drawable?)=Unit

    open fun getForegroundGravity(): Int = Gravity.START or Gravity.TOP

    open fun setForegroundGravity(gravity: Int)=Unit

    open fun setForegroundTintList(tint: ColorStateList?)=Unit

    open fun getForegroundTintList(): ColorStateList?=null

    open fun setForegroundTintMode(tintMode: PorterDuff.Mode?)=Unit

    open fun setForegroundTintBlendMode(blendMode: BlendMode?)=Unit

    open fun getForegroundTintMode(): PorterDuff.Mode? = null

    open fun getForegroundTintBlendMode(): BlendMode?=null

    open fun onDrawForeground(canvas: Canvas)=Unit

    open fun setPadding(left: Int, top: Int, right: Int, bottom: Int)=Unit

    open fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int)=Unit

    open fun getSourceLayoutResId(): Int=Resources.ID_NULL

    open fun getPaddingTop(): Int=0

    open fun getPaddingBottom(): Int=0

    open fun getPaddingLeft(): Int=0

    open fun getPaddingStart(): Int=0

    open fun getPaddingRight(): Int=0

    open fun getPaddingEnd(): Int=0

    open fun isPaddingRelative(): Boolean=false

    open fun setSelected(selected: Boolean)=Unit

    protected open fun dispatchSetSelected(selected: Boolean)=Unit

    open fun isSelected(): Boolean=false

    open fun setActivated(activated: Boolean)=Unit

     protected open fun dispatchSetActivated(activated: Boolean) = Unit

    open fun isActivated(): Boolean = false

    open fun getViewTreeObserver(): ViewTreeObserver = this.viewTreeObserver

    open fun getRootView(): View=this

    open fun transformMatrixToGlobal(matrix: Matrix)=Unit

    open fun transformMatrixToLocal(matrix: Matrix)=Unit

    open fun getLocationOnScreen(outLocation: IntArray) = Unit

    open fun getLocationInWindow(outLocation: IntArray)=Unit

    fun <T : View> findViewById(id: Int): T? = if(this.id==id) this as T else null

    fun <T : View> requireViewById(id: Int): T = this.findViewById<T>(id) ?: throw IllegalArgumentException("ID does not reference a View inside this View")

    fun <T : View> findViewWithTag(tag: Any?): T? =
        when
        {
            tag == null -> null
            tag == this.tag -> this as T
            else -> null
        }

    fun <T : View> findViewByPredicate(predicate: Predicate<View>): T? = if(predicate.test(this)) this as T else null

    open fun getUniqueDrawingId(): Long=0

    open fun getTag(): Any? = this.tag

    open fun setTag(tag: Any) {this.tag=tag}


    open fun getTag(key: Int)=this.tags.get(key)

    open fun setTag(key: Int, tag: Any?)
    {
        if (tag == null)
        {
            this.tags.remove(key)
        }
        else
        {
            this.tags.put(key, tag)
        }
    }

    open fun getBaseline(): Int=-1

    open fun isInLayout(): Boolean=false

    open fun requestLayout()=Unit

    open fun forceLayout()=Unit

    fun measure(widthMeasureSpec: Int, heightMeasureSpec: Int)=Unit

    protected open fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
        this.setMeasuredDimension(
            getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
            getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec)
        )

    protected fun setMeasuredDimension(measuredWidth: Int, measuredHeight: Int)=Unit

    protected open fun getSuggestedMinimumHeight(): Int = 16

    protected open fun getSuggestedMinimumWidth(): Int=16

    open fun getMinimumHeight(): Int=16

    open fun setMinimumHeight(minHeight: Int)=Unit

    open fun getMinimumWidth(): Int=16

    open fun setMinimumWidth(minWidth: Int)=Unit

    open fun getAnimation(): Animation?=null

    open fun startAnimation(animation: Animation)=Unit

    open fun clearAnimation()=Unit

    open fun setAnimation(animation: Animation?)=Unit

    protected open fun onAnimationStart()=Unit

    protected open fun onAnimationEnd()=Unit

    protected open fun onSetAlpha(alpha: Int): Boolean=false

    open fun playSoundEffect(soundConstant: Int)=Unit

    open fun performHapticFeedback(feedbackConstant: Int): Boolean=false

    open fun performHapticFeedback(feedbackConstant: Int, flags: Int): Boolean=false

    open fun setSystemUiVisibility(visibility: Int)=Unit

    open fun getSystemUiVisibility(): Int=SYSTEM_UI_FLAG_LAYOUT_STABLE

    open fun getWindowSystemUiVisibility(): Int = 0

    open fun onWindowSystemUiVisibilityChanged(visible: Int)=Unit

    open fun dispatchWindowSystemUiVisiblityChanged(visible: Int)=Unit

    open fun setOnSystemUiVisibilityChangeListener(l: OnSystemUiVisibilityChangeListener)=Unit

    open fun dispatchSystemUiVisibilityChanged(visibility: Int)=Unit

    fun startDrag(
        data: ClipData, shadowBuilder: DragShadowBuilder,
        myLocalState: Any, flags: Int
    ): Boolean = this.startDragAndDrop(data, shadowBuilder, myLocalState, flags)

    fun startDragAndDrop(
        data: ClipData, shadowBuilder: DragShadowBuilder,
        myLocalState: Any, flags: Int
    ): Boolean=false

    fun cancelDragAndDrop()=Unit

    fun updateDragShadow(shadowBuilder: DragShadowBuilder)=Unit

    open fun onDragEvent(event: DragEvent): Boolean=false

    open fun dispatchDragEvent(event: DragEvent): Boolean=false

    protected open fun overScrollBy(
        deltaX: Int, deltaY: Int,
        scrollX: Int, scrollY: Int,
        scrollRangeX: Int, scrollRangeY: Int,
        maxOverScrollX: Int, maxOverScrollY: Int,
        isTouchEvent: Boolean
    ): Boolean=false

    protected open fun onOverScrolled(
        scrollX: Int, scrollY: Int,
        clampedX: Boolean, clampedY: Boolean
    )=Unit

    open fun getOverScrollMode(): Int=OVER_SCROLL_ALWAYS

    open fun setOverScrollMode(overScrollMode: Int)=Unit

    open fun setNestedScrollingEnabled(enabled: Boolean)=Unit

    open fun isNestedScrollingEnabled(): Boolean=false

    open fun startNestedScroll(axes: Int): Boolean=false

    open fun stopNestedScroll()=Unit

    open fun hasNestedScrollingParent(): Boolean=false

    open fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int, @Size(2) offsetInWindow: IntArray?
    ): Boolean = false

    open fun dispatchNestedPreScroll(
        dx: Int, dy: Int,
        @Size(2) consumed: IntArray?, @Size(2) offsetInWindow: IntArray?
    ): Boolean = false

    open fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean=false

    open fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean=false

    open fun setTextDirection(textDirection: Int)=Unit

    open fun getTextDirection(): Int=TEXT_DIRECTION_LTR

    open fun canResolveTextDirection(): Boolean=true

    open fun isTextDirectionResolved(): Boolean=true

    open fun setTextAlignment(textAlignment: Int) = Unit

    open fun getTextAlignment(): Int = TEXT_ALIGNMENT_CENTER

    open fun canResolveTextAlignment(): Boolean=true

    open fun isTextAlignmentResolved(): Boolean=true

    open fun findNamedViews(namedElements: Map<String, View>)=Unit

    open fun onResolvePointerIcon(event: MotionEvent, pointerIndex: Int): PointerIcon?=null

    open fun setPointerIcon(pointerIcon: PointerIcon?)=Unit

    open fun getPointerIcon(): PointerIcon?=null

    open fun hasPointerCapture(): Boolean = false

    open fun requestPointerCapture() = Unit

    open fun releasePointerCapture()=Unit

    open fun onPointerCaptureChange(hasCapture: Boolean)=Unit

    open fun dispatchPointerCaptureChanged(hasCapture: Boolean)=Unit

    open fun onCapturedPointerEvent(event: MotionEvent): Boolean=false

    open fun setOnCapturedPointerListener(l: OnCapturedPointerListener)=Unit

    open fun animate(): ViewPropertyAnimator=this.viewPropertyAnimator

    fun setTransitionName(transitionName: String?)=Unit

    fun getTransitionName(): String?=null

    open fun setTooltipText(tooltipText: CharSequence?)=Unit

    open fun getTooltipText(): CharSequence?=null

    open fun addOnUnhandledKeyEventListener(listener: OnUnhandledKeyEventListener)=Unit

    open fun removeOnUnhandledKeyEventListener(listener: OnUnhandledKeyEventListener)=Unit
}