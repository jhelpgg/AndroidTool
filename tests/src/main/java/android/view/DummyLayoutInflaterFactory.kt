package android.view

import android.content.Context
import android.util.AttributeSet
import jhelp.tests.theApplication

object DummyLayoutInflaterFactory : LayoutInflater.Factory2
{
   override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet) = DummyView(context)

   override fun onCreateView(name: String, context: Context, attrs: AttributeSet) = DummyView(context)
}