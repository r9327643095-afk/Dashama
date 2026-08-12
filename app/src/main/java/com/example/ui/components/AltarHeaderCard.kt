package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AltarHeaderCard(
    isDiyaLit: Boolean,
    onToggleDiya: () -> Unit,
    onRingBell: () -> Unit,
    onPlayShankh: () -> Unit,
    onShowBlessing: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Flickering animation for Diya flame
    val infiniteTransition = rememberInfiniteTransition(label = "FlameFlicker")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("altar_header_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DevotionalCardBg),
        border = CardBorder(0.8.dp, DevotionalGold.copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DevotionalMaroon.copy(alpha = 0.8f),
                            DevotionalDeepDark,
                            DevotionalCardBg
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top Header Pill
                Surface(
                    shape = RoundedCornerShape(50),
                    color = DevotionalMaroon.copy(alpha = 0.6f),
                    border = CardBorder(1.dp, DevotionalGold.copy(alpha = 0.35f)),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ૐ",
                            color = DevotionalGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "જય માતાજી · અષાઢ વદ અમાસ પવિત્ર વ્રત",
                            color = DevotionalGold.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Main Devotional Editorial Title
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "દશામા",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = DevotionalCream,
                        letterSpacing = (-1).sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "ની પવિત્ર વ્રત વાર્તા",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevotionalGold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Text(
                    text = "દાદીમાના મુખેથી સાંભળેલી એ પવિત્ર કથા, જે દસ દિવસ શ્રદ્ધાથી સાંભળતા સઘળા દુઃખ દૂર થાય છે.",
                    fontSize = 13.sp,
                    color = DevotionalMutedGold,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                )

                // Visual Altar Illustration
                Box(
                    modifier = Modifier
                        .size(width = 280.dp, height = 180.dp)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Halo Ring Light Behind Kalash
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        if (isDiyaLit) DevotionalGold.copy(alpha = 0.35f) else Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Diya Flame Animation
                        if (isDiyaLit) {
                            Box(
                                modifier = Modifier
                                    .scale(flameScale)
                                    .size(width = 24.dp, height = 36.dp)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xFFFFF1A5),
                                                DevotionalGold,
                                                DevotionalVermilion
                                            )
                                        ),
                                        shape = RoundedCornerShape(topStart = 80.dp, bottomEnd = 80.dp)
                                    )
                            )
                        } else {
                            Spacer(modifier = Modifier.height(36.dp))
                        }

                        // Diya Body (Brass Lamp)
                        Box(
                            modifier = Modifier
                                .size(width = 64.dp, height = 24.dp)
                                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(DevotionalGoldLight, DevotionalGold, DevotionalMaroon)
                                    )
                                )
                                .border(1.dp, DevotionalGold, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("શુભ", fontSize = 10.sp, color = DevotionalMaroon, fontWeight = FontWeight.Bold)
                        }

                        // Bajot Base
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(width = 220.dp, height = 16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(DevotionalRed, DevotionalVermilion, DevotionalRed)
                                    )
                                )
                                .border(1.dp, DevotionalGold, RoundedCornerShape(4.dp))
                        )
                    }
                }

                // Interactive Altar Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Diya Toggle
                    OutlinedButton(
                        onClick = onToggleDiya,
                        modifier = Modifier.testTag("diya_toggle_button"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DevotionalGold
                        ),
                        border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "Diya",
                            tint = if (isDiyaLit) DevotionalGold else DevotionalMutedGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isDiyaLit) "દીવો પ્રગટેલો" else "દીવો પ્રગટાવો",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Bell Ring
                    OutlinedButton(
                        onClick = onRingBell,
                        modifier = Modifier.testTag("bell_ring_button"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DevotionalCream
                        ),
                        border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Bell",
                            tint = DevotionalGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "ઘંટડી 🔔", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Shankh Naad
                    OutlinedButton(
                        onClick = onPlayShankh,
                        modifier = Modifier.testTag("shankh_button"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DevotionalCream
                        ),
                        border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.6f))
                    ) {
                        Text(text = "શંખનાદ 🐚", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Jai Dashama Blessing Button
                Button(
                    onClick = onShowBlessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .testTag("blessing_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DevotionalGold,
                        contentColor = DevotionalDeepDark
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Blessing",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "જય દશામાં આશીર્વાદ મેળવો 🙏",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// Border Helper Function for Compose compatibility
@Composable
private fun CardBorder(width: androidx.compose.ui.unit.Dp, color: Color) = BorderStroke(width, color)
