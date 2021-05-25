package com.caunb163.data.datalocal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [ProfanityWord::class], version = 1, exportSchema = false)
abstract class ProfanityDatabase : RoomDatabase() {

    abstract fun profanityDao(): ProfanityDao

    companion object {
        @Volatile
        private var INSTANCE: ProfanityDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS)!!

        fun getDatabase(context: Context): ProfanityDatabase {
            return INSTANCE ?: synchronized(ProfanityDatabase::class.java) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfanityDatabase::class.java,
                    "profanity_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}