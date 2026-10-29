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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CounterItemCard(
    title: String,
    subtitle: String? = null,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isJapaPreset: Boolean = false,
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
                            lineHeight = 22.sp
                        ),
                        color = ChaitanyaBluePrimary
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                            color = ChaitanyaTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Stepper controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = { if (value > 0) onValueChange(value - 1) },
                        enabled = value > 0,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("${testTag}_decrement"),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = ColorNavigations, // #D3EFFB
                            contentColor = ChaitanyaBlueDark
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease count",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Value badge
                    Text(
                        text = "$value",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = if (value > 0) ColorActiveStatus // #FD0031
                        else ChaitanyaTextSecondary,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .testTag("${testTag}_value")
                    )

                    // Increment button with active status color #FD0031 and text #FFFFFF
                    FilledTonalIconButton(
                        onClick = { onValueChange(value + 1) },
                        modifier = Modifier
                            .size(40.dp)
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
                            modifier = Modifier.size(18.dp)
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
                if (isJapaPreset) {
                    QuickChip(label = "+108") { onValueChange(value + 108) }
                    QuickChip(label = "+500") { onValueChange(value + 500) }
                    QuickChip(label = "+1000") { onValueChange(value + 1000) }
                    if (value > 0) {
                        QuickChip(label = "Reset (0)", isReset = true) { onValueChange(0) }
                    }
                } else {
                    QuickChip(label = "+1") { onValueChange(value + 1) }
                    QuickChip(label = "+5") { onValueChange(value + 5) }
                    QuickChip(label = "+10") { onValueChange(value + 10) }
                    if (value > 0) {
                        QuickChip(label = "Reset (0)", isReset = true) { onValueChange(0) }
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
