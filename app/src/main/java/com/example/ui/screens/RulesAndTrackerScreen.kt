package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookmarkEntity
import com.example.data.DashamaData
import com.example.data.PrayerNoteEntity
import com.example.data.VratDayEntity
import com.example.ui.VratViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesAndTrackerScreen(
    viewModel: VratViewModel,
    vratDays: List<VratDayEntity>,
    bookmarks: List<BookmarkEntity>,
    prayerNotes: List<PrayerNoteEntity>,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Vidhi & Rules, 1: Daily Tracker, 2: Journal & Notes
    var newNoteText by remember { mutableStateOf("") }
    var selectedDayForNote by remember { mutableIntStateOf(1) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("rules_and_tracker_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tab Selector Row
        item {
            TabRow(
                selectedTabIndex = selectedSubTab,
                containerColor = DevotionalCardBg,
                contentColor = DevotionalGold,
                divider = { Divider(color = DevotionalLine) }
            ) {
                Tab(
                    selected = selectedSubTab == 0,
                    onClick = { selectedSubTab = 0 },
                    text = { Text("વ્રત વિધિ & નિયમો", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
                Tab(
                    selected = selectedSubTab == 1,
                    onClick = { selectedSubTab = 1 },
                    text = { Text("૧૦ દિવસ ટ્રેકર", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
                Tab(
                    selected = selectedSubTab == 2,
                    onClick = { selectedSubTab = 2 },
                    text = { Text("મારી નોંધ ($prayerNotes.size)", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
            }
        }

        // Sub Tab 0: Vidhi & Rules
        if (selectedSubTab == 0) {
            items(DashamaData.vratRules) { rule ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rule_card_${rule.stepNumber}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DevotionalCardBg),
                    border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = DevotionalVermilion.copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, DevotionalGold),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${rule.stepNumber}",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DevotionalGold,
                                    fontSize = 15.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = rule.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DevotionalGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = rule.description,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = DevotionalCream
                            )
                        }
                    }
                }
            }
        }

        // Sub Tab 1: 10 Days Daily Progress Tracker Checklist
        if (selectedSubTab == 1) {
            items(10) { index ->
                val dayNum = index + 1
                val dayData = vratDays.find { it.dayNumber == dayNum }
                    ?: VratDayEntity(dayNumber = dayNum, title = "દિવસ $dayNum")

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tracker_day_card_$dayNum"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DevotionalCardBg),
                    border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "દિવસ $dayNum: ${DashamaData.kathas.find { it.dayNumber == dayNum }?.title ?: ""}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DevotionalGold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Check 1: Fast Completed
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = dayData.isFastCompleted,
                                    onCheckedChange = {
                                        viewModel.toggleFastCompleted(dayNum, dayData.isFastCompleted)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = DevotionalGold,
                                        checkmarkColor = DevotionalDeepDark,
                                        uncheckedColor = DevotionalMutedGold
                                    ),
                                    modifier = Modifier.testTag("fast_checkbox_$dayNum")
                                )
                                Text("ઉપવાસ / એકટાણું", fontSize = 12.sp, color = DevotionalCream)
                            }

                            // Check 2: Katha Read
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = dayData.isKathaRead,
                                    onCheckedChange = {
                                        viewModel.toggleKathaRead(dayNum, dayData.isKathaRead)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = DevotionalGold,
                                        checkmarkColor = DevotionalDeepDark,
                                        uncheckedColor = DevotionalMutedGold
                                    ),
                                    modifier = Modifier.testTag("katha_checkbox_$dayNum")
                                )
                                Text("વાર્તા પઠન", fontSize = 12.sp, color = DevotionalCream)
                            }
                        }
                    }
                }
            }
        }

        // Sub Tab 2: Journal & Prayer Notes
        if (selectedSubTab == 2) {
            // Add New Note Input Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DevotionalCardBg),
                    border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "નવી પવિત્ર મનોકામના / પ્રાર્થના નોંધ ઉમેરો",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DevotionalGold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = newNoteText,
                            onValueChange = { newNoteText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("new_note_text_input"),
                            placeholder = { Text("અહીં તમારી શ્રદ્ધા પૂર્વક પ્રાર્થના લખો...", color = DevotionalMutedGold) },
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DevotionalSurfaceContainer,
                                unfocusedContainerColor = DevotionalSurfaceContainer,
                                focusedBorderColor = DevotionalGold,
                                unfocusedBorderColor = DevotionalLine,
                                focusedTextColor = DevotionalCream,
                                unfocusedTextColor = DevotionalCream
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                viewModel.addPrayerNote(selectedDayForNote, newNoteText)
                                newNoteText = ""
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("add_note_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DevotionalGold,
                                contentColor = DevotionalDeepDark
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "સાચવો", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Saved Notes List
            items(prayerNotes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_card_${note.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DevotionalSurfaceContainer),
                    border = BorderStroke(0.8.dp, DevotionalGold.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "દિવસ ${note.dayNumber} પ્રાર્થના",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DevotionalGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = note.noteText,
                                fontSize = 14.sp,
                                color = DevotionalCream
                            )
                        }

                        IconButton(
                            onClick = { viewModel.deletePrayerNote(note.id) },
                            modifier = Modifier.testTag("delete_note_button_${note.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = DevotionalVermilion
                            )
                        }
                    }
                }
            }
        }
    }
}
