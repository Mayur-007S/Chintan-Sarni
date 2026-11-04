package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DailyWizardScreen
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.WeeklyChartsScreen
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorBackground
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite
import com.example.ui.theme.ColorTopHeading
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ChintanSaraniViewModel

enum class MainTab {
    DAILY,
    WEEKLY_CHARTS,
    ANALYTICS,
    HISTORY,
    GOALS
}

class MainActivity : ComponentActivity() {

    private val viewModel: ChintanSaraniViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize notification channel and schedule reminder
        com.example.notification.ReminderManager.createNotificationChannel(this)
        val reminderSettings = com.example.notification.ReminderManager.getSettings(this)
        if (reminderSettings.enabled) {
            com.example.notification.ReminderManager.scheduleDailyReminder(
                this,
                reminderSettings.hour,
                reminderSettings.minute
            )
        }

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: ChintanSaraniViewModel) {
    var currentTab by rememberSaveable { mutableStateOf(MainTab.DAILY) }
    val activeLang by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle back button when on sub-tabs
    BackHandler(enabled = currentTab != MainTab.DAILY) {
        currentTab = MainTab.DAILY
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    val tabList = listOf(
        Pair(MainTab.DAILY, AppStrings.tabDaily(activeLang)),
        Pair(MainTab.WEEKLY_CHARTS, AppStrings.tabWeeklyCharts(activeLang)),
        Pair(MainTab.ANALYTICS, AppStrings.tabAnalytics(activeLang)),
        Pair(MainTab.HISTORY, AppStrings.tabHistory(activeLang)),
        Pair(MainTab.GOALS, AppStrings.tabGoals(activeLang))
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground, // #F7F7F7
        topBar = {
            Column {
                // Top Heading Component with exact #FFFFCB
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = ColorComponentWhite,
                                border = BorderStroke(1.dp, ChaitanyaBluePrimary.copy(alpha = 0.3f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "ॐ",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 19.sp
                                        ),
                                        color = ChaitanyaBluePrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f, fill = false)) {
                                Text(
                                    text = AppStrings.appTitle(activeLang),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    ),
                                    color = ChaitanyaBluePrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = AppStrings.appSubtitle(activeLang),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                    color = ChaitanyaTextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    },
                    actions = {
                        // One-tap Language Switcher Button
                        LanguageToggleButton(
                            currentLanguage = activeLang,
                            onToggle = { viewModel.toggleLanguage() }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ColorTopHeading, // #FFFFCB
                        titleContentColor = ChaitanyaBluePrimary,
                        actionIconContentColor = ChaitanyaBluePrimary
                    ),
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )

                // Navigation Tab Strip with exact #D3EFFB and active status #FD0031
                ScrollableTabRow(
                    selectedTabIndex = tabList.indexOfFirst { it.first == currentTab }.coerceAtLeast(0),
                    containerColor = ColorNavigations, // #D3EFFB
                    contentColor = ColorActiveStatus,  // #FD0031
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        val index = tabList.indexOfFirst { it.first == currentTab }.coerceAtLeast(0)
                        if (index < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                                height = 3.dp,
                                color = ColorActiveStatus // #FD0031
                            )
                        }
                    },
                    divider = {}
                ) {
                    tabList.forEach { (tab, label) ->
                        val isSelected = currentTab == tab
                        Tab(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            text = {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    ),
                                    color = if (isSelected) ColorActiveStatus else ChaitanyaBlueDark
                                )
                            },
                            modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Navigation Bottom Bar with exact #D3EFFB
            NavigationBar(
                containerColor = ColorNavigations, // #D3EFFB
                tonalElevation = 2.dp
            ) {
                val items = listOf(
                    NavigationTabItem(
                        tab = MainTab.DAILY,
                        label = AppStrings.tabDaily(activeLang),
                        selectedIcon = Icons.Filled.EditNote,
                        unselectedIcon = Icons.Outlined.EditNote,
                        testTag = "tab_daily"
                    ),
                    NavigationTabItem(
                        tab = MainTab.WEEKLY_CHARTS,
                        label = AppStrings.tabWeeklyCharts(activeLang),
                        selectedIcon = Icons.Filled.ShowChart,
                        unselectedIcon = Icons.Outlined.ShowChart,
                        testTag = "tab_weekly_charts"
                    ),
                    NavigationTabItem(
                        tab = MainTab.ANALYTICS,
                        label = AppStrings.tabAnalytics(activeLang),
                        selectedIcon = Icons.Filled.BarChart,
                        unselectedIcon = Icons.Outlined.BarChart,
                        testTag = "tab_analytics"
                    ),
                    NavigationTabItem(
                        tab = MainTab.HISTORY,
                        label = AppStrings.tabHistory(activeLang),
                        selectedIcon = Icons.Filled.History,
                        unselectedIcon = Icons.Outlined.History,
                        testTag = "tab_history"
                    ),
                    NavigationTabItem(
                        tab = MainTab.GOALS,
                        label = AppStrings.tabGoals(activeLang),
                        selectedIcon = Icons.Filled.TrackChanges,
                        unselectedIcon = Icons.Outlined.TrackChanges,
                        testTag = "tab_goals"
                    )
                )

                items.forEach { item ->
                    val isSelected = currentTab == item.tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = item.tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 10.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ColorActiveStatus, // #FD0031
                            selectedTextColor = ColorActiveStatus, // #FD0031
                            indicatorColor = ColorComponentWhite,  // White pill container
                            unselectedIconColor = ChaitanyaBlueDark,
                            unselectedTextColor = ChaitanyaBlueDark
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.DAILY -> DailyWizardScreen(viewModel = viewModel)
                MainTab.WEEKLY_CHARTS -> WeeklyChartsScreen(viewModel = viewModel)
                MainTab.ANALYTICS -> AnalyticsScreen(
                    viewModel = viewModel,
                    onNavigateToDaily = { dateStr ->
                        currentTab = MainTab.DAILY
                    }
                )
                MainTab.HISTORY -> HistoryScreen(
                    viewModel = viewModel,
                    onSelectDate = { dateStr ->
                        currentTab = MainTab.DAILY
                    }
                )
                MainTab.GOALS -> GoalsScreen(viewModel = viewModel)
            }
        }
    }
}

private data class NavigationTabItem(
    val tab: MainTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun LanguageToggleButton(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(end = 12.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onToggle() }
            .testTag("language_toggle_button"),
        shape = RoundedCornerShape(20.dp),
        color = ColorComponentWhite, // White component
        border = BorderStroke(1.dp, ChaitanyaBluePrimary.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Switch language",
                modifier = Modifier.size(16.dp),
                tint = ChaitanyaBluePrimary
            )
            Text(
                text = if (currentLanguage == AppLanguage.MARATHI) "मराठी" else "English",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = ChaitanyaBluePrimary
            )
        }
    }
}
