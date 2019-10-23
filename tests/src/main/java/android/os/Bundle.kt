package android.os

import android.os.Parcelable.Creator
import android.util.ArrayMap
import android.util.SparseArray
import java.io.Serializable

class Bundle(private var classLoader: ClassLoader?) : Parcelable
{
   companion object CREATOR : Creator<Bundle>
   {
      @JvmStatic
      val EMPTY = Bundle()

      override fun createFromParcel(source: Parcel): Bundle
      {
         val bundle = Bundle()
         val size = source.readInt()

         for (count in 0 until size)
         {
            val key = source.readString()!!
            val value = source.readValue(null)!!
            bundle.bundle.put(key, value)
         }

         return bundle
      }

      override fun newArray(size: Int): Array<Bundle?>
      {
         return arrayOfNulls(size)
      }
   }

   private val bundle = HashMap<String, Any>()

   init
   {
      println(this.javaClass.name)
   }

   constructor() : this(null)

   constructor(capacity: Int) : this(null)

   constructor(bundle: Bundle) : this(null)
   {
      this.bundle.putAll(bundle.bundle)
   }

   fun size() = this.bundle.size

   fun isEmpty() = this.bundle.isEmpty()

   fun clear()
   {
      this.bundle.clear()
   }

   fun containsKey(key: String) = this.bundle.containsKey(key)

   operator fun get(key: String) = this.bundle[key]

   fun remove(key: String)
   {
      this.bundle.remove(key)
   }

   fun putAll(map: ArrayMap<String, Any>)
   {
      this.bundle.putAll(map)
   }

   fun keySet() = this.bundle.keys

   fun putBoolean(key: String, value: Boolean)
   {
      this.bundle[key] = value
   }

   fun getBoolean(key: String, defaultValue: Boolean) = this.bundle[key] as? Boolean
      ?: defaultValue

   fun getBoolean(key: String) = this.getBoolean(key, false)

   fun putByte(key: String, value: Byte)
   {
      this.bundle[key] = value
   }

   fun getByte(key: String, defaultValue: Byte) = this.bundle[key] as? Byte
      ?: defaultValue

   fun getByte(key: String) = this.getByte(key, 0)

   fun putChar(key: String, value: Char)
   {
      this.bundle[key] = value
   }

   fun getChar(key: String, defaultValue: Char) = this.bundle[key] as? Char
      ?: defaultValue

   fun getChar(key: String) = this.getChar(key, 0.toChar())

   fun putShort(key: String, value: Short)
   {
      this.bundle[key] = value
   }

   fun getShort(key: String, defaultValue: Short) = this.bundle[key] as? Short
      ?: defaultValue

   fun getShort(key: String) = this.getShort(key, 0)

   fun putInt(key: String, value: Int)
   {
      this.bundle[key] = value
   }

   fun getInt(key: String, defaultValue: Int) = this.bundle[key] as? Int
      ?: defaultValue

   fun getInt(key: String) = this.getInt(key, 0)

   fun putLong(key: String, value: Long)
   {
      this.bundle[key] = value
   }

   fun getLong(key: String, defaultValue: Long) = this.bundle[key] as? Long
      ?: defaultValue

   fun getLong(key: String) = this.getLong(key, 0)

   fun putFloat(key: String, value: Float)
   {
      this.bundle[key] = value
   }

   fun getFloat(key: String, defaultValue: Float) = this.bundle[key] as? Float
      ?: defaultValue

   fun getFloat(key: String) = this.getFloat(key, 0f)

   fun putDouble(key: String, value: Double)
   {
      this.bundle[key] = value
   }

   fun getDouble(key: String, defaultValue: Double) = this.bundle[key] as? Double
      ?: defaultValue

   fun getDouble(key: String) = this.getDouble(key, 0.0)

   fun putString(key: String, value: String)
   {
      this.bundle[key] = value
   }

   fun getString(key: String, defaultValue: String) = this.bundle[key] as? String
      ?: defaultValue

   fun getString(key: String) = this.getString(key, "")

   fun putCharSequence(key: String, value: CharSequence)
   {
      this.bundle[key] = value
   }

   fun getCharSequence(key: String, defaultValue: CharSequence) = this.bundle[key] as? CharSequence
      ?: defaultValue

   fun getCharSequence(key: String) = this.getCharSequence(key, "")

   fun putIntegerArrayList(key: String, value: ArrayList<Int>)
   {
      this.bundle[key] = value
   }

   fun getIntegerArrayList(key: String) = this.bundle[key] as ArrayList<Int>?

