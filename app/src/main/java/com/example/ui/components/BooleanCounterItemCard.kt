package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BooleanCounterItemCard(
    title: String,
    subtitle: String? = null,
    isDone: Boolean,
    count: Int,
    onDoneChange: (Boolean) -> Unit,
    onCountChange: (Int) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val borderColor by animateColorAsState(
        targetValue = if (isDone) ColorActiveStatus.copy(alpha = 0.4f)
        else ChaitanyaCardBorder,
        label = "border_bool_color"
    )

    // Component is strictly white #FFFFFF
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

                // Yes / No pill selector with exact active status #FD0031
                YesNoSwitchPill(
                    isYes = isDone,
                    language = language,
                    onToggle = { selectedYes ->
                        onDoneChange(selectedYes)
                        if (selectedYes && count == 0) {
                            onCountChange(1)
                        } else if (!selectedYes) {
                            onCountChange(0)
                        }
                    },
                    testTag = "${testTag}_switch"
                )
            }

            // Animated counter input revealed only when "Yes" is checked
            AnimatedVisibility(
                visible = isDone,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ColorNavigations) // #D3EFFB
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.countLabel(language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ChaitanyaBlueDark
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalIconButton(
                                onClick = { if (count > 0) onCountChange(count - 1) },
                                enabled = count > 0,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("${testTag}_decrement"),
                                shape = CircleShape,
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = ColorComponentWhite,
                                    contentColor = ChaitanyaBlueDark
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease count",
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ColorActiveStatus, // #FD0031
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .testTag("${testTag}_value")
                            )

                            FilledTonalIconButton(
                                onClick = { onCountChange(count + 1) },
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
                                    contentDescription = "Increase count",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = { onCountChange(count + 1) },
                            label = { Text("+1", style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = ColorNavigations,
                                labelColor = ChaitanyaBlueDark
                            ),
                            border = BorderStroke(0.5.dp, ChaitanyaCardBorder),
                            modifier = Modifier.height(28.dp)
                        )
                        SuggestionChip(
                            onClick = { onCountChange(count + 2) },
                            label = { Text("+2", style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = ColorNavigations,
                                labelColor = ChaitanyaBlueDark
                            ),
                            border = BorderStroke(0.5.dp, ChaitanyaCardBorder),
                            modifier = Modifier.height(28.dp)
                        )
                        SuggestionChip(
                            onClick = { onCountChange(count + 5) },
                            label = { Text("+5", style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = ColorNavigations,
                                labelColor = ChaitanyaBlueDark
                            ),
                            border = BorderStroke(0.5.dp, ChaitanyaCardBorder),
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun YesNoSwitchPill(
    isYes: Boolean,
    language: AppLanguage,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .testTag(testTag),
        color = ColorNavigations, // #D3EFFB
        border = BorderStroke(1.dp, ChaitanyaCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // No button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (!isYes) Color(0xFFE2E8F0)
                        else Color.Transparent
                    )
                    .clickable { onToggle(false) }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                    .testTag("${testTag}_no"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (!isYes) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFF475569)
                        )
                    }
                    Text(
                        text = AppStrings.noLabel(language),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp),
                        color = if (!isYes) Color(0xFF1E293B)
                        else ChaitanyaTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            // Yes button (Active Status: #FD0031 with text #FFFFFF)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isYes) ColorActiveStatus // #FD0031
                        else Color.Transparent
                    )
                    .clickable { onToggle(true) }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                    .testTag("${testTag}_yes"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (isYes) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = ColorTextWhite // #FFFFFF
                        )
                    }
                    Text(
                        text = AppStrings.yesLabel(language),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 11.5.sp),
                        color = if (isYes) ColorTextWhite // #FFFFFF
                        else ChaitanyaTextSecondary
                    )
                }
            }
        }
    }
}
