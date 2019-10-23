package android.content

import android.content.SharedPreferences.Editor

class EditorForTests internal constructor(val shreadPreferences: SharedPreferencesForTests) : SharedPreferences.Editor
{
   internal var clearFirst = false
   internal val toRemove = ArrayList<String>()
   internal val operations = HashMap<String, Any>()

   init
   {
      println(this.javaClass.name)
   }

   override fun clear(): Editor
   {
      this.clearFirst = true
      return this
   }

   override fun putLong(key: String, value: Long): Editor
   {
      this.toRemove.remove(key)
      this.operations[key] = value
      return this
   }

   override fun putInt(key: String, value: Int): Editor
   {
      this.toRemove.remove(key)
      this.operations[key] = value
      return this
   }

   override fun remove(key: String): Editor
   {
      this.toRemove += key
      return this
   }

   override fun putBoolean(key: String, value: Boolean): Editor
   {
      this.toRemove.remove(key)
      this.operations[key] = value
      return this
   }

   override fun putStringSet(key: String, values: MutableSet<String>?): Editor
   {
      if (values == null)
      {
         toRemove += key
      }
      else
      {
         this.toRemove.remove(key)
         this.operations[key] = values
      }

      return this
   }

   override fun commit(): Boolean
   {
      this.shreadPreferences.apply(this)
      return true
   }

   override fun putFloat(key: String, value: Float): Editor
   {
      this.toRemove.remove(key)
      this.operations[key] = value
      return this
   }

   override fun apply()
   {
      this.shreadPreferences.apply(this)
   }

   override fun putString(key: String, value: String?): Editor
   {
      if (value == null)
      {
         toRemove += key
      }
      else
      {
         this.toRemove.remove(key)
         this.operations[key] = value
      }

      return this
   }
}