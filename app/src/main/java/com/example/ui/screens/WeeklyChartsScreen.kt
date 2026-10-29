package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
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
import com.example.ui.viewmodel.WeeklyProgressDay

// Enum for Integer-based spiritual practices (Line Graph)
enum class IntegerPractice(val marathiLabel: String, val englishLabel: String) {
    TOTAL_NAAMJAP("एकूण नामजप", "Total Naamjap"),
    KULDEVI("कुलदेवी नामजप", "Kuldevi Naamjap"),
    GURUDEV_DATTA("दत्त नामजप", "Gurudev Datta"),
    KAPUR_ITR("कापूर इत्र उपाय", "Camphor & Attar"),
    PRARTHANA("प्रार्थना", "Prayers"),
    KRUTAJNATA("कृतज्ञता", "Gratitude"),
    AAVARAN("आवरण काढणे", "Aavaran"),
    BHAVJAGRUTI("भावजागृती", "Bhavjagruti"),
    AATMANIVEDAN("आत्मनिवेदन", "Atmanivedan"),
    MANACHA_AADHAV("मनाचा आढावा", "Mind Review"),
    DAINANDIN_LIKHAN("दैनंदिन लिखाण", "Daily Journaling"),
    GRANTH_PATHAN("ग्रंथ पठण", "Holy Reading"),
    CHUKA_VICHARNE("चुका विचारणे", "Mistakes Shared"),
    PRAYASHCHITTA("प्रायश्चित", "Penance"),
    CHUKA_LIHINE("चुका लिहिणे", "Writing Mistakes"),
    SWAYAMSUCHANA("स्वयंसूचना", "Auto-suggestion");

    fun getLabel(lang: AppLanguage): String = if (lang == AppLanguage.MARATHI) marathiLabel else englishLabel

    fun extractValue(entity: DailyRoutineEntity?): Int {
        if (entity == null) return 0
        return when (this) {
            TOTAL_NAAMJAP -> entity.totalNaamjap
            KULDEVI -> entity.naamjapKuldevi
            GURUDEV_DATTA -> entity.naamjapGurudevDatta
            KAPUR_ITR -> entity.kapurItrUpay
            PRARTHANA -> entity.prarthana
            KRUTAJNATA -> entity.krutajnata
            AAVARAN -> entity.aavaranKadhne
            BHAVJAGRUTI -> entity.bhavjagruti
            AATMANIVEDAN -> entity.aatmanivedan
            MANACHA_AADHAV -> entity.manachaAadhavCount
            DAINANDIN_LIKHAN -> entity.dainandinLikhanCount
            GRANTH_PATHAN -> entity.granthPathanCount
            CHUKA_VICHARNE -> entity.chukaVicharneCount
            PRAYASHCHITTA -> entity.prayashchittaCount
            CHUKA_LIHINE -> entity.chukaLihineCount
            SWAYAMSUCHANA -> entity.swayamsuchanaCount
        }
    }
}

// Enum for Boolean-based spiritual practices (Bar Chart)
enum class BooleanPractice(val marathiLabel: String, val englishLabel: String) {
    ALL_HABITS("सर्व सवयी एकत्रित", "All Boolean Habits"),
    SARANI_LIKHAN("सारणी लिखाण", "Sarani Likhan"),
    KSHAMAYACHANA("क्षमायाचना", "Forgiveness"),
    MANACHA_AADHAV("मनाचा आढावा", "Mind Review"),
    DAINANDIN_LIKHAN("दैनंदिन लिखाण", "Daily Journaling"),
    GRANTH_PATHAN("दैनिक ग्रंथ पठण", "Holy Reading"),
    CHUKA_VICHARNE("चुका विचारणे व सांगणे", "Mistakes Shared"),
    PRAYASHCHITTA("प्रायश्चित व शिक्षा", "Penance"),
    CHUKA_LIHINE("चुका लिहिणे", "Writing Mistakes"),
    SWAYAMSUCHANA("स्वयंसूचना सत्र", "Auto-suggestion");

    fun getLabel(lang: AppLanguage): String = if (lang == AppLanguage.MARATHI) marathiLabel else englishLabel

