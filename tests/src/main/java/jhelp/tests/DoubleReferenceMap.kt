package jhelp.tests

class DoubleReferenceMap<K1, K2>
{
   private val firstSecond = HashMap<K1, K2>()
   private val secondFirst = HashMap<K2, K1>()

   fun associate(key1: K1, key2: K2)
   {
      this.firstSecond[key1] = key2
      this.secondFirst[key2] = key1
   }

   fun get1(key1: K1) = this.firstSecond[key1]

   fun get2(key2: K2) = this.secondFirst[key2]
}