package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaSkyContainer
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorBackground
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite
import com.example.ui.theme.ColorTopHeading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailBottomSheet(
    entry: DailyRoutineEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onEditEntry: (DailyRoutineEntity) -> Unit,
    onDeleteEntry: ((DailyRoutineEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ColorBackground, // #F7F7F7
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("routine_detail_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Top Bar with Heading color #FFFFCB
            Surface(
                color = ColorTopHeading, // #FFFFCB
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ColorComponentWhite,
                            border = BorderStroke(1.dp, ChaitanyaBluePrimary.copy(alpha = 0.3f)),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "ॐ",
                                    fontWeight = FontWeight.Bold,
                                    color = ChaitanyaBluePrimary,
                                    fontSize = 18.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "तपशीलवार चिंतन सारणी" else "Full Routine Details",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ChaitanyaBluePrimary
                            )
                            Text(
                                text = entry.date,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = ChaitanyaTextSecondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (onDeleteEntry != null) {
                            IconButton(
                                onClick = { showDeleteConfirmDialog = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("btn_delete_detail_sheet_top")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = AppStrings.deleteButtonContentDescription(language),
                                    tint = ColorActiveStatus
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_close_detail_sheet")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = ChaitanyaBluePrimary
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Overview Badge Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorComponentWhite),
                    border = BorderStroke(1.dp, ChaitanyaCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "एकूण साधना पूर्णता" else "Overall Completion",
                                style = MaterialTheme.typography.labelMedium,
                                color = ChaitanyaTextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${entry.completedHabitsCount} / ${entry.totalHabitsCount} " +
                                        if (language == AppLanguage.MARATHI) "सवयी पूर्ण" else "habits done",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ChaitanyaBluePrimary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ColorActiveStatus // #FD0031
                        ) {
                            Text(
                                text = "%.0f%%".format(entry.completionPercentage),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ColorTextWhite, // #FFFFFF
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                // -----------------------------------------------------------------
                // SECTION 1: नामजप वेळ तपशील (Naamjap Time Details)
                // -----------------------------------------------------------------
                DetailSectionCard(
                    title = if (language == AppLanguage.MARATHI) "१) नामजप साधना (वेळ)" else "1) Naamjap Practice (Time)",
                    icon = Icons.Default.Timer
                ) {
                    DetailTimeRow(
                        label = if (language == AppLanguage.MARATHI) "कुलदेवी नामजप" else "Kuldevi Naamjap",
                        minutes = entry.naamjapKuldevi,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailTimeRow(
                        label = if (language == AppLanguage.MARATHI) "श्री गुरुदेव दत्त नामजप" else "Shri Gurudev Datta Naamjap",
                        minutes = entry.naamjapGurudevDatta,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailTimeRow(
                        label = if (language == AppLanguage.MARATHI) "एकूण नामजप वेळ" else "Total Naamjap Time",
                        minutes = entry.totalNaamjap,
                        isHighlight = true,
                        language = language
                    )
                }

                // -----------------------------------------------------------------
                // SECTION 2: आध्यात्मिक उपाय व प्रार्थना
                // -----------------------------------------------------------------
                DetailSectionCard(
                    title = if (language == AppLanguage.MARATHI) "२) आध्यात्मिक उपाय व प्रार्थना" else "2) Remedies & Prayers",
                    icon = Icons.Default.AutoAwesome
                ) {
                    DetailCountRow(
                        label = if (language == AppLanguage.MARATHI) "कापूर इत्र उपाय" else "Kapur Itr Remedy",
                        count = entry.kapurItrUpay,
                        unit = if (language == AppLanguage.MARATHI) "वेळा" else "times"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailCountRow(
                        label = if (language == AppLanguage.MARATHI) "प्रार्थना" else "Prayers",
                        count = entry.prarthana,
                        unit = if (language == AppLanguage.MARATHI) "वेळा" else "times"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailCountRow(
                        label = if (language == AppLanguage.MARATHI) "कृतज्ञता" else "Gratitude",
                        count = entry.krutajnata,
                        unit = if (language == AppLanguage.MARATHI) "वेळा" else "times"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailCountRow(
                        label = if (language == AppLanguage.MARATHI) "आवरण काढणे" else "Aavaran Kadhne",
                        count = entry.aavaranKadhne,
                        unit = if (language == AppLanguage.MARATHI) "वेळा" else "times"
                    )
                }

                // -----------------------------------------------------------------
                // SECTION 3: भाव व अंतर्मुखता
                // -----------------------------------------------------------------
                DetailSectionCard(
                    title = if (language == AppLanguage.MARATHI) "३) भाव व अंतर्मुखता" else "3) Devotion & Reflection",
                    icon = Icons.Default.SelfImprovement
                ) {
                    DetailCountRow(
                        label = if (language == AppLanguage.MARATHI) "भावजागृती चे प्रयत्न" else "Bhavjagruti Efforts",
                        count = entry.bhavjagruti,
                        unit = if (language == AppLanguage.MARATHI) "वेळा" else "times"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailCountRow(
                        label = if (language == AppLanguage.MARATHI) "आत्मनिवेदन" else "Aatmanivedan",
                        count = entry.aatmanivedan,
                        unit = if (language == AppLanguage.MARATHI) "वेळा" else "times"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanCountRow(
                        label = if (language == AppLanguage.MARATHI) "मनाचा आढावा घेणे" else "Review of Mind",
                        isDone = entry.manachaAadhavDone,
                        count = entry.manachaAadhavCount,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanCountRow(
                        label = if (language == AppLanguage.MARATHI) "दैनंदिन लिखाण" else "Daily Writing",
                        isDone = entry.dainandinLikhanDone,
                        count = entry.dainandinLikhanCount,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanCountRow(
                        label = if (language == AppLanguage.MARATHI) "दैनिक ग्रंथ पठण" else "Reading Holy Texts",
                        isDone = entry.granthPathanDone,
                        count = entry.granthPathanCount,
                        language = language
                    )
                }

                // -----------------------------------------------------------------
                // SECTION 4: दोष निर्मूलन व साधना
                // -----------------------------------------------------------------
                DetailSectionCard(
                    title = if (language == AppLanguage.MARATHI) "४) दोष निर्मूलन व साधना" else "4) Error Rectification & Humility",
                    icon = Icons.Default.MenuBook
                ) {
                    DetailBooleanCountRow(
                        label = if (language == AppLanguage.MARATHI) "चुका विचारणे व सांगणे" else "Asking & Sharing Mistakes",
                        isDone = entry.chukaVicharneDone,
                        count = entry.chukaVicharneCount,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanCountRow(
                        label = if (language == AppLanguage.MARATHI) "प्रायश्चित व शिक्षा घेणे" else "Repentance / Penance",
                        isDone = entry.prayashchittaDone,
                        count = entry.prayashchittaCount,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanRow(
                        label = if (language == AppLanguage.MARATHI) "सारणी लिखाण" else "Chart Writing",
                        isDone = entry.saraniLikhanDone,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanCountRow(
                        label = if (language == AppLanguage.MARATHI) "चुका लिहिणे" else "Writing Mistakes",
                        isDone = entry.chukaLihineDone,
                        count = entry.chukaLihineCount,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanCountRow(
                        label = if (language == AppLanguage.MARATHI) "स्वयंसूचना सत्र" else "Auto-suggestion Session",
                        isDone = entry.swayamsuchanaDone,
                        count = entry.swayamsuchanaCount,
                        language = language
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = ColorNavigations.copy(alpha = 0.5f))
                    DetailBooleanRow(
                        label = if (language == AppLanguage.MARATHI) "क्षमायाचना ईश्वर व साधक" else "Forgiveness to God & Seekers",
                        isDone = entry.kshamayachanaDone,
                        language = language
                    )
                }

                // -----------------------------------------------------------------
                // SECTION 5: चिंतन / मनोगत
                // -----------------------------------------------------------------
                if (entry.reflectionNotes.isNotBlank()) {
                    DetailSectionCard(
                        title = if (language == AppLanguage.MARATHI) "५) मनोगत व चिंतन नोंदी" else "5) Reflection Notes",
                        icon = Icons.Default.Notes
                    ) {
                        Text(
                            text = entry.reflectionNotes,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = ChaitanyaBlueDark,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ColorNavigations.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onEditEntry(entry)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.1f)
                            .height(48.dp)
                            .testTag("btn_edit_detail_entry"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorActiveStatus, // #FD0031
                            contentColor = ColorTextWhite       // #FFFFFF
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = ColorTextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "बदला" else "Edit",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorTextWhite
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            shareRoutineDetails(context, entry, language)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_share_detail_entry"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ChaitanyaBluePrimary.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = ChaitanyaBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "शेअर" else "Share",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = ChaitanyaBluePrimary
                        )
                    }

                    if (onDeleteEntry != null) {
                        OutlinedButton(
                            onClick = {
                                showDeleteConfirmDialog = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("btn_delete_detail_entry"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ColorActiveStatus.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = ColorActiveStatus,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.deleteAction(language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = ColorActiveStatus
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text(
                    text = AppStrings.deleteEntryConfirmTitle(language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBluePrimary
                )
            },
            text = {
                Text(
                    text = AppStrings.deleteEntryConfirmMessage(language, entry.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ChaitanyaTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDeleteEntry?.invoke(entry)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorActiveStatus,
                        contentColor = ColorTextWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_confirm_delete_detail_entry")
                ) {
                    Text(
                        text = AppStrings.deleteAction(language),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmDialog = false },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_cancel_delete_detail_entry")
                ) {
                    Text(
                        text = AppStrings.deleteCancel(language),
                        color = ChaitanyaBlueDark
                    )
                }
            },
            shape = RoundedCornerShape(18.dp),
            containerColor = ColorComponentWhite
        )
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorComponentWhite),
        border = BorderStroke(1.dp, ChaitanyaCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ColorNavigations, // #D3EFFB
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = ColorActiveStatus, // #FD0031
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ChaitanyaBluePrimary
                )
            }
            content()
        }
    }
}

@Composable
private fun DetailTimeRow(
    label: String,
    minutes: Int,
    isHighlight: Boolean = false,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isHighlight) ChaitanyaBlueDark else ChaitanyaTextSecondary
        )
        val formatted = if (minutes <= 0) {
            if (language == AppLanguage.MARATHI) "० मि." else "0 min"
        } else if (minutes < 60) {
            if (language == AppLanguage.MARATHI) "$minutes मि." else "$minutes min"
        } else {
            val h = minutes / 60
            val m = minutes % 60
            if (language == AppLanguage.MARATHI) {
                if (m == 0) "$h तास" else "$h तास $m मि."
            } else {
                if (m == 0) "$h hr" else "$h hr $m min"
            }
        }
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isHighlight) ColorActiveStatus else if (minutes > 0) ColorNavigations else ColorBackground
        ) {
            Text(
                text = formatted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = if (isHighlight) ColorTextWhite else if (minutes > 0) ChaitanyaBlueDark else ChaitanyaTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun DetailCountRow(
    label: String,
    count: Int,
    unit: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = ChaitanyaTextSecondary
        )
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (count > 0) ColorNavigations else ColorBackground
        ) {
            Text(
                text = "$count $unit",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (count > 0) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (count > 0) ChaitanyaBlueDark else ChaitanyaTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun DetailBooleanRow(
    label: String,
    isDone: Boolean,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = ChaitanyaTextSecondary
        )
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isDone) ColorActiveStatus else ColorBackground
        ) {
            Text(
                text = if (isDone) {
                    if (language == AppLanguage.MARATHI) "✓ पूर्ण केले" else "✓ Done"
                } else {
                    if (language == AppLanguage.MARATHI) "✗ नाही केले" else "✗ Not done"
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (isDone) ColorTextWhite else ChaitanyaTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun DetailBooleanCountRow(
    label: String,
    isDone: Boolean,
    count: Int,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = ChaitanyaTextSecondary
        )
        val text = if (isDone) {
            if (language == AppLanguage.MARATHI) "✓ झाले ($count वेळा)" else "✓ Done ($count)"
        } else if (count > 0) {
            "$count " + if (language == AppLanguage.MARATHI) "वेळा" else "times"
        } else {
            if (language == AppLanguage.MARATHI) "✗ नाही केले" else "✗ Not done"
        }

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isDone || count > 0) ColorNavigations else ColorBackground
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isDone || count > 0) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (isDone || count > 0) ChaitanyaBlueDark else ChaitanyaTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

private fun shareRoutineDetails(context: Context, entry: DailyRoutineEntity, language: AppLanguage) {
    val totalMins = entry.totalNaamjap
    val text = buildString {
        appendLine(if (language == AppLanguage.MARATHI) "🙏 चिंतन सारणी दैनंदिन साधना (${entry.date})" else "🙏 Daily Chintan Sarani Report (${entry.date})")
        appendLine("----------------------------------------")
        appendLine(if (language == AppLanguage.MARATHI) "• कुलदेवी नामजप: ${entry.naamjapKuldevi} मि." else "• Kuldevi Naamjap: ${entry.naamjapKuldevi} mins")
        appendLine(if (language == AppLanguage.MARATHI) "• श्री गुरुदेव दत्त नामजप: ${entry.naamjapGurudevDatta} मि." else "• Shri Gurudev Datta: ${entry.naamjapGurudevDatta} mins")
        appendLine(if (language == AppLanguage.MARATHI) "• एकूण नामजप वेळ: $totalMins मि." else "• Total Naamjap: $totalMins mins")
        appendLine(if (language == AppLanguage.MARATHI) "• एकूण पूर्ण सवयी: ${entry.completedHabitsCount}/${entry.totalHabitsCount} (%.0f%%)".format(entry.completionPercentage) else "• Completed Habits: ${entry.completedHabitsCount}/${entry.totalHabitsCount} (%.0f%%)".format(entry.completionPercentage))
        if (entry.reflectionNotes.isNotBlank()) {
            appendLine("• " + (if (language == AppLanguage.MARATHI) "मनोगत: ${entry.reflectionNotes}" else "Notes: ${entry.reflectionNotes}"))
        }
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Chintan Sarani - ${entry.date}")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, if (language == AppLanguage.MARATHI) "साधना तपशील शेअर करा" else "Share Routine Details"))
}
