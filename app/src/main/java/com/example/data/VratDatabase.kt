package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [VratDayEntity::class, BookmarkEntity::class, PrayerNoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VratDatabase : RoomDatabase() {
    abstract fun vratDao(): VratDao

    companion object {
        @Volatile
        private var INSTANCE: VratDatabase? = null

        fun getDatabase(context: Context): VratDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VratDatabase::class.java,
                    "dashama_vrat_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
