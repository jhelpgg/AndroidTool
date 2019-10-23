package android.app

import android.content.Context
import android.content.Loader
import android.os.Bundle
import java.io.FileDescriptor
import java.io.PrintWriter

class LoaderManagerForTests(val context: Context) : LoaderManager()
{
   private val loaders = HashMap<Int, Loader<out Any>>()

   init
   {
      println(this.javaClass.name)
   }

   override fun <D : Any> initLoader(id: Int, args: Bundle, callback: LoaderCallbacks<D>): Loader<D>
   {
      synchronized(this.loaders)
      {
         return this.loaders.getOrPut(id) { Loader<D>(this.context) } as Loader<D>
      }
   }

   override fun dump(prefix: String, fd: FileDescriptor, writer: PrintWriter, args: Array<out String>)
   {
      synchronized(this.loaders)
      {
         for (loader in this.loaders.values)
         {
            loader.dump(prefix, fd, writer, args)
         }
      }
   }

   override fun destroyLoader(id: Int)
   {
      synchronized(this.loaders)
      {
         this.loaders.remove(id)
      }
   }

   override fun <D : Any> getLoader(id: Int): Loader<D>?
   {
      synchronized(this.loaders)
      {
         return this.loaders.get(id) as? Loader<D>
      }
   }

   override fun <D : Any> restartLoader(id: Int, args: Bundle, callback: LoaderCallbacks<D>): Loader<D>
   {
      synchronized(this.loaders)
      {
         this.loaders[id]?.let { loader ->
            loader.reset()
         }
      }

      return this.initLoader(id, args, callback)
   }
}