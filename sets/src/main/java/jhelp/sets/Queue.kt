package jhelp.sets

class Queue<T>
{
   private var head: QueueElement<T>? = null
   private var tail: QueueElement<T>? = null

   val empty get() = this.head == null

   val notEmpty get() = this.head != null

   fun inqueue(element: T)
   {
      if (this.head == null)
      {
         this.head = QueueElement(element)
         this.tail = this.head
         return
      }

      val tail = this.tail!!
      tail.next = QueueElement(element)
      this.tail = tail.next
   }

   fun outqueue(): T
   {
      val head = this.head
         ?: throw IllegalStateException("The queue is empty")
      val element = head.element
      this.head = head.next

      if (this.head == null)
      {
         this.tail = null
      }

      return element
   }

   fun peek() =
      this.head?.element
         ?: throw IllegalStateException("The queue is empty")
}