package android.os

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import android.util.SparseBooleanArray
import androidx.annotation.RequiresApi
import androidx.core.util.keyIterator
import jhelp.tests.ArrayBytes
import jhelp.tests.ClassLoaderObjectInputStream
import jhelp.tests.atLeastLolipop_21
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

private const val VAL_NULL = -1
private const val VAL_STRING = 0
private const val VAL_INTEGER = 1
private const val VAL_MAP = 2
private const val VAL_BUNDLE = 3
private const val VAL_PARCELABLE = 4
private const val VAL_SHORT = 5
private const val VAL_LONG = 6
private const val VAL_FLOAT = 7
private const val VAL_DOUBLE = 8
private const val VAL_BOOLEAN = 9
private const val VAL_CHARSEQUENCE = 10
private const val VAL_LIST = 11
private const val VAL_SPARSEARRAY = 12
private const val VAL_BYTEARRAY = 13
private const val VAL_STRINGARRAY = 14
private const val VAL_IBINDER = 15
private const val VAL_PARCELABLEARRAY = 16
private const val VAL_OBJECTARRAY = 17
private const val VAL_INTARRAY = 18
private const val VAL_LONGARRAY = 19
private const val VAL_BYTE = 20
private const val VAL_SERIALIZABLE = 21
private const val VAL_SPARSEBOOLEANARRAY = 22
private const val VAL_BOOLEANARRAY = 23
private const val VAL_CHARSEQUENCEARRAY = 24
private const val VAL_PERSISTABLEBUNDLE = 25
private const val VAL_SIZE = 26
private const val VAL_SIZEF = 27
private const val VAL_DOUBLEARRAY = 28
private val CREATORS = HashMap<String, Parcelable.Creator<Parcelable>>()


class Parcel private constructor(pointer: Long)
{
   companion object
   {
      @JvmStatic
      fun obtain() = Parcel(0)
   }

   private val arrayBytes = ArrayBytes()

   init
   {
      println(this.javaClass.name)
   }

   fun marshall() = this.arrayBytes.bytes

   fun unmarshall(array: ByteArray)
   {
      this.arrayBytes.clear()
      this.arrayBytes.write(array)
   }

   fun dataAvail() = this.arrayBytes.available

   fun dataPosition() = this.arrayBytes.read

   fun dataSize() = this.arrayBytes.size

   fun setDataPosition(poosition: Int) = this.arrayBytes.readPosition(poosition)

   private fun readHashMapInternal(
      hashMap: HashMap<Any?, Any?>,
      size: Int,
      classLoader: ClassLoader?
                                  )
   {
      for (count in 0 until size)
      {
         val key = this.readValue(classLoader)
         val value = this.readValue(classLoader)
         hashMap[key] = value
      }
   }

   private fun readListInternal(list: MutableList<Any?>, size: Int, classLoader: ClassLoader?)
   {
      for (count in 0 until size)
      {
         list.add(this.readValue(classLoader))
      }
   }

   fun readByte() = this.arrayBytes.read(1)[0]

   fun writeByte(byte: Byte)
   {
      this.arrayBytes.write(byteArrayOf(byte))
   }

   fun readBoolean() = this.readByte() == 1.toByte()

   fun writeBoolean(boolean: Boolean) = this.writeByte(if (boolean) 1.toByte() else 0.toByte())

   fun readShort(): Short
   {
      val data = this.arrayBytes.read(4)
      return (((data[2].toInt() and 0xFF) shl 8) or
           (data[3].toInt() and 0xFF)).toShort()
   }

   fun writeShort(short: Short)
   {
      val value = short.toInt() and 0xFFFF
      this.arrayBytes.write(byteArrayOf((value shr 8).toByte(), (value and 0xFF).toByte()))
   }

   fun readChar() = this.readShort().toChar()

   fun writeChar(char: Char) = this.writeShort(char.toShort())

   fun readInt(): Int
   {
      val data = this.arrayBytes.read(4)
      return ((data[0].toInt() and 0xFF) shl 24) or
           ((data[1].toInt() and 0xFF) shl 16) or
           ((data[2].toInt() and 0xFF) shl 8) or
           (data[3].toInt() and 0xFF)
   }

   fun writeInt(value: Int)
   {
      this.arrayBytes.write(
         byteArrayOf(
            (value shr 24).toByte(),
            ((value shr 16) and 0xFF).toByte(),
            ((value shr 8) and 0xFF).toByte(),
            (value and 0xFF).toByte()
                    )
                           )
   }

