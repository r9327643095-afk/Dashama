package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioTrackState
import com.example.ui.theme.*

@Composable
fun AudioPlayerBar(
    state: AudioTrackState,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Spinning disc animation when audio is playing
    val infiniteTransition = rememberInfiniteTransition(label = "DiscSpin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DiscRotation"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("audio_player_bar"),
        shape = RoundedCornerShape(20.dp),
        color = DevotionalCardBg.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spinning Music Disc
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .rotate(if (state.isPlaying) rotation else 0f)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(DevotionalGold, DevotionalMaroon, DevotionalDeepDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Playing",
                        tint = DevotionalCream,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Track Metadata
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = state.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevotionalCream,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = state.subTitle,
                        fontSize = 11.sp,
                        color = DevotionalMutedGold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Play / Pause Floating Button
                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(DevotionalGold)
                        .testTag("audio_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                        tint = DevotionalDeepDark,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Seek Bar Slider & Time Codes
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentMin = state.currentPositionSeconds / 60
                val currentSec = state.currentPositionSeconds % 60
                val totalMin = state.totalDurationSeconds / 60
                val totalSec = state.totalDurationSeconds % 60

                Text(
                    text = String.format("%02d:%02d", currentMin, currentSec),
                    fontSize = 10.sp,
                    color = DevotionalMutedGold
                )

                Slider(
                    value = state.currentPositionSeconds.toFloat(),
                    onValueChange = { onSeekTo(it.toInt()) },
                    valueRange = 0f..state.totalDurationSeconds.toFloat(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .testTag("audio_slider"),
                    colors = SliderDefaults.colors(
                        thumbColor = DevotionalGold,
                        activeTrackColor = DevotionalGold,
                        inactiveTrackColor = DevotionalLine
                    )
                )

                Text(
                    text = String.format("%02d:%02d", totalMin, totalSec),
                    fontSize = 10.sp,
                    color = DevotionalMutedGold
                )
            }
        }
    }
}
