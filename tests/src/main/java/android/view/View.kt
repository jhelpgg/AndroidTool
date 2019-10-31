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

   private var defStyleRes = 0
   private var id = NO_ID
   private var verticalFadingEdgeLength = 0
   private var hoorizontalFadingEdgeLength = 0
   protected var mParent:ViewParent?=null
   protected var mLayoutParams: ViewGroup.LayoutParams? = null

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


}