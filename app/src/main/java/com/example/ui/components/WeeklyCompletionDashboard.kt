package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBlueLight
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorBackground
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite

@Composable
fun WeeklyCompletionDashboard(
    weeklyData: List<DayChartData>,
    allEntries: List<DailyRoutineEntity>,
    language: AppLanguage,
    onDayClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "weekly_completion_dashboard"
) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(weeklyData) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, animationSpec = tween(durationMillis = 750))
    }

    // Selected day index for interactive inspection (defaults to the last/today)
    var selectedIndex by remember(weeklyData) {
        val initialIdx = weeklyData.indexOfFirst { it.isSelected }
        mutableIntStateOf(if (initialIdx >= 0) initialIdx else weeklyData.lastIndex.coerceAtLeast(0))
    }

    // Calculate 7-day KPIs
    val validDays = weeklyData.filter { it.completionPercentage > 0f }
    val avgCompletion = if (weeklyData.isNotEmpty()) {
        weeklyData.map { it.completionPercentage }.average().toFloat()
    } else 0f

    val consistentDaysCount = weeklyData.count { it.completionPercentage >= 60f }
    val fullRoutineDaysCount = weeklyData.count { it.completionPercentage >= 95f }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ColorComponentWhite), // #FFFFFF
        border = BorderStroke(1.dp, ChaitanyaCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Dashboard Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ColorNavigations, // #D3EFFB
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = "Completion Dashboard",
                                tint = ColorActiveStatus, // #FD0031
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = AppStrings.completionDashboardTitle(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = ChaitanyaBluePrimary
                        )
                        Text(
                            text = AppStrings.completionDashboardSubtitle(language),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = ChaitanyaTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KPI Summary Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardMetricPill(
                    title = AppStrings.avgCompletionRateLabel(language),
                    value = "%.0f%%".format(avgCompletion),
                    subtitle = if (language == AppLanguage.MARATHI) "७ दिवसांची सरासरी" else "7-day average",
                    highlight = true,
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricPill(
                    title = AppStrings.highConsistencyDays(language),
                    value = "$consistentDaysCount/7",
                    subtitle = if (language == AppLanguage.MARATHI) "दिवस (≥६०%)" else "days (≥60%)",
                    highlight = false,
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricPill(
                    title = if (language == AppLanguage.MARATHI) "संपूर्ण साधना" else "Full Routine",
                    value = "$fullRoutineDaysCount/7",
                    subtitle = if (language == AppLanguage.MARATHI) "दिवस (१००%)" else "days (100%)",
                    highlight = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 7-DAY COMPLETION RATE BAR CHART
            if (weeklyData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "मागील ७ दिवसांचा डेटा उपलब्ध नाही" else "No 7-day data recorded yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ChaitanyaTextSecondary
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .pointerInput(weeklyData) {
                            detectTapGestures(
                                onTap = { offset ->
                                    val leftPadding = 32.dp.toPx()
                                    val rightPadding = 12.dp.toPx()
                                    val chartWidth = size.width - leftPadding - rightPadding
                                    val slotWidth = chartWidth / weeklyData.size
                                    val relativeX = offset.x - leftPadding
                                    if (relativeX >= 0) {
                                        val idx = (relativeX / slotWidth).toInt().coerceIn(0, weeklyData.lastIndex)
                                        selectedIndex = idx
                                    }
                                }
                            )
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val totalWidth = size.width
                        val totalHeight = size.height

                        val leftPadding = 34.dp.toPx()
                        val rightPadding = 12.dp.toPx()
                        val topPadding = 22.dp.toPx()
                        val bottomPadding = 36.dp.toPx()

                        val chartWidth = totalWidth - leftPadding - rightPadding
                        val chartHeight = totalHeight - topPadding - bottomPadding

                        // Reference Grid Lines for 0%, 25%, 50%, 75%, 100%
                        val steps = 4
                        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        val gridLineColor = Color(0xFFE5E7EB)

                        for (i in 0..steps) {
                            val y = topPadding + chartHeight * (i.toFloat() / steps)
                            drawLine(
                                color = gridLineColor,
                                start = Offset(leftPadding, y),
                                end = Offset(totalWidth - rightPadding, y),
                                strokeWidth = 1f,
                                pathEffect = dashEffect
                            )
                        }

                        // Bar rendering
                        val count = weeklyData.size
                        val slotWidth = chartWidth / count
                        val barWidth = (slotWidth * 0.52f).coerceIn(16.dp.toPx(), 32.dp.toPx())

                        weeklyData.forEachIndexed { index, day ->
                            val slotCenterX = leftPadding + (index + 0.5f) * slotWidth
                            val barLeft = slotCenterX - (barWidth / 2f)

                            val pct = (day.completionPercentage / 100f).coerceIn(0f, 1f)
                            val isSelected = index == selectedIndex

                            // 1. Background full-height column track (100% guide)
                            drawRoundRect(
                                color = ColorNavigations.copy(alpha = 0.35f), // Soft #D3EFFB
                                topLeft = Offset(barLeft, topPadding),
                                size = Size(barWidth, chartHeight),
                                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            )

                            // 2. Foreground Completion Progress Bar
                            val barHeight = (chartHeight * pct * animatedProgress.value).coerceAtLeast(0f)
                            val barTop = topPadding + chartHeight - barHeight

                            if (barHeight > 2f) {
                                val barBrush = when {
                                    isSelected || pct >= 0.8f -> Brush.verticalGradient(
                                        colors = listOf(
                                            ColorActiveStatus, // #FD0031
                                            ColorActiveStatus.copy(alpha = 0.85f)
                                        ),
                                        startY = barTop,
                                        endY = topPadding + chartHeight
                                    )
                                    pct > 0f -> Brush.verticalGradient(
                                        colors = listOf(
                                            ChaitanyaBluePrimary,
                                            ChaitanyaBlueLight
                                        ),
                                        startY = barTop,
                                        endY = topPadding + chartHeight
                                    )
                                    else -> Brush.verticalGradient(
                                        colors = listOf(ColorNavigations, ColorNavigations),
                                        startY = barTop,
                                        endY = topPadding + chartHeight
                                    )
                                }

                                drawRoundRect(
                                    brush = barBrush,
                                    topLeft = Offset(barLeft, barTop),
                                    size = Size(barWidth, barHeight),
                                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                                )
                            }

                            // 3. Selection outline highlight around the bar
                            if (isSelected) {
                                drawRoundRect(
                                    color = ColorActiveStatus,
                                    topLeft = Offset(barLeft - 2.dp.toPx(), topPadding - 2.dp.toPx()),
                                    size = Size(barWidth + 4.dp.toPx(), chartHeight + 4.dp.toPx()),
                                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                        }
                    }

                    // Y-Axis Percentage Labels
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 14.dp, bottom = 44.dp)
                            .height(150.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("100%", "75%", "50%", "25%", "0%").forEach { label ->
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = ChaitanyaTextSecondary
                            )
                        }
                    }

                    // Top Bar Percentage Badges & Bottom Day Labels
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 34.dp, end = 12.dp)
                    ) {
                        // Top Percentage Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            weeklyData.forEachIndexed { idx, day ->
                                val isSelected = idx == selectedIndex
                                Text(
                                    text = if (day.completionPercentage > 0f) "%.0f%%".format(day.completionPercentage) else "-",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
                                    ),
                                    color = if (isSelected) ColorActiveStatus else if (day.completionPercentage >= 80f) ColorActiveStatus else ChaitanyaBluePrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Bottom Day & Date Labels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            weeklyData.forEachIndexed { idx, day ->
                                val isSelected = idx == selectedIndex
                                val dayNumber = day.dateStr.split("-").lastOrNull() ?: ""
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { selectedIndex = idx }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = day.dayLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        color = if (isSelected) ColorActiveStatus else ChaitanyaBlueDark
                                    )
                                    Text(
                                        text = dayNumber,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) ColorActiveStatus else ChaitanyaTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // INTERACTIVE DAY INSPECTION CARD
            if (selectedIndex in weeklyData.indices) {
                val inspectedDay = weeklyData[selectedIndex]
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("day_inspection_card"),
                    shape = RoundedCornerShape(14.dp),
                    color = ColorBackground, // #F7F7F7
                    border = BorderStroke(1.dp, ChaitanyaCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${inspectedDay.dayLabel}, ${inspectedDay.dateStr}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = ChaitanyaBluePrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    if (inspectedDay.isSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = ColorActiveStatus
                                        ) {
                                            Text(
                                                text = if (language == AppLanguage.MARATHI) "निवडलेला दिवस" else "Selected",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                                color = ColorTextWhite,
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp),
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = if (language == AppLanguage.MARATHI)
                                        "${inspectedDay.habitsCompleted} पैकी ${inspectedDay.totalHabits} सवयी पूर्ण (%.0f%%)".format(inspectedDay.completionPercentage)
                                    else
                                        "${inspectedDay.habitsCompleted} of ${inspectedDay.totalHabits} practices done (%.0f%%)".format(inspectedDay.completionPercentage),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp),
                                    color = if (inspectedDay.completionPercentage >= 70f) ColorActiveStatus else ChaitanyaBlueDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Open Daily Routine Button with compact padding
                            Button(
                                onClick = { onDayClick(inspectedDay.dateStr) },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorActiveStatus, // #FD0031
                                    contentColor = ColorTextWhite
                                ),
                                modifier = Modifier.testTag("btn_inspect_open_daily")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (language == AppLanguage.MARATHI) "सारणी उघडा" else "Open Routine",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = ColorTextWhite,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        tint = ColorTextWhite,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7-DAY PHASE-WISE COMPLETION RATE BREAKDOWN
            Text(
                text = AppStrings.phaseBreakdownTitle(language),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = ChaitanyaBluePrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            val phase1Avg = calculatePhaseAverage(allEntries, 1)
            val phase2Avg = calculatePhaseAverage(allEntries, 2)
            val phase3Avg = calculatePhaseAverage(allEntries, 3)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PhaseProgressRow(
                    phaseName = if (language == AppLanguage.MARATHI) "टप्पा १: पायाभूत साधना व उपाय" else "Phase 1: Basic & Remedies",
                    percentage = phase1Avg,
                    color = ColorActiveStatus
                )
                PhaseProgressRow(
                    phaseName = if (language == AppLanguage.MARATHI) "टप्पा २: भाव व अंतर्मुखता" else "Phase 2: Devotion & Reflection",
                    percentage = phase2Avg,
                    color = ChaitanyaBluePrimary
                )
                PhaseProgressRow(
                    phaseName = if (language == AppLanguage.MARATHI) "टप्पा ३: दोष निर्मूलन व साधना" else "Phase 3: Rectification & Humility",
                    percentage = phase3Avg,
                    color = ChaitanyaBlueDark
                )
            }
        }
    }
}

@Composable
private fun DashboardMetricPill(
    title: String,
    value: String,
    subtitle: String,
    highlight: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (highlight) ColorNavigations.copy(alpha = 0.6f) else ColorBackground, // #D3EFFB or #F7F7F7
        border = BorderStroke(1.dp, if (highlight) ColorActiveStatus.copy(alpha = 0.4f) else ChaitanyaCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = ChaitanyaTextSecondary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                ),
                color = if (highlight) ColorActiveStatus else ChaitanyaBluePrimary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = ChaitanyaTextSecondary.copy(alpha = 0.8f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PhaseProgressRow(
    phaseName: String,
    percentage: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = phaseName,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = ChaitanyaBlueDark
            )
            Text(
                text = "%.0f%%".format(percentage),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                color = color
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { (percentage / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = ColorNavigations.copy(alpha = 0.5f)
        )
    }
}

private fun calculatePhaseAverage(entries: List<DailyRoutineEntity>, phase: Int): Float {
    if (entries.isEmpty()) return 0f
    val recent7 = entries.sortedByDescending { it.date }.take(7)
    if (recent7.isEmpty()) return 0f

    val percentages = recent7.map { entry ->
        when (phase) {
            1 -> {
                var c = 0
                if (entry.naamjapKuldevi > 0) c++
                if (entry.naamjapGurudevDatta > 0) c++
                if (entry.kapurItrUpay > 0) c++
                if (entry.prarthana > 0) c++
                if (entry.krutajnata > 0) c++
                if (entry.aavaranKadhne > 0) c++
                (c.toFloat() / 6f) * 100f
            }
            2 -> {
                var c = 0
                if (entry.bhavjagruti > 0) c++
                if (entry.aatmanivedan > 0) c++
                if (entry.manachaAadhavDone) c++
                if (entry.dainandinLikhanDone) c++
                if (entry.granthPathanDone) c++
                (c.toFloat() / 5f) * 100f
            }
            else -> {
                var c = 0
                if (entry.chukaVicharneDone) c++
                if (entry.prayashchittaDone) c++
                if (entry.saraniLikhanDone) c++
                if (entry.chukaLihineDone) c++
                if (entry.swayamsuchanaDone) c++
                if (entry.kshamayachanaDone) c++
                (c.toFloat() / 6f) * 100f
            }
        }
    }
    return percentages.average().toFloat()
}
