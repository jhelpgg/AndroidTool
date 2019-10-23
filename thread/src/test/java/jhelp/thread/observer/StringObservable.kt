package jhelp.thread.observer

class StringObservable : Observable<StringObservable>()
{
   var value = ""
      private set

   override fun copy(): StringObservable
   {
      val copy = StringObservable()
      copy.value = this.value
      return copy
   }

   fun value(value: String)
   {
      if (this.value != value)
      {
         this.value = value
         this.fireChange()
      }
   }
}