   fun readLong(): Long
   {
      val data = this.arrayBytes.read(8)
      return ((data[0].toLong() and 0xFF) shl 56) or
           ((data[1].toLong() and 0xFF) shl 48) or
           ((data[2].toLong() and 0xFF) shl 40) or
           ((data[3].toLong() and 0xFF) shl 32) or
           ((data[4].toLong() and 0xFF) shl 24) or
           ((data[5].toLong() and 0xFF) shl 16) or
           ((data[6].toLong() and 0xFF) shl 8) or
           (data[7].toLong() and 0xFF)
   }

   fun writeLong(value: Long)
   {
      this.arrayBytes.write(
         byteArrayOf(
            (value shr 56).toByte(),
            ((value shr 48) and 0xFF).toByte(),
            ((value shr 48) and 0xFF).toByte(),
            ((value shr 32) and 0xFF).toByte(),
            ((value shr 24) and 0xFF).toByte(),
            ((value shr 16) and 0xFF).toByte(),
            ((value shr 8) and 0xFF).toByte(),
            (value and 0xFF).toByte()
                    )
                           )
   }

   fun readFloat() = Float.fromBits(this.readInt())

   fun writeFloat(value: Float) = this.writeInt(value.toBits())

   fun readDouble() = Double.fromBits(this.readLong())

   fun writeDouble(value: Double) = this.writeLong(value.toBits())

