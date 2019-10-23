package android.view

import android.content.Context
import android.util.AttributeSet
import org.xmlpull.v1.XmlPullParser

abstract class LayoutInflater protected constructor(private val context: Context)
{
   companion object
   {
      @JvmStatic
      fun from(context: Context): LayoutInflater = LayoutInflaterForTests(context)
   }

   interface Filter
   {
      fun onLoadClass(clazz: Class<*>): Boolean
   }

   interface Factory
   {
      fun onCreateView(name: String, context: Context, attrs: AttributeSet): View?
   }

   interface Factory2 : Factory
   {
      fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View?
   }

   private var factory2: Factory2 = DummyLayoutInflaterFactory
   private var factory: Factory = this.factory2
   private var filter: Filter = DummyLayoutInflaterFilter

   abstract fun cloneInContext(newContext: Context): LayoutInflater

   fun getContext() = this.context

   fun getFactory() = this.factory

   fun getFactory2() = this.factory2

   fun setFactory(factory: Factory)
   {
      this.factory = factory

      if (factory is Factory2)
      {
         this.factory2 = factory
      }
   }

   fun setFactory2(factory: Factory2)
   {
      this.factory = factory
      this.factory2 = factory
   }

   fun getFilter() = this.filter

   fun setFilter(filter: Filter)
   {
      this.filter = filter
   }

   fun inflate(resource: Int, root: ViewGroup?): View = DummyView(this.context)

   fun inflate(parser: XmlPullParser, root: ViewGroup?): View = DummyView(this.context)

   fun inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean): View = DummyView(this.context)

   fun inflate(parser: XmlPullParser, root: ViewGroup?, attachToRoot: Boolean): View = DummyView(this.context)

   fun createView(name: String, prefix: String?, attrs: AttributeSet?): View = DummyView(this.context)

   fun createView(viewContext: Context, name: String, prefix: String?, attrs: AttributeSet?): View =
      DummyView(viewContext)

   protected open fun onCreateView(name: String, attrs: AttributeSet?): View = DummyView(this.context)

   protected open fun onCreateView(parent: View?, name: String, attrs: AttributeSet?): View = DummyView(this.context)

   fun onCreateView(viewContext: Context, parent: View?, name: String, attrs: AttributeSet?): View =
      DummyView(viewContext)
}