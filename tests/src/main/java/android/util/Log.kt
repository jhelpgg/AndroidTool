package android.util

class Log
{
   companion object
   {
      @JvmStatic
      val VERBOSE = 2

      @JvmStatic
      val DEBUG = 3

      @JvmStatic
      val INFO = 4

      @JvmStatic
      val WARN = 5

      @JvmStatic
      val ERROR = 6

      @JvmStatic
      val ASSERT = 7

      @JvmStatic
      fun v(tag: String, message: String) =
         v(tag, message, null)

      @JvmStatic
      fun v(tag: String, message: String, throwable: Throwable?): Int
      {
         log("VERBOSE", tag, message, throwable)
         return 0
      }

      @JvmStatic
      fun d(tag: String, message: String) =
         d(tag, message, null)

      @JvmStatic
      fun d(tag: String, message: String, throwable: Throwable?): Int
      {
         log("DEBUG", tag, message, throwable)
         return 0
      }

      @JvmStatic
      fun i(tag: String, message: String) =
         i(tag, message, null)

      @JvmStatic
      fun i(tag: String, message: String, throwable: Throwable?): Int
      {
         log("INFORMATION", tag, message, throwable)
         return 0
      }

      @JvmStatic
      fun w(tag: String, message: String) =
         w(tag, message, null)

      @JvmStatic
      fun w(tag: String, message: String, throwable: Throwable?): Int
      {
         log("WARNING", tag, message, throwable)
         return 0
      }

      @JvmStatic
      fun e(tag: String, message: String) =
         e(tag, message, null)

      @JvmStatic
      fun e(tag: String, message: String, throwable: Throwable?): Int
      {
         log("ERROR", tag, message, throwable)
         return 0
      }

      @JvmStatic
      fun wtf(tag: String, message: String) =
         wtf(tag, message, null)

      @JvmStatic
      fun wtf(tag: String, message: String, throwable: Throwable?): Int
      {
         log("FAILURE", tag, message, throwable)
         return 0
      }

      @JvmStatic
      fun getStackTraceString(throwable: Throwable): String
      {
         val stringBuilder = StringBuilder()
         var throwable: Throwable? = throwable

         while (throwable != null)
         {
            stringBuilder.append(throwable)
            stringBuilder.append('\n')

            for (stackTraceElement in throwable.stackTrace)
            {
               stringBuilder.append('\t')
               stringBuilder.append(stackTraceElement.className)
               stringBuilder.append('.')
               stringBuilder.append(stackTraceElement.methodName)
               stringBuilder.append(" at ")
               stringBuilder.append(stackTraceElement.lineNumber)
               stringBuilder.append('\n')
            }

            throwable = throwable.cause

            if (throwable != null)
            {
               stringBuilder.append("\nCaused by:\n")
            }
         }

         return stringBuilder.toString()
      }

      @JvmStatic
      fun println(priority: Int, tag: String, msg: String) =
         when (priority)
         {
            VERBOSE -> v(tag, msg)
            DEBUG   -> d(tag, msg)
            INFO    -> i(tag, msg)
            WARN    -> w(tag, msg)
            ERROR   -> e(tag, msg)
            else    -> wtf(tag, msg)
         }

      private fun log(nature: String, tag: String, message: String, throwable: Throwable?)
      {
         println("$nature:$tag:$message")
         throwable?.printStackTrace()
      }
   }
}