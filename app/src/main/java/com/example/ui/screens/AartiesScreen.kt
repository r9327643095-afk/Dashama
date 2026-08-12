package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AartiItem
import com.example.data.BookmarkEntity
import com.example.data.DashamaData
import com.example.ui.VratViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AartiesScreen(
    viewModel: VratViewModel,
    bookmarks: List<BookmarkEntity>,
    modifier: Modifier = Modifier
) {
    val categories = listOf("બધા", "આરતી", "ચાલીસા", "થાળ", "સ્તુતિ")
    var selectedCategory by remember { mutableStateOf("બધા") }
    var expandedItemIndex by remember { mutableIntStateOf(0) } // default expand first item

    val filteredAarties = if (selectedCategory == "બધા") {
        DashamaData.aarties
    } else {
        DashamaData.aarties.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("aarties_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Category Filter Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                text = cat,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
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
                        modifier = Modifier.testTag("aarti_category_chip_$cat")
                    )
                }
            }
        }

        // List of Aarties Cards
        items(filteredAarties.indices.toList()) { index ->
            val aarti = filteredAarties[index]
            val isExpanded = index == expandedItemIndex
            val isBookmarked = bookmarks.any { it.id == aarti.id }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("aarti_card_${aarti.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DevotionalCardBg),
                border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Title Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = DevotionalVermilion.copy(alpha = 0.25f),
                                border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = aarti.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DevotionalGold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = aarti.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DevotionalCream
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Bookmark Button
                            IconButton(
                                onClick = {
                                    viewModel.toggleBookmark(
                                        id = aarti.id,
                                        title = aarti.title,
                                        category = aarti.category,
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

                            // Expand / Collapse Toggle
                            IconButton(
                                onClick = {
                                    expandedItemIndex = if (isExpanded) -1 else index
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = "Toggle",
                                    tint = DevotionalMutedGold
                                )
                            }
                        }
                    }

                    // Expandable Lyrics Section
                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Divider(color = DevotionalLine)

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = aarti.lyrics,
                                fontSize = 15.sp,
                                lineHeight = 24.sp,
                                color = DevotionalCream
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Play Audio Track Button
                            Button(
                                onClick = {
                                    viewModel.audioPlayer.playTrack(
                                        trackId = aarti.id,
                                        title = aarti.title,
                                        subTitle = "મા દશામા ${aarti.category} ભજન"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DevotionalGold,
                                    contentColor = DevotionalDeepDark
                                ),
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = "Play",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${aarti.title} સાંભળો 🎧",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
