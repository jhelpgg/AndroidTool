package android.os

class SystemProperties
{
   companion object
   {
   private val properties = HashMap<String, String>()

      @JvmStatic
      fun get(key:String) = this.properties[key]

      @JvmStatic
      fun get(key:String,defaultValue:String?) = this.properties[key] ?: defaultValue

      @JvmStatic
      fun getInt(key:String, defaultValue:Int) = this.properties[key]?.toInt() ?: defaultValue

      @JvmStatic
      fun getLong(key:String, defaultValue:Long) = this.properties[key]?.toLong() ?: defaultValue

      @JvmStatic
      fun getBoolean(key:String, defaultValue:Boolean) = this.properties[key]?.equals("TRUE", true) ?: defaultValue

      @JvmStatic
      fun set(key:String, value:String?) {
         if(value==null) {
         this.properties.remove(key)
         }
         else {
            this.properties[key] = value
         }
      }

      @JvmStatic
      fun set(key:String, value:Int?) =  SystemProperties.set(key,value?.toString())

      @JvmStatic
      fun set(key:String, value:Long?) =  SystemProperties.set(key,value?.toString())

      @JvmStatic
      fun set(key:String, value:Boolean?) =  SystemProperties.set(key,value?.toString())
   }
}