    fun extractBoolean(entity: DailyRoutineEntity?): Boolean {
        if (entity == null) return false
        return when (this) {
            ALL_HABITS -> entity.completedHabitsCount > 0
            SARANI_LIKHAN -> entity.saraniLikhanDone
            KSHAMAYACHANA -> entity.kshamayachanaDone
            MANACHA_AADHAV -> entity.manachaAadhavDone
            DAINANDIN_LIKHAN -> entity.dainandinLikhanDone
            GRANTH_PATHAN -> entity.granthPathanDone
            CHUKA_VICHARNE -> entity.chukaVicharneDone
            PRAYASHCHITTA -> entity.prayashchittaDone
            CHUKA_LIHINE -> entity.chukaLihineDone
            SWAYAMSUCHANA -> entity.swayamsuchanaDone
        }
    }

    fun countCompletedBooleans(entity: DailyRoutineEntity?): Int {
        if (entity == null) return 0
        var count = 0
        if (entity.saraniLikhanDone) count++
        if (entity.kshamayachanaDone) count++
        if (entity.manachaAadhavDone) count++
        if (entity.dainandinLikhanDone) count++
        if (entity.granthPathanDone) count++
        if (entity.chukaVicharneDone) count++
        if (entity.prayashchittaDone) count++
        if (entity.chukaLihineDone) count++
        if (entity.swayamsuchanaDone) count++
        return count
    }
}

