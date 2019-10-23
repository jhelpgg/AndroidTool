package jhelp.tests.manifest


object AccessR
{
   private val classString: Class<*>
   private val classColor: Class<*>
   private val classLayout: Class<*>
   private val classId: Class<*>

   init
   {
      val packageName = AndroidManifest.packageName
      val classRname = "$packageName.R"
      this.classString = Class.forName("$classRname\$string")
      this.classColor = Class.forName("$classRname\$color")
      this.classLayout = Class.forName("$classRname\$layout")
      this.classId = Class.forName("$classRname\$id")
   }

   fun obtainStringReference(name: String) =
      this.classString.getDeclaredField(name).getInt(null)

   fun obtainColorReference(name: String) =
      this.classColor.getDeclaredField(name).getInt(null)

   fun obtainLayoutReference(name: String) =
      this.classLayout.getDeclaredField(name).getInt(null)

   fun obtainIdReference(name: String) =
      this.classId.getDeclaredField(name).getInt(null)
}