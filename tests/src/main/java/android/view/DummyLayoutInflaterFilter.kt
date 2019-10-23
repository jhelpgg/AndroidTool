package android.view

object DummyLayoutInflaterFilter : LayoutInflater.Filter
{
   override fun onLoadClass(clazz: Class<*>): Boolean = true
}