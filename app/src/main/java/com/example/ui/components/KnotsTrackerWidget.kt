package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun KnotsTrackerWidget(
    currentDayNumber: Int,
    knotsTiedCount: Int,
    onKnotTapped: (knotIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("knots_tracker_widget"),
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
                Column {
                    Text(
                        text = "૧૦ ગાંઠનો પવિત્ર દોરો",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevotionalGold
                    )
                    Text(
                        text = "દશામાનો પવિત્ર વ્રત સૂત્ર ટ્રેકર ($knotsTiedCount / ૧૦ ગાંઠ)",
                        fontSize = 12.sp,
                        color = DevotionalMutedGold
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = DevotionalVermilion.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, DevotionalGold.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "$knotsTiedCount / 10",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevotionalGold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Red thread line connecting all 10 knots
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                // Thread background line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(DevotionalVermilion, DevotionalRed, DevotionalVermilion)
                            )
                        )
                )

                // 10 Knot Circles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (knot in 1..10) {
                        val isTied = knot <= knotsTiedCount
                        val circleBg by animateColorAsState(
                            targetValue = if (isTied) DevotionalGold else DevotionalCardBg,
                            label = "knotBg"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (isTied) DevotionalDeepDark else DevotionalCream,
                            label = "knotText"
                        )

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(circleBg)
                                .border(
                                    BorderStroke(
                                        1.5.dp,
                                        if (isTied) DevotionalGoldLight else DevotionalVermilion
                                    ),
                                    CircleShape
                                )
                                .clickable { onKnotTapped(knot) }
                                .testTag("knot_item_$knot"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isTied) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Tied",
                                    tint = DevotionalDeepDark,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = "$knot",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "ગોળ પર ટેપ કરીને ગાંઠ બંધાયેલી ચિહ્નિત કરો. દરેક ગાંઠ મનની ઇચ્છા પૂરી કરે છે.",
                fontSize = 11.sp,
                color = DevotionalCream.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
