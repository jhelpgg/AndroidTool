package jhelp.tests

import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectStreamClass

class ClassLoaderObjectInputStream(inputStream: InputStream, private val classLoader: ClassLoader?)
   : ObjectInputStream(inputStream)
{
   override fun resolveClass(objectStreamClass: ObjectStreamClass): Class<*> =
        if (this.classLoader == null)
        {
           super.resolveClass(objectStreamClass)
        }
        else
        {
           Class.forName(objectStreamClass.name, true, this.classLoader)
        }
}