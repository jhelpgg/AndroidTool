package android.content

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import java.util.TreeSet

class SharedPreferencesForTests : SharedPreferences
{
   private val preferences = HashMap<String, Any>()
   private val listeners = ArrayList<OnSharedPreferenceChangeListener>()

   init
   {
      println(this.javaClass.name)
   }

   override fun contains(key: String?) = key in this.preferences

   override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener)
   {
      synchronized(this.listeners)
      {
         this.listeners.remove(listener)
      }
   }

   override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener)
   {
      synchronized(this.listeners)
      {
         if (listener !in this.listeners)
         {
            this.listeners += listener
         }
      }
   }

   private fun fireChange(keys: TreeSet<String>)
   {
      Thread {
         synchronized(this.listeners)
         {
            for (key in keys)
            {
               for (listener in this.listeners)
               {
                  listener.onSharedPreferenceChanged(this, key)
               }
            }
         }
      }.start()
   }

   override fun getBoolean(key: String, defValue: Boolean): Boolean
   {
      val value = this.preferences[key]

      return when (value)
      {
         null       -> defValue
         is Boolean -> value
         else       -> defValue
      }
   }

   override fun getInt(key: String, defValue: Int): Int
   {
      val value = this.preferences[key]

      return when (value)
      {
         null   -> defValue
         is Int -> value
         else   -> defValue
      }
   }

   override fun getLong(key: String, defValue: Long): Long
   {
      val value = this.preferences[key]

      return when (value)
      {
         null    -> defValue
         is Long -> value
         else    -> defValue
      }
   }

   override fun getFloat(key: String, defValue: Float): Float
   {
      val value = this.preferences[key]

      return when (value)
      {
         null     -> defValue
         is Float -> value
         else     -> defValue
      }
   }

   override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>?
   {
      val value = this.preferences[key]
         ?: return defValues

      return value as MutableSet<String>
   }

   override fun getAll() = HashMap(this.preferences)

   override fun edit() = EditorForTests(this)


   override fun getString(key: String, defValue: String?): String?
   {
      val value = this.preferences[key]

      return when (value)
      {
         null      -> defValue
         is String -> value
         else      -> defValue
      }
   }

   @Synchronized
   internal fun apply(editor: EditorForTests)
   {
      val keysChanged = TreeSet<String>()

      if (editor.clearFirst)
      {
         for (key in this.preferences.keys)
         {
            keysChanged += key
         }

         this.preferences.clear()
      }

      for (key in editor.toRemove)
      {
         keysChanged += key
         this.preferences.remove(key)
      }

      for ((key, value) in editor.operations)
      {
         keysChanged += key
         this.preferences[key] = value
      }

      if (keysChanged.isNotEmpty())
      {
         this.fireChange(keysChanged)
      }
   }
}