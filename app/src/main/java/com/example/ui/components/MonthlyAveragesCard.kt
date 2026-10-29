package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite

@Composable
fun MonthlyAveragesCard(
    entries: List<DailyRoutineEntity>,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    testTag: String = "monthly_averages_card"
) {
    val totalDays = entries.size.coerceAtLeast(1)
    val avgNaamjap = entries.sumOf { it.totalNaamjap } / totalDays
    val avgPrarthana = entries.sumOf { it.prarthana } / totalDays
    val avgKrutajnata = entries.sumOf { it.krutajnata } / totalDays
    val avgHabitsCompleted = entries.sumOf { it.completedHabitsCount } / totalDays
    val avgCompletionPct = (avgHabitsCompleted.toFloat() / 17f * 100f).toInt().coerceIn(0, 100)

    // Component is strictly white #FFFFFF
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
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
                        text = if (language == AppLanguage.MARATHI) "मासिक सरासरी व प्रगती (३० दिवस)" else "Monthly Averages & Insights (30 Days)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ChaitanyaBluePrimary
                    )
                    Text(
                        text = if (language == AppLanguage.MARATHI) "एकूण नोंदवलेले दिवस: ${entries.size}" else "Total recorded days: ${entries.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ChaitanyaTextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ColorActiveStatus // #FD0031
                ) {
                    Text(
                        text = "$avgCompletionPct%",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = ColorTextWhite, // #FFFFFF
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricItem(
                    label = if (language == AppLanguage.MARATHI) "दैनिक नामजप" else "Daily Japa",
                    value = "$avgNaamjap",
                    icon = Icons.Default.SelfImprovement,
                    modifier = Modifier.weight(1f)
                )
                StatMetricItem(
                    label = if (language == AppLanguage.MARATHI) "दैनिक प्रार्थना" else "Daily Prayers",
                    value = "$avgPrarthana",
                    icon = Icons.Default.Favorite,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricItem(
                    label = if (language == AppLanguage.MARATHI) "दैनिक कृतज्ञता" else "Gratitude",
                    value = "$avgKrutajnata",
                    icon = Icons.Default.AutoAwesome,
                    modifier = Modifier.weight(1f)
                )
                StatMetricItem(
                    label = if (language == AppLanguage.MARATHI) "साधना सरासरी" else "Habits / Day",
                    value = "$avgHabitsCompleted / 17",
                    icon = Icons.Default.DateRange,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatMetricItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = ColorComponentWhite,
        border = BorderStroke(1.dp, ChaitanyaCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ColorNavigations, // #D3EFFB
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = ChaitanyaBluePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBlueDark
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = ChaitanyaTextSecondary
                )
            }
        }
    }
}
