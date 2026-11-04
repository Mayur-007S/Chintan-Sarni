package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaSkyContainer
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite

import com.example.localization.AppLanguage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CounterItemCard(
    title: String,
    subtitle: String? = null,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isTimeInMinutes: Boolean = false,
    isJapaPreset: Boolean = false,
    language: AppLanguage = AppLanguage.MARATHI,
    testTag: String = ""
) {
    val isCompleted = value > 0
    val borderColor by animateColorAsState(
        targetValue = if (isCompleted) ChaitanyaBluePrimary.copy(alpha = 0.5f)
        else ChaitanyaCardBorder,
        label = "border_color"
    )

    // Component color is strictly white #FFFFFF
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorComponentWhite // #FFFFFF
        ),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        ),
                        color = ChaitanyaBluePrimary
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.5.sp,
                                lineHeight = 15.sp
                            ),
                            color = ChaitanyaTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Stepper controls (increments / decrements directly by 1)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = {
                            if (value > 0) {
                                onValueChange((value - 1).coerceAtLeast(0))
                            }
                        },
                        enabled = value > 0,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("${testTag}_decrement"),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = ColorNavigations, // #D3EFFB
                            contentColor = ChaitanyaBlueDark
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = if (isTimeInMinutes) "वेळ १ मिनिट कमी करा" else "Decrease count",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Value badge (Formatted for minutes or count)
                    val displayValue = if (isTimeInMinutes) {
                        if (value <= 0) {
                            if (language == AppLanguage.MARATHI) "० मि." else "0 min"
                        } else if (value < 60) {
                            if (language == AppLanguage.MARATHI) "$value मि." else "$value min"
                        } else {
                            val hrs = value / 60
                            val mins = value % 60
                            if (language == AppLanguage.MARATHI) {
                                if (mins == 0) "$hrs तास" else "$hrs तास $mins मि."
                            } else {
                                if (mins == 0) "$hrs hr" else "$hrs hr $mins min"
                            }
                        }
                    } else {
                        "$value"
                    }

                    Text(
                        text = displayValue,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isTimeInMinutes && value >= 60) 13.5.sp else if (isTimeInMinutes) 14.5.sp else 17.sp
                        ),
                        color = if (value > 0) ColorActiveStatus // #FD0031
                        else ChaitanyaTextSecondary,
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .testTag("${testTag}_value"),
                        maxLines = 1,
                        softWrap = false
                    )

                    // Increment button with active status color #FD0031 and text #FFFFFF (Increments by 1 directly)
                    FilledTonalIconButton(
                        onClick = { onValueChange(value + 1) },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("${testTag}_increment"),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = ColorActiveStatus, // #FD0031
                            contentColor = ColorTextWhite       // #FFFFFF
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = if (isTimeInMinutes) "वेळ १ मिनिट वाढवा" else "Increase count",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick increment chips styled with #D3EFFB
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isTimeInMinutes) {
                    if (language == AppLanguage.MARATHI) {
                        QuickChip(label = "+१ मि.") { onValueChange(value + 1) }
                        QuickChip(label = "+५ मि.") { onValueChange(value + 5) }
                        QuickChip(label = "+१० मि.") { onValueChange(value + 10) }
                        QuickChip(label = "+१५ मि.") { onValueChange(value + 15) }
                        QuickChip(label = "+३० मि.") { onValueChange(value + 30) }
                        QuickChip(label = "+६० मि. (१ तास)") { onValueChange(value + 60) }
                        if (value > 0) {
                            QuickChip(label = "स्वच्छ करा (०)", isReset = true) { onValueChange(0) }
                        }
                    } else {
                        QuickChip(label = "+1 min") { onValueChange(value + 1) }
                        QuickChip(label = "+5 min") { onValueChange(value + 5) }
                        QuickChip(label = "+10 min") { onValueChange(value + 10) }
                        QuickChip(label = "+15 min") { onValueChange(value + 15) }
                        QuickChip(label = "+30 min") { onValueChange(value + 30) }
                        QuickChip(label = "+60 min (1 hr)") { onValueChange(value + 60) }
                        if (value > 0) {
                            QuickChip(label = "Reset (0)", isReset = true) { onValueChange(0) }
                        }
                    }
                } else if (isJapaPreset) {
                    QuickChip(label = "+108") { onValueChange(value + 108) }
                    QuickChip(label = "+500") { onValueChange(value + 500) }
                    QuickChip(label = "+1000") { onValueChange(value + 1000) }
                    if (value > 0) {
                        QuickChip(
                            label = if (language == AppLanguage.MARATHI) "स्वच्छ करा (०)" else "Reset (0)",
                            isReset = true
                        ) { onValueChange(0) }
                    }
                } else {
                    QuickChip(label = "+1") { onValueChange(value + 1) }
                    QuickChip(label = "+5") { onValueChange(value + 5) }
                    QuickChip(label = "+10") { onValueChange(value + 10) }
                    if (value > 0) {
                        QuickChip(
                            label = if (language == AppLanguage.MARATHI) "स्वच्छ करा (०)" else "Reset (0)",
                            isReset = true
                        ) { onValueChange(0) }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickChip(
    label: String,
    isReset: Boolean = false,
    onClick: () -> Unit
) {
    SuggestionChip(
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (isReset) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
            else ChaitanyaSkyContainer,
            labelColor = if (isReset) MaterialTheme.colorScheme.error
            else ChaitanyaBlueDark
        ),
        border = BorderStroke(
            0.5.dp,
            if (isReset) MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
            else ChaitanyaCardBorder
        ),
        modifier = Modifier.height(30.dp)
    )
}
