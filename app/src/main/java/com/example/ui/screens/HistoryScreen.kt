package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
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
import com.example.util.CsvExportManager

@Composable
fun HistoryScreen(
    viewModel: ChintanSaraniViewModel,
    onSelectDate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeLang by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val entries by viewModel.allEntries.collectAsStateWithLifecycle()

    // SAF Document Creator launcher to save CSV to local storage without broad storage permissions
    val saveDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        if (uri != null) {
            val csvData = CsvExportManager.generateCsv(entries, activeLang)
            val success = CsvExportManager.writeCsvToUri(context, uri, csvData)
            if (success) {
                viewModel.showSnackbar(
                    if (activeLang == AppLanguage.MARATHI)
                        "✅ CSV बॅकअप फाईल डिव्हाइसमध्ये यशस्वीरीत्या सेव्ह झाली!"
                    else
                        "✅ CSV backup successfully saved to device storage!"
                )
            } else {
                viewModel.showSnackbar(
                    if (activeLang == AppLanguage.MARATHI)
                        "❌ फाईल सेव्ह करताना त्रुटी आली."
                    else
                        "❌ Error saving CSV backup file."
                )
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBackground) // #F7F7F7
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (activeLang == AppLanguage.MARATHI) "मागील नोंदींचा इतिहास" else "Past Routine History",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = ChaitanyaBluePrimary
            )
            Text(
                text = if (activeLang == AppLanguage.MARATHI) "कोणत्याही तारखेवर टॅप करून पूर्वीची सारणी तपासा किंवा बदला" else "Tap on any date to inspect or update previous routine entries",
                style = MaterialTheme.typography.bodySmall,
                color = ChaitanyaTextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        // -------------------------------------------------------------
        // CSV EXPORT & BACKUP CARD
        // -------------------------------------------------------------
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_csv_export"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ColorComponentWhite), // #FFFFFF
                border = BorderStroke(1.dp, ChaitanyaCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ColorNavigations, // #D3EFFB
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.FileDownload,
                                    contentDescription = "Export CSV",
                                    tint = ColorActiveStatus, // #FD0031
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (activeLang == AppLanguage.MARATHI) "CSV बॅकअप व एक्सपोर्ट" else "Export History as CSV",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ChaitanyaBluePrimary
                            )
                            Text(
                                text = if (activeLang == AppLanguage.MARATHI)
                                    "सर्व दिवसांचा डेटा स्थानिक स्टोरेजमध्ये CSV फाईल म्हणून सेव्ह करा (${entries.size} नोंदी)"
                                else
                                    "Save all entries to local storage as CSV (${entries.size} entries)",
                                style = MaterialTheme.typography.bodySmall,
                                color = ChaitanyaTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (entries.isEmpty()) {
                                    viewModel.showSnackbar(
                                        if (activeLang == AppLanguage.MARATHI)
                                            "एक्सपोर्ट करण्यासाठी कोणतीही नोंद उपलब्ध नाही."
                                        else
                                            "No entries to export yet."
                                    )
                                } else {
                                    val defaultFileName = CsvExportManager.getDefaultBackupFileName()
                                    saveDocumentLauncher.launch(defaultFileName)
                                }
                            },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                                .testTag("btn_export_csv_storage"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorActiveStatus, // #FD0031
                                contentColor = ColorTextWhite       // #FFFFFF
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = ColorTextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (activeLang == AppLanguage.MARATHI) "CSV सेव्ह करा" else "Save CSV",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = ColorTextWhite
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                if (entries.isEmpty()) {
                                    viewModel.showSnackbar(
                                        if (activeLang == AppLanguage.MARATHI)
                                            "शेअर करण्यासाठी कोणतीही नोंद उपलब्ध नाही."
                                        else
                                            "No entries to share yet."
                                    )
                                } else {
                                    val csvContent = CsvExportManager.generateCsv(entries, activeLang)
                                    CsvExportManager.shareCsvContent(context, csvContent, activeLang)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_share_csv"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ColorActiveStatus.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = ColorActiveStatus
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (activeLang == AppLanguage.MARATHI) "शेअर करा" else "Share",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = ColorActiveStatus
                            )
                        }
                    }
                }
            }
        }

        if (entries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = ChaitanyaBluePrimary.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (activeLang == AppLanguage.MARATHI) "अद्याप कोणतीही नोंद सापडली नाही" else "No entries recorded yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ChaitanyaTextSecondary
                        )
                    }
                }
            }
        } else {
            items(entries, key = { it.date }) { entry ->
                HistoryItemCard(
                    entry = entry,
                    language = activeLang,
                    onClick = {
                        viewModel.selectDate(entry.date)
                        onSelectDate(entry.date)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HistoryItemCard(
    entry: DailyRoutineEntity,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("history_item_${entry.date}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorComponentWhite // #FFFFFF
        ),
        border = BorderStroke(1.dp, ChaitanyaCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.date,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ChaitanyaBluePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (entry.completedHabitsCount >= 12) ColorActiveStatus else ColorNavigations
                    ) {
                        Text(
                            text = "${entry.completedHabitsCount}/${entry.totalHabitsCount} सवयी",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (entry.completedHabitsCount >= 12) ColorTextWhite else ChaitanyaBlueDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SelfImprovement,
                            contentDescription = null,
                            tint = ColorActiveStatus, // #FD0031
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI)
                                "एकूण नामजप: %,d".format(entry.totalNaamjap)
                            else
                                "Naamjap: %,d".format(entry.totalNaamjap),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = ChaitanyaBlueDark
                        )
                    }

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodySmall,
                        color = ChaitanyaTextSecondary
                    )

                    Text(
                        text = "%.0f%% पूर्ण".format(entry.completionPercentage),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = ColorActiveStatus // #FD0031
                    )
                }

                if (entry.reflectionNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${entry.reflectionNotes}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = ChaitanyaTextSecondary,
                        maxLines = 1
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View detail",
                tint = ChaitanyaTextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
