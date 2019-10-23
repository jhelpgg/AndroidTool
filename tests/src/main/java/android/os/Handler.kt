package android.os

private fun obtainLooper(): Looper
{
   val looper = Looper.myLooper()

   if (looper != null)
   {
      return looper
   }

   Looper.prepare()
   return Looper.myLooper()!!
}

open class Handler(val looper: Looper, val callBack: Callback?)
{
   interface Callback
   {
      fun handleMessage(message: Message): Boolean
   }

   companion object
   {
      @JvmStatic
      fun createAsync(looper: Looper) = Handler(looper)

      @JvmStatic
      fun createAsync(looper: Looper, callback: Callback?) =
         Handler(looper, callback)
   }

   init
   {
      println(this.javaClass.name)
   }

   constructor() : this(obtainLooper(), null)

   constructor(callback: Callback?) : this(obtainLooper(), callback)

   constructor(looper: Looper) : this(looper, null)

   fun post(runnable: Runnable): Boolean
   {
      this.sendMessage(Message.obtain(this, runnable))
      return true
   }

   fun postAtFrontOfQueue(runnable: Runnable) = this.post(runnable)

   fun runWithScissors(runnable: Runnable, timeout:Long) = this.post(runnable)

   fun postAtTime(runnable: Runnable, uptimeMillis:Long)= this.post(runnable)

   fun postAtTime(runnable: Runnable, token:Any?, uptimeMillis:Long) :Boolean{
      val message = Message.obtain(this, runnable)
      message.obj = token
      this.sendMessage(message)
      return true
   }

   fun postDelayed(runnable: Runnable, delay:Long)= this.post(runnable)

   fun postDelayed(runnable: Runnable, token:Any?, delay:Long) :Boolean{
      val message = Message.obtain(this, runnable)
      message.obj = token
      this.sendMessage(message)
      return true
   }

   fun removeCallbacks(callback:Runnable) = Unit

   fun removeCallbacks(callback:Runnable, token:Any?) = Unit

   fun getMessageName(message: Message) =
      message.callback?.javaClass?.name
         ?: "0x${Integer.toHexString(message.what)}"

   fun sendMessage(message: Message) : Boolean
   {
      this.handleMessage(message)
      this.callBack?.handleMessage(message)
      message.callback?.run()
      return true
   }

   fun sendEmptyMessage(what:Int) = this.sendMessage(this.obtainMessage(what))

   fun sendEmptyMessageDelayed(what:Int, delay:Long) = this.sendMessage(this.obtainMessage(what))

   fun sendEmptyMessageAtTime(what:Int, time:Long) = this.sendMessage(this.obtainMessage(what))

   fun sendMessageDelayed(message:Message, delay:Long) = this.sendMessage(message)

   fun sendMessageAtTime(message:Message, time:Long) = this.sendMessage(message)

   fun sendMessageAtFrontOfQueue(message: Message) = this.sendMessage(message)

   fun obtainMessage() = Message.obtain(this)

   fun obtainMessage(what:Int) = Message.obtain(this, what)

   fun obtainMessage(what:Int, obj:Any) = Message.obtain(this, what, obj)

   fun obtainMessage(what:Int, arg1:Int, arg2:Int) = Message.obtain(this, what, arg1,arg2)

   fun obtainMessage(what:Int, arg1:Int, arg2:Int, obj:Any) = Message.obtain(this, what, arg1,arg2,obj)

   open fun handleMessage(message: Message)=Unit

   fun removeMessages(what:Int) = Unit

   fun removeMessages(what:Int,obj:Any) = Unit

   fun removeCallbacksAndMessages(token:Any) = Unit

   fun hasMessages(what:Int) = false

   fun hasMessages(what:Int,obj:Any) = false

   fun hasCallbacks(callback:Runnable) = false
}