@Composable
fun WeeklyChartsScreen(
    viewModel: ChintanSaraniViewModel,
    modifier: Modifier = Modifier
) {
    val activeLang by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val weeklyDays by viewModel.weeklyProgressDays.collectAsStateWithLifecycle()
    val weekOffset by viewModel.weekOffset.collectAsStateWithLifecycle()

    var selectedIntegerPractice by remember { mutableStateOf(IntegerPractice.TOTAL_NAAMJAP) }
    var selectedBooleanPractice by remember { mutableStateOf(BooleanPractice.ALL_HABITS) }

    // Recharts-style interactive inspection indices
    var inspectedLineDayIndex by remember { mutableIntStateOf(-1) }
    var inspectedBarDayIndex by remember { mutableIntStateOf(-1) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBackground) // #F7F7F7
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Screen Header & Week Range Navigator
            WeekNavigatorCard(
                weeklyDays = weeklyDays,
                weekOffset = weekOffset,
                language = activeLang,
                onPrevWeek = { viewModel.changeWeek(-1) },
                onNextWeek = { viewModel.changeWeek(1) },
                onResetCurrentWeek = { viewModel.resetToCurrentWeek() }
            )
        }

        // Section 1: Recharts Line Graph for Integer-Based Practices
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_integer_line_chart"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ColorComponentWhite), // #FFFFFF
                border = BorderStroke(1.dp, ChaitanyaCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header with Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ColorNavigations, // #D3EFFB
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ShowChart,
                                        contentDescription = "Line Graph",
                                        tint = ColorActiveStatus, // #FD0031
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.lineChartHeading(activeLang),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = ChaitanyaBluePrimary
                                )
                                Text(
                                    text = if (activeLang == AppLanguage.MARATHI) "सलग सात दिवसांचा कल (Recharts Line Graph)" else "7-Day Trend (Recharts Line Graph)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ChaitanyaTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Integer Practice Selector Chips (Scrollable row)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IntegerPractice.values().forEach { practice ->
                            val isSelected = selectedIntegerPractice == practice
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) ColorActiveStatus else ColorNavigations, // #FD0031 or #D3EFFB
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        selectedIntegerPractice = practice
                                        inspectedLineDayIndex = -1
                                    }
                                    .testTag("chip_practice_${practice.name.lowercase()}")
                            ) {
                                Text(
                                    text = practice.getLabel(activeLang),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSelected) ColorTextWhite else ChaitanyaBlueDark,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stat summary pills for this integer practice
                    val values = weeklyDays.map { selectedIntegerPractice.extractValue(it.entry) }
                    val totalSum = values.sum()
                    val avgValue = if (values.isNotEmpty()) totalSum / values.size else 0
                    val maxVal = values.maxOrNull() ?: 0

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricPill(
                            label = AppStrings.weekTotalLabel(activeLang),
                            value = "%,d".format(totalSum),
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricPill(
                            label = AppStrings.dailyAverageLabel(activeLang),
                            value = "%,d".format(avgValue),
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricPill(
                            label = if (activeLang == AppLanguage.MARATHI) "कमाल" else "Peak",
                            value = "%,d".format(maxVal),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Recharts Line Chart Canvas Component
                    RechartsLineChart(
                        weeklyDays = weeklyDays,
                        practice = selectedIntegerPractice,
                        language = activeLang,
                        inspectedIndex = inspectedLineDayIndex,
                        onInspectIndex = { inspectedLineDayIndex = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                    )

                    // Recharts Tooltip Inspector Details Card
                    AnimatedVisibility(
                        visible = inspectedLineDayIndex in weeklyDays.indices,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        if (inspectedLineDayIndex in weeklyDays.indices) {
                            val inspectedDay = weeklyDays[inspectedLineDayIndex]
                            val count = selectedIntegerPractice.extractValue(inspectedDay.entry)
                            RechartsLineTooltipCard(
                                dayName = inspectedDay.dayLabel,
                                dateStr = inspectedDay.fullDateLabel,
                                practiceName = selectedIntegerPractice.getLabel(activeLang),
                                value = count,
                                language = activeLang
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Recharts Bar Chart for Boolean-Based Practices
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_boolean_bar_chart"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ColorComponentWhite), // #FFFFFF
                border = BorderStroke(1.dp, ChaitanyaCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header with Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ColorNavigations, // #D3EFFB
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.BarChart,
                                        contentDescription = "Bar Chart",
                                        tint = ColorActiveStatus, // #FD0031
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.barChartHeading(activeLang),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = ChaitanyaBluePrimary
                                )
                                Text(
                                    text = if (activeLang == AppLanguage.MARATHI) "सत्य/असत्य पूर्णता स्तंभ (Recharts Bar Chart)" else "Boolean Completion Columns (Recharts Bar Chart)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ChaitanyaTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Boolean Practice Selector Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BooleanPractice.values().forEach { practice ->
                            val isSelected = selectedBooleanPractice == practice
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) ColorActiveStatus else ColorNavigations, // #FD0031 or #D3EFFB
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        selectedBooleanPractice = practice
                                        inspectedBarDayIndex = -1
                                    }
                                    .testTag("chip_boolean_${practice.name.lowercase()}")
                            ) {
                                Text(
                                    text = practice.getLabel(activeLang),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSelected) ColorTextWhite else ChaitanyaBlueDark,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Boolean metric summary
                    val completedDaysCount = if (selectedBooleanPractice == BooleanPractice.ALL_HABITS) {
                        weeklyDays.count { (it.entry?.completedHabitsCount ?: 0) >= 10 }
                    } else {
                        weeklyDays.count { selectedBooleanPractice.extractBoolean(it.entry) }
                    }
                    val consistencyPercent = ((completedDaysCount.toFloat() / 7f) * 100f).toInt()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricPill(
                            label = AppStrings.completedDaysLabel(activeLang),
                            value = "$completedDaysCount/7 ${if (activeLang == AppLanguage.MARATHI) "दिवस" else "days"}",
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricPill(
                            label = if (activeLang == AppLanguage.MARATHI) "नियमितता दर" else "Consistency",
                            value = "$consistencyPercent%",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Recharts Bar Chart Canvas Component
                    RechartsBarChart(
                        weeklyDays = weeklyDays,
                        practice = selectedBooleanPractice,
                        language = activeLang,
                        inspectedIndex = inspectedBarDayIndex,
                        onInspectIndex = { inspectedBarDayIndex = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    // Recharts Bar Tooltip Inspector Details Card
                    AnimatedVisibility(
                        visible = inspectedBarDayIndex in weeklyDays.indices,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        if (inspectedBarDayIndex in weeklyDays.indices) {
                            val inspectedDay = weeklyDays[inspectedBarDayIndex]
                            RechartsBarTooltipCard(
                                dayName = inspectedDay.dayLabel,
                                dateStr = inspectedDay.fullDateLabel,
                                entry = inspectedDay.entry,
                                selectedPractice = selectedBooleanPractice,
                                language = activeLang
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Recharts Weekly Summary Card
        item {
            WeeklyInsightsCard(
                weeklyDays = weeklyDays,
                language = activeLang
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WeekNavigatorCard(
    weeklyDays: List<WeeklyProgressDay>,
    weekOffset: Int,
    language: AppLanguage,
    onPrevWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onResetCurrentWeek: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("week_navigator_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorComponentWhite), // #FFFFFF
        border = BorderStroke(1.dp, ChaitanyaCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevWeek,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("btn_prev_week")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = AppStrings.previousWeek(language),
                    tint = ChaitanyaBluePrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val startLabel = weeklyDays.firstOrNull()?.fullDateLabel ?: ""
                val endLabel = weeklyDays.lastOrNull()?.fullDateLabel ?: ""
                Text(
                    text = "$startLabel - $endLabel",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBluePrimary
                )

                if (weekOffset != 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ColorNavigations, // #D3EFFB
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onResetCurrentWeek() }
                    ) {
                        Text(
                            text = AppStrings.currentWeek(language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = ColorActiveStatus, // #FD0031
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Text(
                        text = AppStrings.currentWeek(language),
                        style = MaterialTheme.typography.labelSmall,
                        color = ChaitanyaTextSecondary
                    )
                }
            }

            IconButton(
                onClick = onNextWeek,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("btn_next_week")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = AppStrings.nextWeek(language),
                    tint = ChaitanyaBluePrimary
                )
            }
        }
    }
}

@Composable
private fun StatMetricPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = ColorNavigations.copy(alpha = 0.5f), // Soft #D3EFFB
        border = BorderStroke(1.dp, ColorNavigations)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = ChaitanyaTextSecondary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = ColorActiveStatus, // #FD0031
                maxLines = 1
            )
        }
    }
}

// -------------------------------------------------------------
// RECHARTS LINE GRAPH COMPONENT (Canvas Monotone Cubic Spline)
// -------------------------------------------------------------
@Composable
private fun RechartsLineChart(
    weeklyDays: List<WeeklyProgressDay>,
    practice: IntegerPractice,
    language: AppLanguage,
    inspectedIndex: Int,
    onInspectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val values = remember(weeklyDays, practice) {
        weeklyDays.map { practice.extractValue(it.entry) }
    }
    val maxVal = remember(values) {
        (values.maxOrNull() ?: 1).coerceAtLeast(10)
    }

    Box(
        modifier = modifier
            .testTag("recharts_line_canvas_box")
            .pointerInput(weeklyDays) {
                detectTapGestures(
                    onTap = { offset ->
                        val slotWidth = size.width / 7f
                        val index = (offset.x / slotWidth).toInt().coerceIn(0, 6)
                        onInspectIndex(if (index == inspectedIndex) -1 else index)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val leftPadding = 36f
            val rightPadding = 16f
            val topPadding = 20f
            val bottomPadding = 38f // for day labels

            val chartWidth = width - leftPadding - rightPadding
            val chartHeight = height - topPadding - bottomPadding
            val slotWidth = chartWidth / 6f

            // 1. Cartesian Grid Lines (Recharts CartesianGrid strokeDasharray="3 3")
            val gridSteps = 4
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
            val gridColor = Color(0xFFE2E8F0)

            for (i in 0..gridSteps) {
                val y = topPadding + chartHeight * (i.toFloat() / gridSteps)
                drawLine(
                    color = gridColor,
                    start = Offset(leftPadding, y),
                    end = Offset(width - rightPadding, y),
                    strokeWidth = 1.2f,
                    pathEffect = dashEffect
                )
            }

            // Points calculation
            val points = mutableListOf<Offset>()
            for (i in 0 until 7) {
                val x = leftPadding + i * slotWidth
                val norm = if (maxVal > 0) (values[i].toFloat() / maxVal).coerceIn(0f, 1f) else 0f
                val y = topPadding + chartHeight * (1f - norm)
                points.add(Offset(x, y))
            }

            // 2. Recharts Area Gradient Fill (smooth curve down to baseline)
            if (points.isNotEmpty()) {
                val areaPath = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val midX = (p0.x + p1.x) / 2f
                        cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                    }
                    // Close down to baseline
                    lineTo(points.last().x, topPadding + chartHeight)
                    lineTo(points.first().x, topPadding + chartHeight)
                    close()
                }

                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ColorActiveStatus.copy(alpha = 0.28f), // #FD0031 with alpha
                            ColorActiveStatus.copy(alpha = 0.02f)
                        ),
                        startY = topPadding,
                        endY = topPadding + chartHeight
                    )
                )

                // 3. Recharts Line Stroke (smooth curve stroke)
                val linePath = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val midX = (p0.x + p1.x) / 2f
                        cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                    }
                }

                drawPath(
                    path = linePath,
                    color = ColorActiveStatus, // #FD0031
                    style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // 4. Reference Cursor Line if inspected
            if (inspectedIndex in points.indices) {
                val inspectedX = points[inspectedIndex].x
                drawLine(
                    color = ColorActiveStatus.copy(alpha = 0.6f),
                    start = Offset(inspectedX, topPadding),
                    end = Offset(inspectedX, topPadding + chartHeight),
                    strokeWidth = 1.8f,
                    pathEffect = dashEffect
                )
            }

            // 5. Data Points (Circles)
            points.forEachIndexed { i, pt ->
                val isInspected = i == inspectedIndex
                if (isInspected) {
                    // Outer glow circle
                    drawCircle(
                        color = ColorActiveStatus.copy(alpha = 0.25f),
                        radius = 11.dp.toPx(),
                        center = pt
                    )
                }
                // White fill circle
                drawCircle(
                    color = ColorComponentWhite, // #FFFFFF
                    radius = if (isInspected) 6.dp.toPx() else 4.5.dp.toPx(),
                    center = pt
                )
                // Border in #FD0031
                drawCircle(
                    color = ColorActiveStatus, // #FD0031
                    radius = if (isInspected) 6.dp.toPx() else 4.5.dp.toPx(),
                    center = pt,
                    style = Stroke(width = 2.5.dp.toPx())
                )
            }
        }

        // X-Axis Day Labels Row at bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 28.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weeklyDays.forEachIndexed { index, day ->
                val isSelected = index == inspectedIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        onInspectIndex(if (isSelected) -1 else index)
                    }
                ) {
                    Text(
                        text = day.dayLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        ),
                        color = if (isSelected) ColorActiveStatus else ChaitanyaTextSecondary
                    )
                    Text(
                        text = day.fullDateLabel.split(" ").firstOrNull() ?: "",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = if (isSelected) ColorActiveStatus else ChaitanyaTextSecondary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RechartsLineTooltipCard(
    dayName: String,
    dateStr: String,
    practiceName: String,
    value: Int,
    language: AppLanguage
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .testTag("recharts_line_tooltip"),
        shape = RoundedCornerShape(12.dp),
        color = ColorNavigations.copy(alpha = 0.7f), // Soft #D3EFFB
        border = BorderStroke(1.dp, ColorActiveStatus.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(ColorActiveStatus, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$dayName, $dateStr",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = ChaitanyaBluePrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = practiceName,
                    style = MaterialTheme.typography.bodySmall,
                    color = ChaitanyaTextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "%,d".format(value),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    ),
                    color = ColorActiveStatus // #FD0031
                )
                Text(
                    text = if (language == AppLanguage.MARATHI) "संख्या / वेळा" else "Count",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = ChaitanyaTextSecondary
                )
            }
        }
    }
}

// -------------------------------------------------------------
// RECHARTS BAR CHART COMPONENT (Canvas Rounded Columns)
// -------------------------------------------------------------
@Composable
private fun RechartsBarChart(
    weeklyDays: List<WeeklyProgressDay>,
    practice: BooleanPractice,
    language: AppLanguage,
    inspectedIndex: Int,
    onInspectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val barValues = remember(weeklyDays, practice) {
        weeklyDays.map { day ->
            if (practice == BooleanPractice.ALL_HABITS) {
                practice.countCompletedBooleans(day.entry)
            } else {
                if (practice.extractBoolean(day.entry)) 1 else 0
            }
        }
    }

    val maxBarScale = remember(practice) {
        if (practice == BooleanPractice.ALL_HABITS) 9f else 1f
    }

    Box(
        modifier = modifier
            .testTag("recharts_bar_canvas_box")
            .pointerInput(weeklyDays) {
                detectTapGestures(
                    onTap = { offset ->
                        val slotWidth = size.width / 7f
                        val index = (offset.x / slotWidth).toInt().coerceIn(0, 6)
                        onInspectIndex(if (index == inspectedIndex) -1 else index)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val leftPadding = 24f
            val rightPadding = 24f
            val topPadding = 24f
            val bottomPadding = 38f // for day labels

            val chartWidth = width - leftPadding - rightPadding
            val chartHeight = height - topPadding - bottomPadding
            val slotWidth = chartWidth / 7f
            val barWidth = (slotWidth * 0.52f).coerceIn(16f, 36f)

            // 1. Cartesian Grid Lines
            val gridSteps = 3
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
            val gridColor = Color(0xFFE2E8F0)

            for (i in 0..gridSteps) {
                val y = topPadding + chartHeight * (i.toFloat() / gridSteps)
                drawLine(
                    color = gridColor,
                    start = Offset(leftPadding, y),
                    end = Offset(width - rightPadding, y),
                    strokeWidth = 1.2f,
                    pathEffect = dashEffect
                )
            }

            // 2. Draw Bars with rounded top corners
            for (i in 0 until 7) {
                val cx = leftPadding + (i + 0.5f) * slotWidth
                val valRatio = (barValues[i].toFloat() / maxBarScale).coerceIn(0f, 1f)
                val barHeight = (chartHeight * valRatio).coerceAtLeast(6f)
                val topY = topPadding + (chartHeight - barHeight)
                val isSelected = i == inspectedIndex

                val barColor = if (barValues[i] > 0) {
                    if (isSelected) ColorActiveStatus else ColorActiveStatus.copy(alpha = 0.85f)
                } else {
                    ColorNavigations.copy(alpha = 0.6f) // empty bar #D3EFFB
                }

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(cx - barWidth / 2f, topY),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )

                // Outline highlight if selected
                if (isSelected) {
                    drawRoundRect(
                        color = ChaitanyaBluePrimary,
                        topLeft = Offset(cx - barWidth / 2f - 2f, topY - 2f),
                        size = Size(barWidth + 4f, barHeight + 2f),
                        cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }

        // X-Axis Day Labels Row at bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weeklyDays.forEachIndexed { index, day ->
                val isSelected = index == inspectedIndex
                val valCount = barValues[index]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        onInspectIndex(if (isSelected) -1 else index)
                    }
                ) {
                    // Quick tag above label
                    Text(
                        text = if (practice == BooleanPractice.ALL_HABITS) "$valCount" else if (valCount > 0) "✓" else "-",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = if (valCount > 0) ColorActiveStatus else ChaitanyaTextSecondary
                    )
                    Text(
                        text = day.dayLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        ),
                        color = if (isSelected) ColorActiveStatus else ChaitanyaTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun RechartsBarTooltipCard(
    dayName: String,
    dateStr: String,
    entry: DailyRoutineEntity?,
    selectedPractice: BooleanPractice,
    language: AppLanguage
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .testTag("recharts_bar_tooltip"),
        shape = RoundedCornerShape(14.dp),
        color = ColorComponentWhite, // #FFFFFF
        border = BorderStroke(1.dp, ColorNavigations)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$dayName ($dateStr)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBluePrimary
                )
                if (selectedPractice == BooleanPractice.ALL_HABITS) {
                    val count = selectedPractice.countCompletedBooleans(entry)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ColorActiveStatus, // #FD0031
                    ) {
                        Text(
                            text = "$count/9 ${if (language == AppLanguage.MARATHI) "पूर्ण" else "done"}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorTextWhite,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    val isDone = selectedPractice.extractBoolean(entry)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isDone) ColorActiveStatus else ColorNavigations
                    ) {
                        Text(
                            text = if (isDone) (if (language == AppLanguage.MARATHI) "होय (पूर्ण)" else "Yes (Done)")
                            else (if (language == AppLanguage.MARATHI) "नाही (अपूर्ण)" else "No (Not done)"),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDone) ColorTextWhite else ChaitanyaBlueDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // If ALL_HABITS mode: show breakdown of boolean items
            if (selectedPractice == BooleanPractice.ALL_HABITS) {
                Spacer(modifier = Modifier.height(10.dp))
                val items = listOf(
                    Pair("सारणी लिखाण", entry?.saraniLikhanDone == true),
                    Pair("क्षमायाचना ईश्वर व साधक", entry?.kshamayachanaDone == true),
                    Pair("मनाचा आढावा घेणे", entry?.manachaAadhavDone == true),
                    Pair("दैनंदिन लिखाण", entry?.dainandinLikhanDone == true),
                    Pair("दैनिक ग्रंथ पठण", entry?.granthPathanDone == true),
                    Pair("चुका विचारणे व सांगणे", entry?.chukaVicharneDone == true),
                    Pair("प्रायश्चित व शिक्षा घेणे", entry?.prayashchittaDone == true),
                    Pair("चुका लिहिणे", entry?.chukaLihineDone == true),
                    Pair("स्वयंसूचना सत्र", entry?.swayamsuchanaDone == true)
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items.forEach { (name, done) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (done) ChaitanyaBluePrimary else ChaitanyaTextSecondary
                            )
                            if (done) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Done",
                                        tint = ColorActiveStatus,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = if (language == AppLanguage.MARATHI) "होय" else "Yes",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = ColorActiveStatus
                                    )
                                }
                            } else {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "नाही" else "No",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChaitanyaTextSecondary.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// WEEKLY SUMMARY & INSIGHTS CARD
// -------------------------------------------------------------
@Composable
private fun WeeklyInsightsCard(
    weeklyDays: List<WeeklyProgressDay>,
    language: AppLanguage
) {
    val totalNaamjapWeek = weeklyDays.sumOf { it.entry?.totalNaamjap ?: 0 }
    val totalSaraniFilled = weeklyDays.count { it.entry?.saraniLikhanDone == true }
    val totalKshamayachana = weeklyDays.count { it.entry?.kshamayachanaDone == true }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("weekly_insights_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ColorComponentWhite), // #FFFFFF
        border = BorderStroke(1.dp, ChaitanyaCardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ColorNavigations, // #D3EFFB
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Summary",
                            tint = ColorActiveStatus,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI) "आठवड्याचा साधना सारांश" else "Weekly Sadhana Summary",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBluePrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InsightMetricBox(
                    title = if (language == AppLanguage.MARATHI) "एकूण नामजप" else "Total Japa",
                    value = "%,d".format(totalNaamjapWeek),
                    subtitle = if (language == AppLanguage.MARATHI) "७ दिवसांत" else "in 7 days",
                    modifier = Modifier.weight(1f)
                )
                InsightMetricBox(
                    title = if (language == AppLanguage.MARATHI) "सारणी लिखाण" else "Sarani Filed",
                    value = "$totalSaraniFilled/7",
                    subtitle = if (language == AppLanguage.MARATHI) "दिवस पूर्ण" else "days filled",
                    modifier = Modifier.weight(1f)
                )
                InsightMetricBox(
                    title = if (language == AppLanguage.MARATHI) "क्षमायाचना" else "Forgiveness",
                    value = "$totalKshamayachana/7",
                    subtitle = if (language == AppLanguage.MARATHI) "दिवस" else "days",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InsightMetricBox(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = ColorBackground, // #F7F7F7
        border = BorderStroke(1.dp, ChaitanyaCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = ChaitanyaTextSecondary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = ColorActiveStatus, // #FD0031
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = ChaitanyaTextSecondary.copy(alpha = 0.8f),
                maxLines = 1
            )
        }
    }
}
