package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.ui.theme.ChaitanyaBlueLight
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorBackground
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class DayChartData(
    val dateStr: String,
    val dayLabel: String,
    val naamjapTotal: Int,
    val habitsCompleted: Int,
    val isSelected: Boolean
)

@Composable
fun WeeklyBarChart(
    weeklyData: List<DayChartData>,
    language: AppLanguage,
    onDayClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "weekly_bar_chart"
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    val maxNaamjap = remember(weeklyData) {
        (weeklyData.maxOfOrNull { it.naamjapTotal } ?: 1).coerceAtLeast(500)
    }

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(weeklyData) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, animationSpec = tween(durationMillis = 650))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorComponentWhite // #FFFFFF
        ),
        border = BorderStroke(1.dp, ChaitanyaCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        text = if (language == AppLanguage.MARATHI) "साप्ताहिक नामजप व प्रगती" else "Weekly Naamjap & Progress",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ChaitanyaBluePrimary
                    )
                    Text(
                        text = if (language == AppLanguage.MARATHI) "गेल्या ७ दिवसांचे विश्लेषण" else "Last 7 days analysis",
                        style = MaterialTheme.typography.bodySmall,
                        color = ChaitanyaTextSecondary
                    )
                }

                // Legend
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(10.dp),
                            shape = RoundedCornerShape(2.dp),
                            color = ChaitanyaBluePrimary
                        ) {}
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "नामजप" else "Japa",
                            style = MaterialTheme.typography.labelSmall,
                            color = ChaitanyaTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (weeklyData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "या आठवड्यात अद्याप नोंदी नाहीत" else "No entries recorded for this week yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ChaitanyaTextSecondary
                    )
                }
            } else {
                // Bar Chart Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        val barCount = weeklyData.size
                        if (barCount == 0) return@Canvas

                        val totalAvailableWidth = size.width
                        val slotWidth = totalAvailableWidth / barCount
                        val barWidth = (slotWidth * 0.46f).coerceIn(12f, 32f)
                        val chartHeight = size.height - 24f // reserve space for baseline

                        // Grid lines
                        val stepCount = 3
                        for (i in 0..stepCount) {
                            val y = chartHeight * (i.toFloat() / stepCount)
                            drawLine(
                                color = ColorBackground,
                                start = Offset(0f, y),
                                end = Offset(totalAvailableWidth, y),
                                strokeWidth = 1f
                            )
                        }

                        // Draw Bars
                        weeklyData.forEachIndexed { index, data ->
                            val slotCenterX = index * slotWidth + (slotWidth / 2f)
                            val barLeft = slotCenterX - (barWidth / 2f)

                            val heightRatio = if (maxNaamjap > 0) {
                                (data.naamjapTotal.toFloat() / maxNaamjap).coerceIn(0f, 1f)
                            } else 0f

                            val barHeight = (chartHeight * heightRatio * animatedProgress.value).coerceAtLeast(6f)
                            val barTop = chartHeight - barHeight

                            // Gradient brush for bar
                            val barBrush = if (data.isSelected) {
                                Brush.verticalGradient(
                                    colors = listOf(ColorActiveStatus, ColorActiveStatus.copy(alpha = 0.7f)),
                                    startY = barTop,
                                    endY = chartHeight
                                )
                            } else {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        ChaitanyaBluePrimary,
                                        ChaitanyaBlueLight
                                    ),
                                    startY = barTop,
                                    endY = chartHeight
                                )
                            }

                            // Draw rounded bar
                            drawRoundRect(
                                brush = barBrush,
                                topLeft = Offset(barLeft, barTop),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(barWidth / 3f, barWidth / 3f)
                            )

                            // Highlight circle for selected day with active status #FD0031
                            if (data.isSelected) {
                                drawCircle(
                                    color = ColorActiveStatus,
                                    radius = 4f,
                                    center = Offset(slotCenterX, chartHeight + 6f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Day Labels Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weeklyData.forEach { data ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onDayClick(data.dateStr) }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = data.dayLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (data.isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                color = if (data.isSelected) ColorActiveStatus // #FD0031
                                else ChaitanyaTextSecondary
                            )
                            Text(
                                text = "${data.naamjapTotal}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = if (data.naamjapTotal > 0) ChaitanyaBluePrimary
                                else ChaitanyaTextSecondary.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}
