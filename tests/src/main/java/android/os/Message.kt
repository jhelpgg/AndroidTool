package android.os

class Message
{
   companion object {
      @JvmStatic
      fun obtain() = Message()

      @JvmStatic
      fun obtain(origin:Message) : Message {
         val message = Message()
         message.what = origin.what
         message.arg1 = origin.arg1
         message.arg2 = origin.arg2
         message.obj = origin.obj
         message.replyTo = origin.replyTo
         message.target = origin.target
         message.callback = origin.callback
         return message
      }

      @JvmStatic
      fun obtain(handler:Handler) : Message {
         val message = Message()
         message.target = handler
         return message
      }

      @JvmStatic
      fun obtain(handler:Handler, callback:Runnable) : Message {
         val message = Message()
         message.target = handler
         message.callback = callback
         return message
      }

      @JvmStatic
      fun obtain(handler:Handler, what:Int) : Message {
         val message = Message()
         message.target = handler
         message.what = what
         return message
      }

      @JvmStatic
      fun obtain(handler:Handler, what:Int, obj:Any) : Message {
         val message = Message()
         message.target = handler
         message.what = what
         message.obj = obj
         return message
      }

      @JvmStatic
      fun obtain(handler:Handler, what:Int, arg1:Int,arg2:Int) : Message {
         val message = Message()
         message.target = handler
         message.what = what
         message.arg1 = arg1
         message.arg2 = arg2
         return message
      }

      @JvmStatic
      fun obtain(handler:Handler, what:Int, arg1:Int,arg2:Int, obj:Any) : Message {
         val message = Message()
         message.target = handler
         message.what = what
         message.arg1 = arg1
         message.arg2 = arg2
         message.obj = obj
         return message
      }
   }

   init
   {
      println(this.javaClass.name)
   }

   var what = 0
   var arg1 = 0
   var arg2 = 0
   var obj : Any? = null
   var replyTo : Messenger? = null
   internal var target : Handler? = null
   internal var callback : Runnable? = null

   fun recycle() = Unit

   fun copyFrom(message:Message) {
      this.what = message.what
      this.arg1 = message.arg1
      this.arg2 = message.arg2
      this.obj = message.obj
      this.replyTo = message.replyTo
      this.target = message.target
      this.callback = message.callback
   }

   fun getWhen() = 0L

   fun setTarget(target:Handler) {this.target=target}

   fun getTarget() = this.target

   fun getCallback() = this.callback
}