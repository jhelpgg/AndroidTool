package android.os

class Messenger(val target:Handler)
{
   init
   {
      println(this.javaClass.name)
   }

   fun sendMessage(message:Message) {
      this.target.sendMessage(message)
   }
}