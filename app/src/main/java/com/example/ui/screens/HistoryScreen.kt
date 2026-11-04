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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.components.RoutineDetailBottomSheet
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
import com.example.ui.viewmodel.PracticeFilter
import com.example.util.CsvExportManager

@Composable
fun HistoryScreen(
    viewModel: ChintanSaraniViewModel,
    onSelectDate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeLang by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val filteredEntries by viewModel.filteredHistoryEntries.collectAsStateWithLifecycle()
    val searchQuery by viewModel.historySearchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.historyPracticeFilter.collectAsStateWithLifecycle()
    val selectedDetailEntry by viewModel.selectedDetailEntry.collectAsStateWithLifecycle()
    var pendingDeleteEntry by remember { mutableStateOf<DailyRoutineEntity?>(null) }

    // SAF Document Creator launcher to save CSV to local storage without broad storage permissions
    val saveDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        if (uri != null) {
            val csvData = CsvExportManager.generateCsv(allEntries, activeLang)
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBackground) // #F7F7F7
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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
                    text = if (activeLang == AppLanguage.MARATHI)
                        "कोणत्याही तारखेवर टॅप करून पूर्वीची सारणी तपासा किंवा बदला"
                    else
                        "Tap on any date to inspect or update previous routine entries",
                    style = MaterialTheme.typography.bodySmall,
                    color = ChaitanyaTextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // -------------------------------------------------------------
            // SEARCH & FILTER PRACTICES SECTION
            // -------------------------------------------------------------
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_history_search_filter"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorComponentWhite),
                    border = BorderStroke(1.dp, ChaitanyaCardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setHistorySearchQuery(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_history_search"),
                            placeholder = {
                                Text(
                                    text = AppStrings.searchHistoryPlaceholder(activeLang),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ChaitanyaTextSecondary
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = ChaitanyaBluePrimary
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { viewModel.setHistorySearchQuery("") },
                                        modifier = Modifier.testTag("btn_clear_history_search")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = AppStrings.clearSearch(activeLang),
                                            tint = ChaitanyaTextSecondary
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ColorComponentWhite,
                                unfocusedContainerColor = ColorComponentWhite,
                                focusedBorderColor = ChaitanyaBluePrimary,
                                unfocusedBorderColor = ChaitanyaCardBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Practice Filter Pills (Horizontal scroll)
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("row_practice_filters"),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(PracticeFilter.values()) { filter ->
                                val isSelected = selectedFilter == filter
                                val label = getPracticeFilterLabel(filter, activeLang)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.setHistoryPracticeFilter(filter)
                                    },
                                    label = {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        )
                                    },
                                    modifier = Modifier.testTag("filter_chip_${filter.name.lowercase()}"),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ColorActiveStatus,
                                        selectedLabelColor = ColorTextWhite,
                                        containerColor = ColorNavigations.copy(alpha = 0.4f),
                                        labelColor = ChaitanyaBluePrimary
                                    ),
                                    border = BorderStroke(1.dp, if (isSelected) ColorActiveStatus else ChaitanyaCardBorder),
                                    shape = RoundedCornerShape(20.dp)
                                )
                            }
                        }

                        // Active filter count & reset action
                        if (searchQuery.isNotEmpty() || selectedFilter != PracticeFilter.ALL) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = AppStrings.filterResultsCount(
                                        activeLang,
                                        filteredEntries.size,
                                        allEntries.size
                                    ),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = ChaitanyaBlueDark,
                                    modifier = Modifier.weight(1f, fill = false),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = AppStrings.resetFilters(activeLang),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorActiveStatus
                                    ),
                                    modifier = Modifier
                                        .clickable { viewModel.resetHistoryFilters() }
                                        .padding(vertical = 4.dp, horizontal = 6.dp)
                                        .testTag("btn_reset_history_filters"),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
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
                                        "सर्व दिवसांचा डेटा स्थानिक स्टोरेजमध्ये CSV फाईल म्हणून सेव्ह करा (${allEntries.size} नोंदी)"
                                    else
                                        "Save all entries to local storage as CSV (${allEntries.size} entries)",
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
                                    if (allEntries.isEmpty()) {
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
                                    if (allEntries.isEmpty()) {
                                        viewModel.showSnackbar(
                                            if (activeLang == AppLanguage.MARATHI)
                                                "शेअर करण्यासाठी कोणतीही नोंद उपलब्ध नाही."
                                            else
                                                "No entries to share yet."
                                        )
                                    } else {
                                        val csvContent = CsvExportManager.generateCsv(allEntries, activeLang)
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

            // -------------------------------------------------------------
            // ENTRIES LIST OR EMPTY STATE
            // -------------------------------------------------------------
            if (allEntries.isEmpty()) {
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
                                text = if (activeLang == AppLanguage.MARATHI)
                                    "अद्याप कोणतीही नोंद सापडली नाही"
                                else
                                    "No entries recorded yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ChaitanyaTextSecondary
                            )
                        }
                    }
                }
            } else if (filteredEntries.isEmpty()) {
                // Empty search/filter results state
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .testTag("card_empty_history_results"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorComponentWhite),
                        border = BorderStroke(1.dp, ChaitanyaCardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = ColorNavigations,
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = ColorActiveStatus,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.noMatchingEntriesTitle(activeLang),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ChaitanyaBluePrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AppStrings.noMatchingEntriesSubtitle(activeLang),
                                style = MaterialTheme.typography.bodySmall,
                                color = ChaitanyaTextSecondary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.resetHistoryFilters() },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ChaitanyaBluePrimary,
                                    contentColor = ColorTextWhite
                                ),
                                modifier = Modifier.testTag("btn_empty_reset_filters")
                            ) {
                                Text(
                                    text = AppStrings.resetFilters(activeLang),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            } else {
                items(filteredEntries, key = { it.date }) { entry ->
                    HistoryItemCard(
                        entry = entry,
                        language = activeLang,
                        selectedFilter = selectedFilter,
                        onClick = {
                            viewModel.showEntryDetails(entry)
                        },
                        onDeleteClick = {
                            pendingDeleteEntry = entry
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Full Detailed Data Bottom Sheet when clicking any history item
        selectedDetailEntry?.let { entry ->
            RoutineDetailBottomSheet(
                entry = entry,
                language = activeLang,
                onDismiss = { viewModel.dismissEntryDetails() },
                onEditEntry = { entryToEdit ->
                    viewModel.loadEntryIntoDraft(entryToEdit)
                    onSelectDate(entryToEdit.date)
                },
                onDeleteEntry = { entryToDelete ->
                    viewModel.deleteEntry(entryToDelete.date)
                }
            )
        }

        // Confirmation dialog when deleting directly from a history card
        pendingDeleteEntry?.let { entryToDelete ->
            AlertDialog(
                onDismissRequest = { pendingDeleteEntry = null },
                title = {
                    Text(
                        text = AppStrings.deleteEntryConfirmTitle(activeLang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ChaitanyaBluePrimary
                    )
                },
                text = {
                    Text(
                        text = AppStrings.deleteEntryConfirmMessage(activeLang, entryToDelete.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ChaitanyaTextSecondary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteEntry(entryToDelete.date)
                            pendingDeleteEntry = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorActiveStatus,
                            contentColor = ColorTextWhite
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_confirm_delete_history_dialog")
                    ) {
                        Text(
                            text = AppStrings.deleteAction(activeLang),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { pendingDeleteEntry = null },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_cancel_delete_history_dialog")
                    ) {
                        Text(
                            text = AppStrings.deleteCancel(activeLang),
                            color = ChaitanyaBlueDark
                        )
                    }
                },
                shape = RoundedCornerShape(18.dp),
                containerColor = ColorComponentWhite
            )
        }
    }
}

private fun getPracticeFilterLabel(filter: PracticeFilter, lang: AppLanguage): String {
    return when (filter) {
        PracticeFilter.ALL -> AppStrings.filterAll(lang)
        PracticeFilter.NAAMJAP -> AppStrings.filterNaamjap(lang)
        PracticeFilter.KAPUR_ITR -> AppStrings.filterKapurItr(lang)
        PracticeFilter.PRARTHANA -> AppStrings.filterPrarthana(lang)
        PracticeFilter.KRUTAJNATA -> AppStrings.filterKrutajnata(lang)
        PracticeFilter.AAVARAN -> AppStrings.filterAavaran(lang)
        PracticeFilter.BHAVJAGRUTI -> AppStrings.filterBhavjagruti(lang)
        PracticeFilter.AATMANIVEDAN -> AppStrings.filterAatmanivedan(lang)
        PracticeFilter.MANACHA_AADHAV -> AppStrings.filterManachaAadhav(lang)
        PracticeFilter.GRANTH_PATHAN -> AppStrings.filterGranthPathan(lang)
        PracticeFilter.DAINANDIN_LIKHAN -> AppStrings.filterDainandinLikhan(lang)
        PracticeFilter.CHUKA_VICHARNE -> AppStrings.filterChukaVicharne(lang)
        PracticeFilter.PRAYASHCHITTA -> AppStrings.filterPrayashchitta(lang)
        PracticeFilter.SARANI_LIKHAN -> AppStrings.filterSaraniLikhan(lang)
        PracticeFilter.CHUKA_LIHINE -> AppStrings.filterChukaLihine(lang)
        PracticeFilter.SWAYAMSUCHANA -> AppStrings.filterSwayamsuchana(lang)
        PracticeFilter.KSHAMAYACHANA -> AppStrings.filterKshamayachana(lang)
    }
}

private fun getPracticeSummary(entry: DailyRoutineEntity, filter: PracticeFilter, lang: AppLanguage): String? {
    return when (filter) {
        PracticeFilter.ALL -> null
        PracticeFilter.NAAMJAP -> {
            val totalMins = entry.totalNaamjap
            if (lang == AppLanguage.MARATHI)
                "नामजप: $totalMins मि. (कुलदेवी: ${entry.naamjapKuldevi}, दत्त: ${entry.naamjapGurudevDatta})"
            else
                "Naamjap: $totalMins mins (Kuldevi: ${entry.naamjapKuldevi}, Datta: ${entry.naamjapGurudevDatta})"
        }
        PracticeFilter.KAPUR_ITR -> if (lang == AppLanguage.MARATHI) "कापूर इत्र उपाय: ${entry.kapurItrUpay} वेळा" else "Camphor remedy: ${entry.kapurItrUpay} times"
        PracticeFilter.PRARTHANA -> if (lang == AppLanguage.MARATHI) "प्रार्थना: ${entry.prarthana} वेळा" else "Prayers: ${entry.prarthana} times"
        PracticeFilter.KRUTAJNATA -> if (lang == AppLanguage.MARATHI) "कृतज्ञता: ${entry.krutajnata} वेळा" else "Gratitude: ${entry.krutajnata} times"
        PracticeFilter.AAVARAN -> if (lang == AppLanguage.MARATHI) "आवरण काढणे: ${entry.aavaranKadhne} वेळा" else "Aavaran: ${entry.aavaranKadhne} times"
        PracticeFilter.BHAVJAGRUTI -> if (lang == AppLanguage.MARATHI) "भावजागृती: ${entry.bhavjagruti} वेळा" else "Bhavjagruti: ${entry.bhavjagruti} times"
        PracticeFilter.AATMANIVEDAN -> if (lang == AppLanguage.MARATHI) "आत्मनिवेदन: ${entry.aatmanivedan} वेळा" else "Aatmanivedan: ${entry.aatmanivedan} times"
        PracticeFilter.MANACHA_AADHAV -> if (lang == AppLanguage.MARATHI) "मनाचा आढावा: पूर्ण ✓" else "Mind Review: Done ✓"
        PracticeFilter.GRANTH_PATHAN -> if (lang == AppLanguage.MARATHI) "ग्रंथ पठण: पूर्ण ✓" else "Holy Text: Done ✓"
        PracticeFilter.DAINANDIN_LIKHAN -> if (lang == AppLanguage.MARATHI) "दैनंदिन लिखाण: पूर्ण ✓" else "Journaling: Done ✓"
        PracticeFilter.CHUKA_VICHARNE -> if (lang == AppLanguage.MARATHI) "चुका विचारणे: पूर्ण ✓" else "Admitting Faults: Done ✓"
        PracticeFilter.PRAYASHCHITTA -> if (lang == AppLanguage.MARATHI) "प्रायश्चित घेणे: पूर्ण ✓" else "Penance: Done ✓"
        PracticeFilter.SARANI_LIKHAN -> if (lang == AppLanguage.MARATHI) "सारणी लिखाण: पूर्ण ✓" else "Sarani: Done ✓"
        PracticeFilter.CHUKA_LIHINE -> if (lang == AppLanguage.MARATHI) "चुका लिहिणे: पूर्ण ✓" else "Writing Mistakes: Done ✓"
        PracticeFilter.SWAYAMSUCHANA -> if (lang == AppLanguage.MARATHI) "स्वयंसूचना: पूर्ण ✓" else "Autosuggestion: Done ✓"
        PracticeFilter.KSHAMAYACHANA -> if (lang == AppLanguage.MARATHI) "क्षमायाचना: पूर्ण ✓" else "Forgiveness: Done ✓"
    }
}

@Composable
private fun HistoryItemCard(
    entry: DailyRoutineEntity,
    language: AppLanguage,
    selectedFilter: PracticeFilter,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
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
                        val totalMins = entry.totalNaamjap
                        val formattedTime = if (totalMins < 60) {
                            if (language == AppLanguage.MARATHI) "$totalMins मि." else "$totalMins mins"
                        } else {
                            val h = totalMins / 60
                            val m = totalMins % 60
                            if (language == AppLanguage.MARATHI) {
                                if (m == 0) "$h तास" else "$h तास $m मि."
                            } else {
                                if (m == 0) "$h hr" else "$h hr $m min"
                            }
                        }
                        Text(
                            text = if (language == AppLanguage.MARATHI)
                                "एकूण नामजप: $formattedTime"
                            else
                                "Naamjap: $formattedTime",
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

                // Practice highlight badge when a specific filter is selected
                val practiceSummary = getPracticeSummary(entry, selectedFilter, language)
                if (practiceSummary != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ColorNavigations.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, ChaitanyaCardBorder)
                    ) {
                        Text(
                            text = practiceSummary,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = ChaitanyaBluePrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("btn_delete_history_${entry.date}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = AppStrings.deleteButtonContentDescription(language),
                        tint = ColorActiveStatus,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(2.dp))

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View detail",
                    tint = ChaitanyaTextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
