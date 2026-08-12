package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AudioTrackState(
    val trackId: String = "aarti_mangal",
    val title: String = "દશામાની મંગળ આરતી",
    val subTitle: String = "દશામા વ્રત ભક્તિ સંગીત",
    val isPlaying: Boolean = false,
    val currentPositionSeconds: Int = 0,
    val totalDurationSeconds: Int = 180,
    val volume: Float = 0.8f
)

class DevotionalAudioPlayer(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var playbackJob: Job? = null
    private var toneGenerator: ToneGenerator? = null

    private val _trackState = MutableStateFlow(AudioTrackState())
    val trackState: StateFlow<AudioTrackState> = _trackState.asStateFlow()

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playTrack(trackId: String, title: String, subTitle: String) {
        if (_trackState.value.trackId == trackId && _trackState.value.isPlaying) {
            pauseTrack()
            return
        }

        _trackState.value = _trackState.value.copy(
            trackId = trackId,
            title = title,
            subTitle = subTitle,
            isPlaying = true,
            currentPositionSeconds = 0,
            totalDurationSeconds = 180
        )

        playBellChime()
        startPlaybackTimer()
    }

    fun togglePlayPause() {
        if (_trackState.value.isPlaying) {
            pauseTrack()
        } else {
            _trackState.value = _trackState.value.copy(isPlaying = true)
            playBellChime()
            startPlaybackTimer()
        }
    }

    fun pauseTrack() {
        _trackState.value = _trackState.value.copy(isPlaying = false)
        playbackJob?.cancel()
    }

    fun seekTo(seconds: Int) {
        _trackState.value = _trackState.value.copy(
            currentPositionSeconds = seconds.coerceIn(0, _trackState.value.totalDurationSeconds)
        )
    }

    fun setVolume(volume: Float) {
        _trackState.value = _trackState.value.copy(volume = volume.coerceIn(0f, 1f))
    }

    fun playBellChime() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 300)
            scope.launch(Dispatchers.IO) {
                delay(150)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 250)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playShankhNaad() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_RINGTONE, 800)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startPlaybackTimer() {
        playbackJob?.cancel()
        playbackJob = scope.launch {
            while (isActive && _trackState.value.isPlaying) {
                delay(1000)
                val current = _trackState.value.currentPositionSeconds
                val duration = _trackState.value.totalDurationSeconds
                if (current >= duration) {
                    _trackState.value = _trackState.value.copy(
                        currentPositionSeconds = 0,
                        isPlaying = false
                    )
                    break
                } else {
                    _trackState.value = _trackState.value.copy(
                        currentPositionSeconds = current + 1
                    )
                    // Play subtle rhythmic chime every 15s when listening
                    if (current % 15 == 0 && current > 0) {
                        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 120)
                    }
                }
            }
        }
    }

    fun release() {
        playbackJob?.cancel()
        toneGenerator?.release()
        toneGenerator = null
    }
}
