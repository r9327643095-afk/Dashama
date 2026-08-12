package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VratDao {
    @Query("SELECT * FROM vrat_days ORDER BY dayNumber ASC")
    fun getAllVratDays(): Flow<List<VratDayEntity>>

    @Query("SELECT * FROM vrat_days WHERE dayNumber = :dayNumber")
    fun getVratDay(dayNumber: Int): Flow<VratDayEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateVratDay(vratDay: VratDayEntity)

    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun removeBookmark(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Query("SELECT * FROM prayer_notes ORDER BY timestamp DESC")
    fun getAllPrayerNotes(): Flow<List<PrayerNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerNote(note: PrayerNoteEntity)

    @Query("DELETE FROM prayer_notes WHERE id = :id")
    suspend fun deletePrayerNote(id: Long)
}