   fun putStringArrayList(key: String, value: ArrayList<String>)
   {
      this.bundle[key] = value
   }

   fun getStringArrayList(key: String) = this.bundle[key] as ArrayList<String>?

   fun putCharSequenceArrayList(key: String, value: ArrayList<CharSequence>)
   {
      this.bundle[key] = value
   }

   fun getCharSequenceArrayList(key: String) = this.bundle[key] as ArrayList<CharSequence>?

   fun putSerializable(key: String, value: Serializable)
   {
      this.bundle[key] = value
   }

   fun getSerializable(key: String) = this.bundle[key] as Serializable?

   fun putBooleanArray(key: String, value: BooleanArray)
   {
      this.bundle[key] = value
   }

   fun getBooleanArray(key: String) = this.bundle[key] as BooleanArray?

   fun putByteArray(key: String, value: ByteArray)
   {
      this.bundle[key] = value
   }

   fun getByteArray(key: String) = this.bundle[key] as ByteArray?

   fun putShortArray(key: String, value: ShortArray)
   {
      this.bundle[key] = value
   }

   fun getShortArray(key: String) = this.bundle[key] as ShortArray?

   fun putCharArray(key: String, value: CharArray)
   {
      this.bundle[key] = value
   }

   fun getCharArray(key: String) = this.bundle[key] as CharArray?

   fun putIntArray(key: String, value: IntArray)
   {
      this.bundle[key] = value
   }

   fun getIntArray(key: String) = this.bundle[key] as IntArray?

   fun putLongArray(key: String, value: LongArray)
   {
      this.bundle[key] = value
   }

   fun getLongArray(key: String) = this.bundle[key] as LongArray?

   fun putFloatArray(key: String, value: FloatArray)
   {
      this.bundle[key] = value
   }

   fun getFloatArray(key: String) = this.bundle[key] as FloatArray?

   fun putDoubleArray(key: String, value: DoubleArray)
   {
      this.bundle[key] = value
   }

   fun getDoublenArray(key: String) = this.bundle[key] as DoubleArray?

   fun putStringArray(key: String, value: Array<String>)
   {
      this.bundle[key] = value
   }

   fun getStringArray(key: String) = this.bundle[key] as Array<String>?

   fun putCharSequenceArray(key: String, value: Array<CharSequence>)
   {
      this.bundle[key] = value
   }

   fun getCharSequenceArray(key: String) = this.bundle[key] as Array<CharSequence>?

   fun setClassLoader(classLoader: ClassLoader?)
   {
      this.classLoader = classLoader
   }

   fun getClassLoader() = this.classLoader

   fun clone() = Bundle(this)

   fun deepCopy() = Bundle(this)

   fun putAll(bundle: Bundle)
   {
      this.bundle.putAll(bundle.bundle)
   }

   fun hasFileDescriptors() = false

   fun putParcelable(key: String, value: Parcelable)
   {
      this.bundle[key] = value
   }

   fun <P : Parcelable> getParcelable(key: String) = this.bundle[key] as P?

   fun putParcelableArray(key: String, value: Array<out Parcelable>)
   {
      this.bundle[key] = value
   }

   fun getParcelableArray(key: String) = this.bundle[key] as Array<out Parcelable>?

   fun putParcelableArrayList(key: String, value: ArrayList<out Parcelable>)
   {
      this.bundle[key] = value
   }

   fun getParcelableArrayList(key: String) = this.bundle[key] as ArrayList<out Parcelable>?

   fun putParcelableList(key: String, value: List<out Parcelable>)
   {
      this.bundle[key] = value
   }

   fun getParcelableList(key: String) = this.bundle[key] as List<out Parcelable>?

   fun putSparseParcelableArray(key: String, value: SparseArray<out Parcelable>)
   {
      this.bundle[key] = value
   }

   fun getSparseParcelableArray(key: String) = this.bundle[key] as SparseArray<out Parcelable>?

   fun putBundle(key: String, value: Bundle)
   {
      this.bundle[key] = value
   }

   fun getBundle(key: String) = this.bundle[key] as Bundle?


   fun putBinder(key: String, value: IBinder)
   {
      this.bundle[key] = value
   }

   fun getBInder(key: String) = this.bundle[key] as IBinder?

   override fun writeToParcel(destination: Parcel, flags: Int)
   {
      destination.writeInt(this.bundle.size)

      for ((key, value) in this.bundle)
      {
         destination.writeString(key)
         destination.writeValue(value)
      }
   }

   override fun describeContents() = 0
}