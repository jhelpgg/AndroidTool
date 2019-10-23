package android.database.sqlite

import android.content.Context
import android.database.DatabaseErrorHandler

abstract class SQLiteOpenHelper(
   context: Context, val name: String, factory: SQLiteDatabase.CursorFactory?,
   version: Int, errorHandler: DatabaseErrorHandler?
                               ) : AutoCloseable
{
   private val database: SQLiteDatabase

   init
   {
      println(this.javaClass.name)

      val newDatabase = name !in context.databaseList()
      this.database = context.openOrCreateDatabase(name, 0, factory)
      this.onConfigure(this.database)

      if (newDatabase)
      {
         this.database.version = version
         this.onCreate(this.database)
      }

      val oldVersion = this.database.version

      if (oldVersion < version)
      {
         this.onUpgrade(this.database, oldVersion, version)
      }

      if (oldVersion > version)
      {
         this.onDowngrade(this.database, oldVersion, version)
      }

      this.onOpen(this.database)
   }

   constructor(context: Context, name: String, version: Int, openParams: SQLiteDatabase.OpenParams?) :
        this(context, name, null, version, null)

   constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) :
        this(context, name, factory, version, null)


   final override fun close()
   {
      this.database.close()
   }

   fun getDatabaseName() = this.name

   fun getReadableDatabase() = this.database

   fun getWritableDatabase() = this.database

   open fun onConfigure(database: SQLiteDatabase)
   {
   }

   abstract fun onCreate(database: SQLiteDatabase)

   open fun onDowngrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int)
   {
   }

   abstract fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int)

   open fun onOpen(database: SQLiteDatabase)
   {
   }

   fun setIdleConnectionTimeout(idleConnectionTimeoutMs: Long)
   {
      println("setIdleConnectionTimeout is deprecated !")
   }

   fun setLookasideConfig(slotSize: Int, slotCount: Int) = Unit

   fun setOpenParams(openParams: SQLiteDatabase.OpenParams) = Unit

   fun setWriteAheadLoggingEnabled(enabled: Boolean) = Unit
}