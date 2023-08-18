package com.example.presentapp2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Present::class], version = 1, exportSchema = false)
abstract class PresentDatabase: RoomDatabase() {

    abstract fun presentDao(): PresentDao

    companion object {
        private var INSTANCE: PresentDatabase? = null

        fun getDatabase(context: Context): PresentDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PresentDatabase::class.java,
                    "present_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}