   fun readString(): String?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      val utf8 = this.arrayBytes.read(size)
      return String(utf8, Charsets.UTF_8)
   }

   fun writeString(string: String?)
   {
      if (string == null)
      {
         this.writeInt(-1)
         return
      }

      val utf8 = string.toByteArray(Charsets.UTF_8)
      this.writeInt(utf8.size)
      this.arrayBytes.write(utf8)
   }

   private fun readCharSequence() =
      TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this)

   private fun writeCharSequence(charSequence: CharSequence?) =
      TextUtils.writeToParcel(charSequence, this, 0)

   @SuppressLint("NewApi")
   fun readValue(classLoader: ClassLoader?): Any?
   {
      val type = this.readInt()

      return when (type)
      {
         VAL_NULL               -> null
         VAL_STRING             -> this.readString()
         VAL_INTEGER            -> this.readInt()
         VAL_MAP                -> this.readHashMap(classLoader)
         VAL_PARCELABLE         -> this.readParcelable(classLoader)
         VAL_SHORT              -> this.readShort()
         VAL_LONG               -> this.readLong()
         VAL_FLOAT              -> this.readFloat()
         VAL_DOUBLE             -> this.readDouble()
         VAL_BOOLEAN            -> this.readBoolean()
         VAL_CHARSEQUENCE       -> this.readCharSequence()
         VAL_LIST               -> this.readArrayList(classLoader)
         VAL_BOOLEANARRAY       -> this.createBooleanArray()
         VAL_BYTEARRAY          -> this.createByteArray()
         VAL_STRINGARRAY        -> this.readStringArray()
         VAL_CHARSEQUENCEARRAY  -> this.readCharSequenceArray()
         VAL_IBINDER            -> null
         VAL_OBJECTARRAY        -> this.readArray(classLoader)
         VAL_INTARRAY           -> this.createIntArray()
         VAL_LONGARRAY          -> this.createLongArray()
         VAL_BYTE               -> this.readByte()
         VAL_SERIALIZABLE       -> this.readSerializable(classLoader)
         VAL_PARCELABLEARRAY    -> this.readParcelableArray(classLoader)
         VAL_SPARSEARRAY        -> this.readSparseArray<Any>(classLoader)
         VAL_SPARSEBOOLEANARRAY -> this.readSparseBooleanArray()
         VAL_BUNDLE             -> this.readBundle(classLoader)
         VAL_DOUBLEARRAY        -> this.createDoubleArray()
         else                   ->
            if (atLeastLolipop_21)
            {
               when (type)
               {
                  VAL_SIZE              -> this.readSize()
                  VAL_SIZEF             -> this.readSizeF()
                  VAL_PERSISTABLEBUNDLE -> this.readPersistableBundle(classLoader)
                  else                  -> throw RuntimeException("Unknown type : $type")
               }
            } else
            {
               throw RuntimeException("Unknown type : $type")
            }
      }
   }

   @SuppressLint("NewApi")
   fun writeValue(value: Any?)
   {
      when (value)
      {
         null                       -> this.writeInt(VAL_NULL)
         is String                  ->
         {
            this.writeInt(VAL_STRING)
            this.writeString(value)
         }
         is Int                     ->
         {
            this.writeInt(VAL_INTEGER)
            this.writeInt(value)
         }
         is Map<out Any?, out Any?> ->
         {
            this.writeInt(VAL_MAP)
            this.writeMap(value)
         }
         is Bundle                  ->
         {
            this.writeInt(VAL_BUNDLE)
            this.writeBundle(value)
         }
         is Parcelable              ->
         {
            this.writeInt(VAL_PARCELABLE)
            this.writeParcelable(value)
         }
         is Short                   ->
         {
            this.writeInt(VAL_SHORT)
            this.writeShort(value)
         }
         is Long                    ->
         {
            this.writeInt(VAL_LONG)
            this.writeLong(value)
         }
         is Float                   ->
         {
            this.writeInt(VAL_FLOAT)
            this.writeFloat(value)
         }
         is Double                  ->
         {
            this.writeInt(VAL_DOUBLE)
            this.writeDouble(value)
         }
         is Boolean                 ->
         {
            this.writeInt(VAL_BOOLEAN)
            this.writeBoolean(value)
         }
         is CharSequence            ->
         {
            this.writeInt(VAL_CHARSEQUENCE)
            this.writeCharSequence(value)
         }
         is List<out Any?>          ->
         {
            this.writeInt(VAL_LIST)
            this.writeList(value)
         }
         is SparseArray<out Any?>   ->
         {
            this.writeInt(VAL_SPARSEARRAY)
            this.writeSparseArray(value)
         }
         is BooleanArray            ->
         {
            this.writeInt(VAL_BOOLEANARRAY)
            this.writeBooleanArray(value)
         }
         is ByteArray               ->
         {
            this.writeInt(VAL_BYTEARRAY)
            this.writeByteArray(value)
         }
         is IntArray                ->
         {
            this.writeInt(VAL_INTARRAY)
            this.writeIntArray(value)
         }
         is LongArray               ->
         {
            this.writeInt(VAL_LONGARRAY)
            this.writeLongArray(value)
         }
         is DoubleArray             ->
         {
            this.writeInt(VAL_DOUBLE)
            this.writeDoubleArray(value)
         }
         is Byte                    ->
         {
            this.writeInt(VAL_BYTE)
            this.writeByte(value)
         }
         is IBinder                 -> this.writeInt(VAL_IBINDER)
         else                       ->
         {
            if (atLeastLolipop_21)
            {
               when (value)
               {
                  is Size              ->
                  {
                     this.writeInt(VAL_SIZE)
                     this.writeSize(value)
                     return
                  }
                  is SizeF             ->
                  {
                     this.writeInt(VAL_SIZEF)
                     this.writeSizeF(value)
                     return
                  }
                  is PersistableBundle ->
                  {
                     this.writeInt(VAL_PERSISTABLEBUNDLE)
                     this.writePersistableBundle(value)
                     return
                  }
               }
            }

            val valueClass = value.javaClass

            if (valueClass.isArray)
            {
               val componentType = valueClass.componentType!!

               when
               {
                  componentType == String::class.java                      ->
                  {
                     this.writeInt(VAL_STRINGARRAY)
                     this.writeStringArray(value as Array<String?>)
                  }
                  CharSequence::class.java.isAssignableFrom(componentType) ->
                  {
                     this.writeInt(VAL_CHARSEQUENCEARRAY)
                     this.writeCharSequenceArray(value as Array<CharSequence?>)
                  }
                  Parcelable::class.java.isAssignableFrom(componentType)   ->
                  {
                     this.writeInt(VAL_PARCELABLEARRAY)
                     this.writeParcelableArray(value as Array<out Parcelable?>)
                  }
                  value is Serializable                                    ->
                  {
                     this.writeInt(VAL_SERIALIZABLE)
                     this.writeSerializable(value)
                  }
               }
            }

            throw RuntimeException("Can't serialize : $value : ${value.javaClass}")
         }
      }
   }

   fun readHashMap(classLoader: ClassLoader?): HashMap<Any?, Any?>?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      val hashMap = HashMap<Any?, Any?>(size)
      this.readHashMapInternal(hashMap, size, classLoader)
      return hashMap
   }

   fun writeMap(map: Map<out Any?, out Any?>?)
   {
      if (map == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(map.size)

      for ((key, value) in map)
      {
         this.writeValue(key)
         this.writeValue(value)
      }
   }

   private fun <P : Parcelable> readParcelableCreator(classLoader: ClassLoader?): Parcelable.Creator<P>?
   {
      val name = this.readString()
         ?: return null

      return CREATORS.getOrPut(name, {
         val loader = classLoader
            ?: this.javaClass.classLoader
         val parcelableClass = Class.forName(name, true, loader)
         parcelableClass.getDeclaredField("CREATOR").get(null) as Parcelable.Creator<Parcelable>
      }) as Parcelable.Creator<P>
   }

   fun <P : Parcelable> readParcelable(classLoader: ClassLoader?): P?
   {
      val creator = this.readParcelableCreator<P>(classLoader)
         ?: return null
      return creator.createFromParcel(this)
   }

   fun <P : Parcelable> writeParcelable(parcelable: P?, flag: Int) = this.writeParcelable(parcelable)

   fun <P : Parcelable> writeParcelable(parcelable: P?)
   {
      if (parcelable == null)
      {
         this.writeString(null)
         return
      }

      this.writeString(parcelable.javaClass.name)
      parcelable.writeToParcel(this, 0)
   }

   fun readArrayList(classLoader: ClassLoader?): ArrayList<Any?>?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      val arrayList = ArrayList<Any?>()
      this.readListInternal(arrayList, size, classLoader)
      return arrayList
   }

   fun writeArrayList(list: ArrayList<out Any?>?)
   {
      if (list == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(list.size)

      for (element in list)
      {
         this.writeValue(element)
      }
   }

   fun writeList(list: List<out Any?>?)
   {
      if (list == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(list.size)

      for (element in list)
      {
         this.writeValue(element)
      }
   }

   private fun <T> readArray(readElement: () -> T?): Array<T?>?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      return Array<Any?>(size) { readElement() } as Array<T?>
   }

   private fun <T> writeArray(array: Array<T?>?, writeElement: (T?) -> Unit)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size)

      for (element in array)
      {
         writeElement(element)
      }
   }

   private fun <T> readArray(readElement: (ClassLoader?) -> T?, classLoader: ClassLoader?): Array<T?>?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      return Array<Any?>(size) { readElement(classLoader) } as Array<T?>
   }

   fun readArray(classLoader: ClassLoader?) = this.readArray(this::readValue, classLoader)

   fun writeArray(array: Array<out Any?>?) = this.writeArray(array, this::writeValue)

   fun createBooleanArray(): BooleanArray?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      return BooleanArray(size) { this.readBoolean() }
   }

   fun readBooleanArray(array: BooleanArray)
   {
      System.arraycopy(this.createBooleanArray()!!, 0, array, 0, array.size)
   }

   fun writeBooleanArray(array: BooleanArray?)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size)

      for (element in array)
      {
         writeBoolean(element)
      }
   }

   fun createByteArray(): ByteArray?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      return this.arrayBytes.read(size)
   }

   fun writeByteArray(array: ByteArray?)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size)
      this.arrayBytes.write(array)
   }

   fun readStringArray() = this.readArray(this::readString)

   fun writeStringArray(array: Array<String?>?) = this.writeArray(array, this::writeString)

   fun readCharSequenceArray() = this.readArray(this::readCharSequence)

   fun writeCharSequenceArray(array: Array<CharSequence?>?) = this.writeArray(array, this::writeCharSequence)

   fun createIntArray(): IntArray?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      return IntArray(size) { this.readInt() }
   }

   fun readIntArray(array: IntArray)
   {
      System.arraycopy(this.createIntArray()!!, 0, array, 0, array.size)
   }

   fun writeIntArray(array: IntArray?)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size)

      for (element in array)
      {
         writeInt(element)
      }
   }

   fun createLongArray(): LongArray?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      return LongArray(size) { this.readLong() }
   }

   fun readLongArray(array: LongArray)
   {
      System.arraycopy(this.createLongArray()!!, 0, array, 0, array.size)
   }

   fun writeLongArray(array: LongArray?)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size)

      for (element in array)
      {
         writeLong(element)
      }
   }

   fun createDoubleArray(): DoubleArray?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      return DoubleArray(size) { this.readDouble() }
   }

   fun readDoubleArray(array: DoubleArray)
   {
      System.arraycopy(this.createDoubleArray()!!, 0, array, 0, array.size)
   }

   fun writeDoubleArray(array: DoubleArray?)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size)

      for (element in array)
      {
         writeDouble(element)
      }
   }

   fun readSerializable() = this.readSerializable(null)

   private fun readSerializable(classLoader: ClassLoader?): Serializable?
   {
      val name = this.readString()
         ?: return null
      val serializedData = this.createByteArray()
         ?: return null
      val byteArrayInputStream = ByteArrayInputStream(serializedData)

      try
      {
         val objectInputStream = ClassLoaderObjectInputStream(byteArrayInputStream, classLoader)
         return objectInputStream.readObject() as Serializable
      } catch (exception: Exception)
      {
         throw RuntimeException("Can't deserialize the class '$name'", exception)
      }
   }

   fun writeSerializable(serializable: Serializable?)
   {
      if (serializable == null)
      {
         this.writeString(null)
         return
      }

      this.writeString(serializable.javaClass.name)

      try
      {
         val byteArrayOutputStream = ByteArrayOutputStream()
         val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
         objectOutputStream.writeObject(serializable)
         this.writeByteArray(byteArrayOutputStream.toByteArray())
      } catch (exception: Exception)
      {
         throw RuntimeException("Can't serialize the class '${serializable.javaClass.name}'", exception)
      }
   }

   fun readParcelableArray(classLoader: ClassLoader?) = this.readArray<Parcelable>(this::readParcelable, classLoader)

   fun writeParcelableArray(array: Array<out Parcelable?>?) = this.writeArray(array, this::writeParcelable)

   fun <T> readSparseArray(classLoader: ClassLoader?): SparseArray<T?>?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      val sparseArray = SparseArray<T?>()

      for (count in 0 until size)
      {
         val key = this.readInt()
         val value = this.readValue(classLoader) as T?
         sparseArray.put(key, value)
      }

      return sparseArray
   }

   fun <T> writeSparseArray(array: SparseArray<T?>?)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size())

      for (key in array.keyIterator())
      {
         this.writeInt(key)
         this.writeValue(array[key])
      }
   }

   fun readSparseBooleanArray(): SparseBooleanArray?
   {
      val size = this.readInt()

      if (size < 0)
      {
         return null
      }

      val sparseArray = SparseBooleanArray()

      for (count in 0 until size)
      {
         val key = this.readInt()
         val value = this.readBoolean()
         sparseArray.put(key, value)
      }

      return sparseArray
   }

   fun writeSparseBooleanArray(array: SparseBooleanArray?)
   {
      if (array == null)
      {
         this.writeInt(-1)
         return
      }

      this.writeInt(array.size())

      for (key in array.keyIterator())
      {
         this.writeInt(key)
         this.writeBoolean(array[key])
      }
   }

   fun readBundle(classLoader: ClassLoader?): Bundle?
   {
      val notNull = this.readBoolean()

      if (notNull)
      {
         return Bundle.CREATOR.createFromParcel(this)
      }

      return null
   }

   fun writeBundle(bundle: Bundle?)
   {
      if (bundle == null)
      {
         this.writeBoolean(false)
         return
      }

      this.writeBoolean(true)
      bundle.writeToParcel(this, 0)
   }

   @RequiresApi(21)
   fun readPersistableBundle(classLoader: ClassLoader?): PersistableBundle?
   {
      val notNull = this.readBoolean()

      if (notNull)
      {
         return PersistableBundle.CREATOR.createFromParcel(this)
      }

      return null
   }

   @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
   fun writePersistableBundle(bundle: PersistableBundle?)
   {
      if (bundle == null)
      {
         this.writeBoolean(false)
         return
      }

      this.writeBoolean(true)
      bundle.writeToParcel(this, 0)
   }

   @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
   fun readSize(): Size
   {
      val width = this.readInt()
      val height = this.readInt()
      return Size(width, height)
   }

   @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
   fun writeSize(size: Size)
   {
      this.writeInt(size.width)
      this.writeInt(size.height)
   }

   @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
   fun readSizeF(): SizeF
   {
      val width = this.readFloat()
      val height = this.readFloat()
      return SizeF(width, height)
   }

   @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
   fun writeSizeF(size: SizeF)
   {
      this.writeFloat(size.width)
      this.writeFloat(size.height)
   }
}