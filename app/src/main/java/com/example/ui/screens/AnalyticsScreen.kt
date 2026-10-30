package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.ui.components.MonthlyAveragesCard
import com.example.ui.components.WeeklyBarChart
import com.example.ui.components.WeeklyCompletionDashboard
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorBackground
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite
import com.example.ui.viewmodel.ChintanSaraniViewModel

@Composable
fun AnalyticsScreen(
    viewModel: ChintanSaraniViewModel,
    onNavigateToDaily: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeLang by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val weeklyChartData by viewModel.weeklyChartData.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val currentDraft by viewModel.currentDraft.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBackground) // #F7F7F7
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (activeLang == AppLanguage.MARATHI) "साधना प्रगती व आलेख" else "Spiritual Analytics & Trends",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = ChaitanyaBluePrimary
            )
            Text(
                text = if (activeLang == AppLanguage.MARATHI) "आजची, आठवड्याची व महिन्याची सरासरी प्रगती" else "Today, weekly, and monthly average progress",
                style = MaterialTheme.typography.bodySmall,
                color = ChaitanyaTextSecondary
            )
        }

        // --- 1. 7-DAY VISUAL ROUTINE COMPLETION RATE DASHBOARD (BAR CHART) ---
        item {
            WeeklyCompletionDashboard(
                weeklyData = weeklyChartData,
                allEntries = allEntries,
                language = activeLang,
                onDayClick = { dateStr ->
                    viewModel.selectDate(dateStr)
                    onNavigateToDaily(dateStr)
                }
            )
        }

        // --- 2. TODAY'S SUMMARY SCORECARD ---
        item {
            TodayScorecard(
                entry = currentDraft,
                selectedDate = selectedDate,
                isToday = selectedDate == viewModel.getTodayDate(),
                language = activeLang
            )
        }

        // --- 3. THIS WEEK'S NAAMJAP CHART ---
        item {
            WeeklyBarChart(
                weeklyData = weeklyChartData,
                language = activeLang,
                onDayClick = { dateStr ->
                    viewModel.selectDate(dateStr)
                    onNavigateToDaily(dateStr)
                }
            )
        }

        // --- 3. MONTH'S AVERAGE (30-DAY STATS) ---
        item {
            MonthlyAveragesCard(
                entries = allEntries,
                language = activeLang
            )
        }

        // --- 4. CATEGORY BREAKDOWN ---
        item {
            CategoryBreakdownCard(
                entry = currentDraft,
                language = activeLang
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TodayScorecard(
    entry: DailyRoutineEntity,
    selectedDate: String,
    isToday: Boolean,
    language: AppLanguage
) {
    val completionFraction = (entry.completedHabitsCount.toFloat() / entry.totalHabitsCount.toFloat()).coerceIn(0f, 1f)
    val percentage = (completionFraction * 100).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("today_scorecard"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorComponentWhite // #FFFFFF
        ),
        border = BorderStroke(1.dp, ChaitanyaCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                        text = if (isToday) {
                            if (language == AppLanguage.MARATHI) "आजची साधना स्थिती" else "Today's Routine Status"
                        } else {
                            if (language == AppLanguage.MARATHI) "दिनांक: $selectedDate" else "Date: $selectedDate"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ChaitanyaBluePrimary
                    )
                    Text(
                        text = if (language == AppLanguage.MARATHI) "१६ मुद्द्यांपैकी पूर्ण केलेल्या कृती" else "Out of 16 spiritual points",
                        style = MaterialTheme.typography.bodySmall,
                        color = ChaitanyaTextSecondary
                    )
                }

                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { completionFraction },
                        modifier = Modifier.size(54.dp),
                        color = ColorActiveStatus, // #FD0031
                        trackColor = ColorNavigations, // #D3EFFB
                        strokeWidth = 5.dp
                    )
                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = ColorActiveStatus // #FD0031
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick metrics grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricPill(
                    icon = Icons.Default.SelfImprovement,
                    label = if (language == AppLanguage.MARATHI) "नामजप" else "Naamjap",
                    value = if (entry.totalNaamjap > 0) {
                        if (language == AppLanguage.MARATHI) "${entry.totalNaamjap} मि." else "${entry.totalNaamjap} min"
                    } else {
                        if (language == AppLanguage.MARATHI) "० मि." else "0 min"
                    },
                    modifier = Modifier.weight(1f)
                )
                MetricPill(
                    icon = Icons.Default.Favorite,
                    label = if (language == AppLanguage.MARATHI) "प्रार्थना" else "Prayers",
                    value = "${entry.prarthana}",
                    modifier = Modifier.weight(1f)
                )
                MetricPill(
                    icon = Icons.Default.CheckCircle,
                    label = if (language == AppLanguage.MARATHI) "कृती" else "Habits",
                    value = "${entry.completedHabitsCount}/17",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricPill(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = ColorNavigations, // #D3EFFB
        border = BorderStroke(0.5.dp, ChaitanyaCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = ChaitanyaBluePrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = ChaitanyaBlueDark
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = ChaitanyaTextSecondary
            )
        }
    }
}

@Composable
private fun CategoryBreakdownCard(
    entry: DailyRoutineEntity,
    language: AppLanguage
) {
    // Phase 1 items completed
    var p1Done = 0
    if (entry.naamjapKuldevi > 0) p1Done++
    if (entry.naamjapGurudevDatta > 0) p1Done++
    if (entry.kapurItrUpay > 0) p1Done++
    if (entry.prarthana > 0) p1Done++
    if (entry.krutajnata > 0) p1Done++
    if (entry.aavaranKadhne > 0) p1Done++

    // Phase 2 items completed
    var p2Done = 0
    if (entry.bhavjagruti > 0) p2Done++
    if (entry.aatmanivedan > 0) p2Done++
    if (entry.manachaAadhavDone) p2Done++
    if (entry.dainandinLikhanDone) p2Done++
    if (entry.granthPathanDone) p2Done++

    // Phase 3 items completed
    var p3Done = 0
    if (entry.chukaVicharneDone) p3Done++
    if (entry.prayashchittaDone) p3Done++
    if (entry.saraniLikhanDone) p3Done++
    if (entry.chukaLihineDone) p3Done++
    if (entry.swayamsuchanaDone) p3Done++
    if (entry.kshamayachanaDone) p3Done++

    // Component is strictly white #FFFFFF
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("category_breakdown_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorComponentWhite // #FFFFFF
        ),
        border = BorderStroke(1.dp, ChaitanyaCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "टप्प्यांनुसार वर्गवारी (Category Breakdown)" else "Phase & Category Breakdown",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = ChaitanyaBluePrimary
            )
            Spacer(modifier = Modifier.height(14.dp))

            CategoryProgressRow(
                title = if (language == AppLanguage.MARATHI) "१. पायाभूत साधना व उपाय" else "1. Foundation & Remedies",
                completed = p1Done,
                total = 6,
                icon = Icons.Default.SelfImprovement
            )
            Spacer(modifier = Modifier.height(10.dp))

            CategoryProgressRow(
                title = if (language == AppLanguage.MARATHI) "२. भाव व अंतर्मुखता" else "2. Devotion & Reflection",
                completed = p2Done,
                total = 5,
                icon = Icons.Default.Favorite
            )
            Spacer(modifier = Modifier.height(10.dp))

            CategoryProgressRow(
                title = if (language == AppLanguage.MARATHI) "३. दोष निर्मूलन व क्षमायाचना" else "3. Rectification & Humility",
                completed = p3Done,
                total = 6,
                icon = Icons.Default.Psychology
            )
        }
    }
}

@Composable
private fun CategoryProgressRow(
    title: String,
    completed: Int,
    total: Int,
    icon: ImageVector
) {
    val progress = (completed.toFloat() / total.toFloat()).coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = ChaitanyaBluePrimary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = ChaitanyaBlueDark
                )
            }
            Text(
                text = "$completed / $total",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = ColorActiveStatus // #FD0031
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = ColorActiveStatus, // #FD0031
            trackColor = ColorNavigations // #D3EFFB
        )
    }
}
