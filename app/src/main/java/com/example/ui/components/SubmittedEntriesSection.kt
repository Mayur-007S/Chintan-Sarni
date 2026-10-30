package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaSkyContainer
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite

@Composable
fun SubmittedEntriesSection(
    lastSubmittedEntry: DailyRoutineEntity?,
    allEntries: List<DailyRoutineEntity>,
    language: AppLanguage,
    onEntryClick: (DailyRoutineEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (lastSubmittedEntry == null && allEntries.isEmpty()) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("submitted_entries_section"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ColorNavigations, // #D3EFFB
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            tint = ColorActiveStatus, // #FD0031
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI) "नोंदवलेली माहिती (सेव्ह केलेल्या नोंदी)" else "Submitted Routine Records",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBluePrimary
                )
            }

            Text(
                text = if (language == AppLanguage.MARATHI) "तपशीलासाठी टॅप करा" else "Tap for details",
                style = MaterialTheme.typography.labelSmall,
                color = ChaitanyaTextSecondary
            )
        }

        // 1) Highlight Recently Submitted Entry if present
        if (lastSubmittedEntry != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEntryClick(lastSubmittedEntry) }
                    .testTag("card_last_submitted_entry"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorComponentWhite),
                border = BorderStroke(1.5.dp, ColorActiveStatus.copy(alpha = 0.7f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ColorActiveStatus // #FD0031
                            ) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "✓ आत्ताच सेव्ह झाली" else "✓ Just Submitted",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = ColorTextWhite,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = lastSubmittedEntry.date,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = ChaitanyaBluePrimary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View full detail",
                            tint = ColorActiveStatus,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Naamjap time pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ColorNavigations,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = ColorActiveStatus,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                val mins = lastSubmittedEntry.totalNaamjap
                                val timeText = if (mins < 60) {
                                    if (language == AppLanguage.MARATHI) "$mins मि." else "$mins min"
                                } else {
                                    val h = mins / 60
                                    val m = mins % 60
                                    if (language == AppLanguage.MARATHI) "${h}ता ${m}मि" else "${h}h ${m}m"
                                }
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "नामजप: $timeText" else "Naamjap: $timeText",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = ChaitanyaBlueDark,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Habits completed pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ChaitanyaSkyContainer,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SelfImprovement,
                                    contentDescription = null,
                                    tint = ChaitanyaBluePrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${lastSubmittedEntry.completedHabitsCount}/${lastSubmittedEntry.totalHabitsCount} " +
                                            if (language == AppLanguage.MARATHI) "सवयी" else "habits",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = ChaitanyaBlueDark,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.MARATHI)
                            "👉 संपूर्ण तपशील (नामजप वेळ, उपाय, ग्रंथ पठण इत्यादी) पाहण्यासाठी येथे टॅप करा"
                        else
                            "👉 Tap here to view full detailed routine breakdown",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = ColorActiveStatus
                    )
                }
            }
        }

        // 2) List other submitted entries (up to 4 recent)
        val otherEntries = allEntries
            .filter { it.date != lastSubmittedEntry?.date }
            .take(3)

        otherEntries.forEach { entry ->
            SubmittedEntryCompactCard(
                entry = entry,
                language = language,
                onClick = { onEntryClick(entry) }
            )
        }
    }
}

@Composable
private fun SubmittedEntryCompactCard(
    entry: DailyRoutineEntity,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("submitted_entry_compact_${entry.date}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorComponentWhite),
        border = BorderStroke(1.dp, ChaitanyaCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBluePrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                val mins = entry.totalNaamjap
                val timeFormatted = if (mins < 60) "$mins मि." else "${mins / 60} तास ${mins % 60} मि."
                Text(
                    text = "${entry.completedHabitsCount}/${entry.totalHabitsCount} सवयी • नामजप: $timeFormatted",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = ChaitanyaTextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ColorNavigations // #D3EFFB
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "तपशील" else "Details",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = ChaitanyaBlueDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View detail",
                    tint = ChaitanyaTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
