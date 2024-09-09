package com.example.musicapplication.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicapplication.DataBase.SongInstance

@Database (entities = [SongInstance::class, AddSongInstance::class], version = 2, exportSchema = false)
abstract class SongDb : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: SongDb? = null

        fun getDb(context: Context): SongDb {
            val tempInstance = INSTANCE
            if (tempInstance !== null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDb::class.java,
                    "database.db"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}