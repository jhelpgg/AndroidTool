package android.content

import android.os.Parcel

data class ComponentName(val packageName: String, val className: String)
{
   constructor(context: Context, clazz: Class<*>) : this(context.getPackageName(), clazz.name)

   constructor(context: Context, className: String) : this(context.getPackageName(), className)

   constructor(parcel: Parcel) :
        this(
           parcel.readString()
              ?: throw NullPointerException("package name is null"),
           parcel.readString()
              ?: throw NullPointerException("class name is null")
            )

   // TODO methods
}

fun ComponentName.obtainClass(): Class<*>?
{
   val className = if (this.className.startsWith('.'))
   {
      "${this.packageName}${this.className}"
   }
   else
   {
      this.className
   }

   return try
   {
      Class.forName(className)
   }
   catch (_: Exception)
   {
      null
   }
}