package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vrat_days")
data class VratDayEntity(
    @PrimaryKey val dayNumber: Int, // 1 to 10
    val title: String,
    val isFastCompleted: Boolean = false,
    val isKathaRead: Boolean = false,
    val isAartiDone: Boolean = false,
    val knotsTiedCount: Int = 0, // 0 to 10
    val userNote: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String, // e.g. "katha_1", "aarti_1"
    val title: String,
    val category: String, // "Katha" or "Aarti"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "prayer_notes")
data class PrayerNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayNumber: Int,
    val noteText: String,
    val timestamp: Long = System.currentTimeMillis()
)
