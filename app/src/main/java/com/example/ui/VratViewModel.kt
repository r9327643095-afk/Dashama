package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.DevotionalAudioPlayer
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VratViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VratRepository
    val audioPlayer: DevotionalAudioPlayer

    val allVratDays: StateFlow<List<VratDayEntity>>
    val allBookmarks: StateFlow<List<BookmarkEntity>>
    val allPrayerNotes: StateFlow<List<PrayerNoteEntity>>

    private val _isDiyaLit = MutableStateFlow(true)
    val isDiyaLit: StateFlow<Boolean> = _isDiyaLit.asStateFlow()

    private val _currentQuoteIndex = MutableStateFlow(0)
    val currentQuoteIndex: StateFlow<Int> = _currentQuoteIndex.asStateFlow()

    private val _selectedDayForKatha = MutableStateFlow(1)
    val selectedDayForKatha: StateFlow<Int> = _selectedDayForKatha.asStateFlow()

    private val _blessingMessage = MutableStateFlow<String?>(null)
    val blessingMessage: StateFlow<String?> = _blessingMessage.asStateFlow()

    init {
        val db = VratDatabase.getDatabase(application)
        repository = VratRepository(db.vratDao())
        audioPlayer = DevotionalAudioPlayer(application)

        allVratDays = repository.allVratDays.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allBookmarks = repository.allBookmarks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allPrayerNotes = repository.allPrayerNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial 10 days if empty
        viewModelScope.launch {
            allVratDays.first().let { days ->
                if (days.isEmpty()) {
                    DashamaData.kathas.forEach { katha ->
                        repository.updateVratDay(
                            VratDayEntity(
                                dayNumber = katha.dayNumber,
                                title = katha.title,
                                isFastCompleted = false,
                                isKathaRead = false,
                                isAartiDone = false,
                                knotsTiedCount = katha.dayNumber // default knot count matching day
                            )
                        )
                    }
                }
            }
        }
    }

    fun toggleDiya() {
        _isDiyaLit.value = !_isDiyaLit.value
        if (_isDiyaLit.value) {
            audioPlayer.playBellChime()
        }
    }

    fun ringBell() {
        audioPlayer.playBellChime()
    }

    fun playShankh() {
        audioPlayer.playShankhNaad()
    }

    fun nextQuote() {
        _currentQuoteIndex.value = (_currentQuoteIndex.value + 1) % DashamaData.dailyQuotes.size
    }

    fun selectDayForKatha(dayNumber: Int) {
        _selectedDayForKatha.value = dayNumber.coerceIn(1, 10)
    }

    fun toggleFastCompleted(dayNumber: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            val day = allVratDays.value.find { it.dayNumber == dayNumber }
                ?: VratDayEntity(dayNumber = dayNumber, title = "દિવસ $dayNumber")
            repository.updateVratDay(day.copy(isFastCompleted = !currentStatus))
            audioPlayer.playBellChime()
        }
    }

    fun toggleKathaRead(dayNumber: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            val day = allVratDays.value.find { it.dayNumber == dayNumber }
                ?: VratDayEntity(dayNumber = dayNumber, title = "દિવસ $dayNumber")
            repository.updateVratDay(day.copy(isKathaRead = !currentStatus))
            audioPlayer.playBellChime()
        }
    }

    fun setKnotsTiedCount(dayNumber: Int, knotsCount: Int) {
        viewModelScope.launch {
            val day = allVratDays.value.find { it.dayNumber == dayNumber }
                ?: VratDayEntity(dayNumber = dayNumber, title = "દિવસ $dayNumber")
            repository.updateVratDay(day.copy(knotsTiedCount = knotsCount.coerceIn(0, 10)))
            audioPlayer.playBellChime()
        }
    }

    fun toggleBookmark(id: String, title: String, category: String, isCurrentBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(id, title, category, isCurrentBookmarked)
            audioPlayer.playBellChime()
        }
    }

    fun isBookmarked(id: String): Flow<Boolean> {
        return repository.isBookmarked(id)
    }

    fun addPrayerNote(dayNumber: Int, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addPrayerNote(dayNumber, text.trim())
            audioPlayer.playBellChime()
        }
    }

    fun deletePrayerNote(id: Long) {
        viewModelScope.launch {
            repository.deletePrayerNote(id)
        }
    }

    fun showBlessing() {
        val blessings = listOf(
            "મા દશામા તમારું કલ્યાણ કરે! ઘરમાં સુખ અને શાંતિ રહે. 🙏",
            "દશામાના આશીર્વાદથી તમારા પરિવારના બધા કષ્ટ દૂર થાય! ✨",
            "જય દશામા! તમારી દરેક મનોકામના પૂર્ણ થાય. 🌸",
            "વ્રતના પ્રભાવથી તમારા ઘરમાં લક્ષ્મીનો વાસ થાય અને સમૃદ્ધિ આવે. 🕉️"
        )
        _blessingMessage.value = blessings.random()
        audioPlayer.playBellChime()
    }

    fun dismissBlessing() {
        _blessingMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
