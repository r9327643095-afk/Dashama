package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DashamaData
import com.example.data.VratDayEntity
import com.example.ui.VratViewModel
import com.example.ui.components.AltarHeaderCard
import com.example.ui.components.KnotsTrackerWidget
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: VratViewModel,
    vratDays: List<VratDayEntity>,
    onNavigateToKathaDay: (Int) -> Unit,
    onNavigateToAarties: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDiyaLit by viewModel.isDiyaLit.collectAsState()
    val quoteIndex by viewModel.currentQuoteIndex.collectAsState()
    val currentQuote = DashamaData.dailyQuotes[quoteIndex]

    // Determine current day progress
    val firstIncompleteDay = vratDays.find { !it.isFastCompleted || !it.isKathaRead }?.dayNumber ?: 1
    val currentDayEntity = vratDays.find { it.dayNumber == firstIncompleteDay }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("home_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Digital Altar Header
        item {
            AltarHeaderCard(
                isDiyaLit = isDiyaLit,
                onToggleDiya = { viewModel.toggleDiya() },
                onRingBell = { viewModel.ringBell() },
                onPlayShankh = { viewModel.playShankh() },
                onShowBlessing = { viewModel.showBlessing() }
            )
        }

        // 10 Knots Vrat Tracker
        item {
            KnotsTrackerWidget(
                currentDayNumber = firstIncompleteDay,
                knotsTiedCount = currentDayEntity?.knotsTiedCount ?: firstIncompleteDay,
                onKnotTapped = { knotIndex ->
                    viewModel.setKnotsTiedCount(firstIncompleteDay, knotIndex)
                }
            )
        }

        // Daily Devotional Quote Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quote_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DevotionalCardBg),
                border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Quote",
                                tint = DevotionalGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentQuote.subtext,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DevotionalGold
                            )
                        }

                        IconButton(
                            onClick = { viewModel.nextQuote() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Next Quote",
                                tint = DevotionalMutedGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "“${currentQuote.text}”",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = DevotionalCream,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Current Day Katha Shortcut Card
        item {
            val kathaItem = DashamaData.kathas.find { it.dayNumber == firstIncompleteDay } ?: DashamaData.kathas.first()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_katha_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DevotionalSurfaceContainer),
                border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "આજની વિશેષ કથા · દિવસ $firstIncompleteDay",
                        fontSize = 12.sp,
                        color = DevotionalGold,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = kathaItem.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DevotionalCream
                    )

                    Text(
                        text = kathaItem.subTitle,
                        fontSize = 13.sp,
                        color = DevotionalMutedGold,
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onNavigateToKathaDay(firstIncompleteDay) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DevotionalGold,
                                contentColor = DevotionalDeepDark
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = "Read Katha",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "વાર્તા વાંચો", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.audioPlayer.playTrack(
                                    trackId = "katha_$firstIncompleteDay",
                                    title = kathaItem.title,
                                    subTitle = "દિવસ $firstIncompleteDay પવિત્ર શ્રવણ"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DevotionalMaroon,
                                contentColor = DevotionalGold
                            ),
                            border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Play Audio",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "કથા સાંભળો 🎧", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Aarti Shortcuts Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Aarti Shortcut
                OutlinedButton(
                    onClick = {
                        viewModel.audioPlayer.playTrack(
                            trackId = "aarti_mangal",
                            title = "દશામાની મંગળ આરતી",
                            subTitle = "જય દશા મા મારી જય દશા મા"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_aarti_button"),
                    border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DevotionalCardBg,
                        contentColor = DevotionalGold
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(text = "🪔 મંગળ આરતી", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "સાંભળો", fontSize = 11.sp, color = DevotionalCream)
                    }
                }

                // Chalisa Shortcut
                OutlinedButton(
                    onClick = {
                        viewModel.audioPlayer.playTrack(
                            trackId = "chalisa_dashama",
                            title = "મા દશામા ચાલીસા",
                            subTitle = "જય દશામાતા ભક્ત હિતકારી"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_chalisa_button"),
                    border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DevotionalCardBg,
                        contentColor = DevotionalGold
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(text = "📜 ચાલીસા પાઠ", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "સાંભળો", fontSize = 11.sp, color = DevotionalCream)
                    }
                }
            }
        }
    }
}
