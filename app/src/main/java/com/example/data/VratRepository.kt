package com.example.data

import kotlinx.coroutines.flow.Flow

class VratRepository(private val vratDao: VratDao) {

    val allVratDays: Flow<List<VratDayEntity>> = vratDao.getAllVratDays()
    val allBookmarks: Flow<List<BookmarkEntity>> = vratDao.getAllBookmarks()
    val allPrayerNotes: Flow<List<PrayerNoteEntity>> = vratDao.getAllPrayerNotes()

    fun getVratDay(dayNumber: Int): Flow<VratDayEntity?> = vratDao.getVratDay(dayNumber)

    suspend fun updateVratDay(vratDay: VratDayEntity) {
        vratDao.insertOrUpdateVratDay(vratDay)
    }

    fun isBookmarked(id: String): Flow<Boolean> = vratDao.isBookmarked(id)

    suspend fun toggleBookmark(id: String, title: String, category: String, isCurrentBookmarked: Boolean) {
        if (isCurrentBookmarked) {
            vratDao.removeBookmark(id)
        } else {
            vratDao.addBookmark(BookmarkEntity(id = id, title = title, category = category))
        }
    }

    suspend fun addPrayerNote(dayNumber: Int, noteText: String) {
        vratDao.insertPrayerNote(PrayerNoteEntity(dayNumber = dayNumber, noteText = noteText))
    }

    suspend fun deletePrayerNote(id: Long) {
        vratDao.deletePrayerNote(id)
    }
}
