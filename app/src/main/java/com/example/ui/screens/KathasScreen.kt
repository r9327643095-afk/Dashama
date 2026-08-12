package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookmarkEntity
import com.example.data.DashamaData
import com.example.data.KathaItem
import com.example.data.VratDayEntity
import com.example.ui.VratViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KathasScreen(
    viewModel: VratViewModel,
    vratDays: List<VratDayEntity>,
    bookmarks: List<BookmarkEntity>,
    selectedDayIndex: Int = 1,
    modifier: Modifier = Modifier
) {
    var activeDayTab by remember { mutableIntStateOf(selectedDayIndex) }
    var searchQuery by remember { mutableStateOf("") }
    var fontSizeSp by remember { mutableIntStateOf(16) }

    val activeKatha = DashamaData.kathas.find { it.dayNumber == activeDayTab } ?: DashamaData.kathas.first()
    val isBookmarked = bookmarks.any { it.id == "katha_${activeKatha.dayNumber}" }
    val dayStatus = vratDays.find { it.dayNumber == activeDayTab }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("kathas_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search & Filter Input Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("katha_search_input"),
                placeholder = { Text("૧ થી ૧૦ દિવસની વાર્તા શોધો...", color = DevotionalMutedGold) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = DevotionalGold)
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DevotionalCardBg,
                    unfocusedContainerColor = DevotionalCardBg,
                    focusedBorderColor = DevotionalGold,
                    unfocusedBorderColor = DevotionalLine,
                    focusedTextColor = DevotionalCream,
                    unfocusedTextColor = DevotionalCream
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }

        // 10 Days Horizontal Selector Tabs
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items(DashamaData.kathas) { katha ->
                    val isSelected = katha.dayNumber == activeDayTab
                    val isRead = vratDays.find { it.dayNumber == katha.dayNumber }?.isKathaRead == true

                    FilterChip(
                        selected = isSelected,
                        onClick = { activeDayTab = katha.dayNumber },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isRead) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Done",
                                        tint = if (isSelected) DevotionalDeepDark else DevotionalGold,
                                        modifier = Modifier.size(14.dp).padding(end = 4.dp)
                                    )
                                }
                                Text(
                                    text = "દિવસ ${katha.dayNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DevotionalGold,
                            selectedLabelColor = DevotionalDeepDark,
                            containerColor = DevotionalCardBg,
                            labelColor = DevotionalCream
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = DevotionalGold.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.testTag("katha_day_chip_${katha.dayNumber}")
                    )
                }
            }
        }

        // Active Katha Reader Container
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("katha_reader_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = DevotionalCardBg),
                border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header Bar with Font controls, Bookmark & Read status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = DevotionalVermilion.copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "દિવસ $activeDayTab કથા",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DevotionalGold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Font Size Decrease
                            IconButton(
                                onClick = { if (fontSizeSp > 12) fontSizeSp -= 2 },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text("-A", color = DevotionalGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            // Font Size Increase
                            IconButton(
                                onClick = { if (fontSizeSp < 26) fontSizeSp += 2 },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text("+A", color = DevotionalGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            // Bookmark Toggle
                            IconButton(
                                onClick = {
                                    viewModel.toggleBookmark(
                                        id = "katha_${activeKatha.dayNumber}",
                                        title = activeKatha.title,
                                        category = "કથા",
                                        isCurrentBookmarked = isBookmarked
                                    )
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = DevotionalGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = activeKatha.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevotionalGold,
                        lineHeight = 30.sp
                    )

                    Text(
                        text = activeKatha.subTitle,
                        fontSize = 14.sp,
                        color = DevotionalMutedGold,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Divider(color = DevotionalLine)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Story Text Content
                    Text(
                        text = activeKatha.storyContent,
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp + 10).sp,
                        color = DevotionalCream
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Story Moral Box
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DevotionalSurfaceContainer),
                        border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "વાર્તાનો પવિત્ર સાર:", fontSize = 12.sp, color = DevotionalGold, fontWeight = FontWeight.Bold)
                            Text(
                                text = activeKatha.moral,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = DevotionalCream,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Bottom Actions: Listen Audio & Mark Read Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                viewModel.audioPlayer.playTrack(
                                    trackId = "katha_$activeDayTab",
                                    title = activeKatha.title,
                                    subTitle = "દિવસ $activeDayTab કથા પાઠ"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DevotionalGold,
                                contentColor = DevotionalDeepDark
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = "Play", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "કથા સાંભળો 🎧", fontWeight = FontWeight.Bold)
                        }

                        val isKathaRead = dayStatus?.isKathaRead == true

                        OutlinedButton(
                            onClick = { viewModel.toggleKathaRead(activeDayTab, isKathaRead) },
                            border = BorderStroke(1.dp, if (isKathaRead) DevotionalGold else DevotionalMutedGold),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isKathaRead) DevotionalGold else DevotionalCream
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Mark Read",
                                tint = if (isKathaRead) DevotionalGold else DevotionalMutedGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isKathaRead) "વાંચેલી છે" else "વાંચી લીધી")
                        }
                    }
                }
            }
        }
    }
}
