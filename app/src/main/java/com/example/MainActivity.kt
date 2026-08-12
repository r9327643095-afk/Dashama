package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.VratViewModel
import com.example.ui.components.AudioPlayerBar
import com.example.ui.components.BlessingDialog
import com.example.ui.screens.AartiesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.KathasScreen
import com.example.ui.screens.RulesAndTrackerScreen
import com.example.ui.theme.DashamaTheme
import com.example.ui.theme.DevotionalCardBg
import com.example.ui.theme.DevotionalDeepDark
import com.example.ui.theme.DevotionalGold

enum class NavTab(val title: String, val icon: ImageVector, val tag: String) {
    HOME("મુખ્ય", Icons.Default.Home, "nav_tab_home"),
    KATHAS("૧૦ વાર્તા", Icons.Default.MenuBook, "nav_tab_kathas"),
    AARTIES("આરતી", Icons.Default.MusicNote, "nav_tab_aarties"),
    RULES("નિયમો", Icons.Default.CheckCircle, "nav_tab_rules")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashamaTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: VratViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(NavTab.HOME) }
    var selectedKathaDay by remember { mutableIntStateOf(1) }

    val vratDays by viewModel.allVratDays.collectAsStateWithLifecycle()
    val bookmarks by viewModel.allBookmarks.collectAsStateWithLifecycle()
    val prayerNotes by viewModel.allPrayerNotes.collectAsStateWithLifecycle()
    val audioTrackState by viewModel.audioPlayer.trackState.collectAsStateWithLifecycle()
    val blessingMessage by viewModel.blessingMessage.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        containerColor = DevotionalDeepDark,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                // Persistent Floating Audio Player Bar
                AudioPlayerBar(
                    state = audioTrackState,
                    onTogglePlayPause = { viewModel.audioPlayer.togglePlayPause() },
                    onSeekTo = { seconds -> viewModel.audioPlayer.seekTo(seconds) }
                )

                // Navigation Bar
                NavigationBar(
                    containerColor = DevotionalCardBg,
                    contentColor = DevotionalGold,
                    tonalElevation = 8.dp
                ) {
                    NavTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) DevotionalGold else DevotionalGold.copy(alpha = 0.5f)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) DevotionalGold else DevotionalGold.copy(alpha = 0.6f)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = DevotionalGold.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag(tab.tag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    vratDays = vratDays,
                    onNavigateToKathaDay = { day ->
                        selectedKathaDay = day
                        currentTab = NavTab.KATHAS
                    },
                    onNavigateToAarties = {
                        currentTab = NavTab.AARTIES
                    }
                )
                NavTab.KATHAS -> KathasScreen(
                    viewModel = viewModel,
                    vratDays = vratDays,
                    bookmarks = bookmarks,
                    selectedDayIndex = selectedKathaDay
                )
                NavTab.AARTIES -> AartiesScreen(
                    viewModel = viewModel,
                    bookmarks = bookmarks
                )
                NavTab.RULES -> RulesAndTrackerScreen(
                    viewModel = viewModel,
                    vratDays = vratDays,
                    bookmarks = bookmarks,
                    prayerNotes = prayerNotes
                )
            }

            // Blessing Dialog
            blessingMessage?.let { msg ->
                BlessingDialog(
                    message = msg,
                    onDismiss = { viewModel.dismissBlessing() }
                )
            }
        }
    }
}
