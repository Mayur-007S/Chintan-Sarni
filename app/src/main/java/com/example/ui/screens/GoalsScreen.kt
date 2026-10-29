package com.example.ui.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.NotificationsPaused
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.HabitTargetEntity
import com.example.localization.AppLanguage
import com.example.notification.ReminderManager
import com.example.notification.ReminderSettings
import com.example.ui.components.CounterItemCard
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
fun GoalsScreen(
    viewModel: ChintanSaraniViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeLang by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val initialTargets by viewModel.habitTargets.collectAsStateWithLifecycle()

    var reminderSettings by remember { mutableStateOf(ReminderManager.getSettings(context)) }

    // Android 13+ Notification permission launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val updated = reminderSettings.copy(enabled = true)
            reminderSettings = updated
            ReminderManager.saveSettings(context, true, updated.hour, updated.minute)
            viewModel.showSnackbar(
                if (activeLang == AppLanguage.MARATHI) "दैनिक स्मरणपत्र चालू केले आहे!" else "Daily reminder enabled!"
            )
        } else {
            viewModel.showSnackbar(
                if (activeLang == AppLanguage.MARATHI) "कृपया स्मरणपत्रासाठी परवानगी द्या." else "Notification permission is required for reminders."
            )
        }
    }

    var kuldeviTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetKuldeviNaamjap) }
    var gurudevDattaTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetGurudevDattaNaamjap) }
    var prarthanaTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetPrarthana) }
    var krutajnataTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetKrutajnata) }
    var kapurUpayTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetKapurItrUpay) }
    var aavaranTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetAavaranKadhne) }
    var bhavjagrutiTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetBhavjagruti) }
    var aatmanivedanTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetAatmanivedan) }
    var swayamsuchanaTarget by remember(initialTargets) { mutableIntStateOf(initialTargets.targetSwayamsuchana) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBackground) // #F7F7F7
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (activeLang == AppLanguage.MARATHI) "दैनिक साधना ध्येय व स्मरणपत्र" else "Habit Targets & Daily Reminder",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = ChaitanyaBluePrimary
            )
            Text(
                text = if (activeLang == AppLanguage.MARATHI) "आपल्या क्षमतेनुसार दररोजचे वैयक्तिक लक्ष्य व स्मरणपत्र वेळ ठरवा" else "Set customizable daily targets and notification reminder time",
                style = MaterialTheme.typography.bodySmall,
                color = ChaitanyaTextSecondary
            )
        }

        // -------------------------------------------------------------
        // LOCAL NOTIFICATION SYSTEM CARD
        // -------------------------------------------------------------
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_daily_reminder"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ColorComponentWhite), // #FFFFFF
                border = BorderStroke(1.dp, ChaitanyaCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Title Row with Bell Icon & Toggle Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (reminderSettings.enabled) ColorNavigations else ColorBackground, // #D3EFFB
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (reminderSettings.enabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsPaused,
                                        contentDescription = "Reminder Notification",
                                        tint = if (reminderSettings.enabled) ColorActiveStatus else ChaitanyaTextSecondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (activeLang == AppLanguage.MARATHI) "दैनिक स्मरणपत्र (Reminder)" else "Daily Routine Reminder",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = ChaitanyaBluePrimary
                                )
                                Text(
                                    text = if (reminderSettings.enabled) {
                                        if (activeLang == AppLanguage.MARATHI) "दररोज निश्चित वेळेवर सूचना येईल" else "Reminder active daily at set time"
                                    } else {
                                        if (activeLang == AppLanguage.MARATHI) "स्मरणपत्र बंद आहे" else "Reminders turned off"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (reminderSettings.enabled) ColorActiveStatus else ChaitanyaTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = reminderSettings.enabled,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.POST_NOTIFICATIONS
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        val updated = reminderSettings.copy(enabled = true)
                                        reminderSettings = updated
                                        ReminderManager.saveSettings(context, true, updated.hour, updated.minute)
                                        viewModel.showSnackbar(
                                            if (activeLang == AppLanguage.MARATHI) "दैनिक स्मरणपत्र चालू केले आहे!" else "Daily reminder enabled!"
                                        )
                                    }
                                } else {
                                    val updated = reminderSettings.copy(enabled = false)
                                    reminderSettings = updated
                                    ReminderManager.saveSettings(context, false, updated.hour, updated.minute)
                                    viewModel.showSnackbar(
                                        if (activeLang == AppLanguage.MARATHI) "दैनिक स्मरणपत्र बंद केले आहे." else "Daily reminder turned off."
                                    )
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ColorTextWhite,
                                checkedTrackColor = ColorActiveStatus, // #FD0031
                                uncheckedThumbColor = ChaitanyaTextSecondary,
                                uncheckedTrackColor = ColorNavigations.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.testTag("switch_daily_reminder")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Configurable Time Row
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = ColorBackground, // #F7F7F7
                        border = BorderStroke(1.dp, ChaitanyaCardBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Time",
                                    tint = ChaitanyaBluePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (activeLang == AppLanguage.MARATHI) "स्मरणपत्र वेळ" else "Reminder Time",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ChaitanyaTextSecondary
                                    )
                                    Text(
                                        text = reminderSettings.formattedTime,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        color = ColorActiveStatus // #FD0031
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    TimePickerDialog(
                                        context,
                                        { _, selectedHour, selectedMinute ->
                                            val updated = reminderSettings.copy(hour = selectedHour, minute = selectedMinute)
                                            reminderSettings = updated
                                            ReminderManager.saveSettings(context, updated.enabled, selectedHour, selectedMinute)
                                            viewModel.showSnackbar(
                                                if (activeLang == AppLanguage.MARATHI)
                                                    "स्मरणपत्र वेळ ${updated.formattedTime} अशी निश्चित केली आहे."
                                                else
                                                    "Reminder set for ${updated.formattedTime}."
                                            )
                                        },
                                        reminderSettings.hour,
                                        reminderSettings.minute,
                                        false // 12-hour AM/PM format
                                    ).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorNavigations, // #D3EFFB
                                    contentColor = ChaitanyaBlueDark
                                ),
                                modifier = Modifier.testTag("btn_change_reminder_time")
                            ) {
                                Text(
                                    text = if (activeLang == AppLanguage.MARATHI) "वेळ बदला" else "Change Time",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = ChaitanyaBlueDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Instant Test Notification Button
                    OutlinedButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                ReminderManager.showReminderNotification(
                                    context = context,
                                    isTest = true,
                                    language = activeLang
                                )
                                viewModel.showSnackbar(
                                    if (activeLang == AppLanguage.MARATHI)
                                        "🔔 चाचणी स्मरणपत्र पाठवले आहे! कृपया स्टेटस बार तपासा."
                                    else
                                        "🔔 Test reminder sent! Check your notification tray."
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_test_reminder"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ColorActiveStatus.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Test Notification",
                            tint = ColorActiveStatus,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (activeLang == AppLanguage.MARATHI)
                                "आत्ताच चाचणी स्मरणपत्र पाठवा (Test Reminder)"
                            else
                                "Send Test Reminder Notification Now",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ColorActiveStatus // #FD0031
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // CUSTOM HABIT TARGETS
        // -------------------------------------------------------------
        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "कुलदेवी नामजप ध्येय" else "Kuldevi Naamjap Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "दररोज पूर्ण करावयाचा नामजप" else "Daily chanting goal",
                value = kuldeviTarget,
                onValueChange = { kuldeviTarget = it },
                isJapaPreset = true,
                testTag = "target_kuldevi"
            )
        }

        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "श्री गुरुदेव दत्त नामजप ध्येय" else "Shri Gurudev Datta Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "दररोज पूर्ण करावयाचा नामजप" else "Daily chanting goal",
                value = gurudevDattaTarget,
                onValueChange = { gurudevDattaTarget = it },
                isJapaPreset = true,
                testTag = "target_datta"
            )
        }

        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "कापूर इत्र उपाय ध्येय" else "Camphor & Attar Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "दिवसातून किती वेळा उपाय करायचे" else "Daily remedy frequency",
                value = kapurUpayTarget,
                onValueChange = { kapurUpayTarget = it },
                testTag = "target_kapur"
            )
        }

        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "प्रार्थना संख्या ध्येय" else "Prayers Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "दररोज भावपूर्ण प्रार्थनांची संख्या" else "Daily prayers frequency",
                value = prarthanaTarget,
                onValueChange = { prarthanaTarget = it },
                testTag = "target_prarthana"
            )
        }

        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "कृतज्ञता व्यक्त करण्याचे ध्येय" else "Gratitude Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "दररोज कृतज्ञता व्यक्त करण्याची संख्या" else "Daily gratitude frequency",
                value = krutajnataTarget,
                onValueChange = { krutajnataTarget = it },
                testTag = "target_krutajnata"
            )
        }

        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "आवरण काढणे ध्येय" else "Aavaran Removal Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "दररोज आवरण काढण्याची संख्या" else "Daily black energy removal frequency",
                value = aavaranTarget,
                onValueChange = { aavaranTarget = it },
                testTag = "target_aavaran"
            )
        }

        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "भावजागृती चे प्रयत्न ध्येय" else "Bhavjagruti Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "भाव वृद्धीसाठी प्रयत्न संख्या" else "Daily efforts for spiritual emotion",
                value = bhavjagrutiTarget,
                onValueChange = { bhavjagrutiTarget = it },
                testTag = "target_bhavjagruti"
            )
        }

        item {
            CounterItemCard(
                title = if (activeLang == AppLanguage.MARATHI) "स्वयंसूचना सत्र ध्येय" else "Autosuggestion Sessions Target",
                subtitle = if (activeLang == AppLanguage.MARATHI) "दोष निर्मूलन स्वयंसूचना सत्रे" else "Daily autosuggestion sessions",
                value = swayamsuchanaTarget,
                onValueChange = { swayamsuchanaTarget = it },
                testTag = "target_swayamsuchana"
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    viewModel.updateHabitTargets(
                        HabitTargetEntity(
                            targetKuldeviNaamjap = kuldeviTarget,
                            targetGurudevDattaNaamjap = gurudevDattaTarget,
                            targetPrarthana = prarthanaTarget,
                            targetKrutajnata = krutajnataTarget,
                            targetKapurItrUpay = kapurUpayTarget,
                            targetAavaranKadhne = aavaranTarget,
                            targetBhavjagruti = bhavjagrutiTarget,
                            targetAatmanivedan = aatmanivedanTarget,
                            targetSwayamsuchana = swayamsuchanaTarget
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_targets_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorActiveStatus, // #FD0031
                    contentColor = ColorTextWhite       // #FFFFFF
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = ColorTextWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (activeLang == AppLanguage.MARATHI) "ध्येय जतन करा (Save Goals)" else "Save Target Goals",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ColorTextWhite // #FFFFFF
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
