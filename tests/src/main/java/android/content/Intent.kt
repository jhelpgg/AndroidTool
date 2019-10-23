package android.content

import android.net.Uri
import android.os.Bundle
import jhelp.tests.theApplication
import java.util.TreeSet

class Intent(var action: String?, var uri: Uri?, packageContext: Context?, clazz: Class<*>?)
{
   var component = if (packageContext != null && clazz != null) ComponentName(packageContext, clazz) else null
private val categories = TreeSet<String>()
   var selector : Intent? = null
   var clipData:ClipData?=null
   private val extras = Bundle()

   constructor(action: String, uri: Uri) : this(action, uri, null, null)

   constructor(action: String) : this(action, null, null, null)

   constructor(intent: Intent) : this(intent.action, intent.uri, theApplication,
                                      intent.component?.className?.let { Class.forName(it) })
   {
      this.categories.addAll(intent.categories)
      this.selector=intent.selector
      this.clipData=intent.clipData
      this.extras.putAll(intent.extras)
   }

   constructor( packageContext: Context, clazz: Class<*>) : this(null,null, packageContext, clazz)

   constructor() : this(null,null,null,null)

   fun addCategory(category:String) : Intent
   {
      this.categories.add(category)
      return this
   }

   fun removeCategory(category:String){
      this.categories.remove(category)
   }

   fun putExtra(key:String, value:Boolean) : Intent
   {
      this.extras.putBoolean(key,value)
      return this
   }

   fun putExtra(key:String, value:Byte) : Intent
   {
      this.extras.putByte(key,value)
      return this
   }

   // TODO other methods
}