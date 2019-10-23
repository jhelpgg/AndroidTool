package jhelp.tests

import java.lang.reflect.Field
import java.lang.reflect.Method


private fun obtainField(clazz: Class<*>, fieldName: String): Field
{
   val className = clazz.name
   var clazz: Class<*>? = clazz
   var field: Field? = null
   // Internal field in Kotlin are named at compilation with a dollar follow by something.
   // So to be able to find those fields we have to check if the name starts with given name follow by $
   val fieldNameDollar = "$fieldName$"

   while (clazz != null && field == null)
   {
      try
      {
         field = clazz.declaredFields.firstOrNull { it.name == fieldName || it.name.startsWith(fieldNameDollar) }
      }
      catch (_: Exception)
      {
         field = null
      }

      clazz = clazz.superclass
   }

   requireNotNull(field) { "$fieldName not found in $className" }
   field.isAccessible = true
   return field
}

fun <C : Any, F> getClassField(instance: C, fieldName: String): F
{
   try
   {
      val field = obtainField(instance.javaClass, fieldName)
      return field.get(instance) as F
   }
   catch (e: Exception)
   {
      throw RuntimeException(e)
   }

}

fun <C, F> getClassStaticField(clazz: Class<C>, fieldName: String): F
{
   try
   {
      val field = obtainField(clazz, fieldName)
      return field.get(null) as F
   }
   catch (e: Exception)
   {
      throw RuntimeException(e)
   }

}

fun <C : Any, F> setClassField(instance: C, fieldName: String, value: F)
{
   try
   {
      val field = obtainField(instance.javaClass, fieldName)
      field.set(instance, value)
   }
   catch (e: Exception)
   {
      throw RuntimeException(e)
   }

}

fun <C, F> setClassStaticField(clazz: Class<C>, fieldName: String, value: F)
{
   try
   {
      val field = obtainField(clazz, fieldName)
      field.set(null, value)
   }
   catch (e: Exception)
   {
      throw RuntimeException(e)
   }

}

private fun toString(parametersType: Array<Class<*>?>): String
{
   val stringBuilder = StringBuilder()

   if (parametersType.isNotEmpty())
   {
      stringBuilder.append(parametersType[0]?.name)
      val length = parametersType.size

      for (index in 1 until length)
      {
         stringBuilder.append(", ")
         stringBuilder.append(parametersType[index]?.name)
      }
   }

   return stringBuilder.toString()
}

private fun primitiveCompatibleWith(primitiveClass: Class<*>, toCompareWith: Class<*>) =
   when (primitiveClass.name)
   {
      "boolean" -> toCompareWith == Boolean::class.java || toCompareWith.name == "java.lang.Boolean"
      "char"    -> toCompareWith == Char::class.java || toCompareWith.name == "java.lang.Character"
      "byte"    -> toCompareWith == Byte::class.java || toCompareWith.name == "java.lang.Byte"
      "short"   -> toCompareWith == Short::class.java || toCompareWith.name == "java.lang.Short"
      "int"     -> toCompareWith == Int::class.java || toCompareWith.name == "java.lang.Integer"
      "long"    -> toCompareWith == Long::class.java || toCompareWith.name == "java.lang.Long"
      "float"   -> toCompareWith == Float::class.java || toCompareWith.name == "java.lang.Float"
      "double"  -> toCompareWith == Double::class.java || toCompareWith.name == "java.lang.Double"
      else      -> false
   }

private fun compatibleTypes(reference: Class<*>?, toTest: Class<*>?): Boolean =
   when
   {
      reference == null                                         -> toTest == null
      toTest == null                                            -> !reference.isPrimitive
      reference.isArray && toTest.isArray                       -> compatibleTypes(
         reference.componentType,
         toTest.componentType
                                                                                  )
      reference == toTest || reference.isAssignableFrom(toTest) -> true
      reference.isPrimitive                                     -> primitiveCompatibleWith(reference, toTest)
      toTest.isPrimitive                                        -> primitiveCompatibleWith(toTest, reference)
      else                                                      -> false
   }

private fun compatibleTypes(references: Array<Class<*>?>?, toTests: Array<Class<*>?>?): Boolean
{
   if (references == null || references.isEmpty())
   {
      return toTests == null || toTests.isEmpty()
   }

   if (toTests == null || toTests.isEmpty())
   {
      return false
   }

   val length = references.size

   if (length != toTests.size)
   {
      return false
   }

   for (index in 0 until length)
   {
      if (!compatibleTypes(references[index], toTests[index]))
      {
         return false
      }
   }

   return true
}

private fun extractTypes(parameters: Array<Any?>?): Array<Class<*>?>
{
   if (parameters == null)
   {
      return arrayOfNulls(0)
   }

   val length = parameters.size
   val classes = arrayOfNulls<Class<*>>(length)

   for (index in 0 until length)
   {
      if (parameters[index] != null)
      {
         classes[index] = parameters[index]!!.javaClass
      }
   }

   return classes
}

private fun obtainMethod(
   clazz: Class<*>, methodName: String,
   parametersType: Array<Class<*>?>
                        ): Method
{
   var method: Method? = null
   var classToLook: Class<*>? = clazz
   // Internal method in Kotlin are named at compilation with a dollar follow by something.
   // So to be able to find those methods we have to check if the name starts with given name follow by $
   val methodNameDollar = "$methodName$"

   while (classToLook != null && method == null)
   {
      try
      {
         for (methodTest in classToLook.declaredMethods)
         {
            if ((methodName == methodTest.name || methodTest.name.startsWith(methodNameDollar))
               && compatibleTypes(methodTest.parameterTypes, parametersType)
            )
            {
               method = methodTest
               break
            }
         }
      }
      catch (ignored: Exception)
      {
         method = null
      }

      classToLook = classToLook.superclass
   }

   requireNotNull(method) { "$methodName(${toString(parametersType)}) not found in ${clazz.name}" }
   method.isAccessible = true
   return method
}

fun <C : Any, R> invokeMethod(instance: C, methodName: String, vararg parameters: Any?): R
{
   try
   {
      val method = obtainMethod(instance.javaClass, methodName, extractTypes(parameters as Array<Any?>?))
      return method.invoke(instance, *parameters) as R
   }
   catch (e: Exception)
   {
      throw RuntimeException(e)
   }
}

fun <C, R> invokeStaticMethod(clazz: Class<C>, methodName: String, vararg parameters: Any?): R
{
   try
   {
      val method = obtainMethod(clazz, methodName, extractTypes(parameters as Array<Any?>?))
      return method.invoke(null, *parameters) as R
   }
   catch (e: Exception)
   {
      throw RuntimeException(e)
   }
}