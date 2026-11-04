package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorComponentWhite

@Composable
fun BooleanOnlyItemCard(
    title: String,
    subtitle: String? = null,
    isDone: Boolean,
    onDoneChange: (Boolean) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val borderColor by animateColorAsState(
        targetValue = if (isDone) ColorActiveStatus.copy(alpha = 0.4f)
        else ChaitanyaCardBorder,
        label = "border_bool_only_color"
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

            YesNoSwitchPill(
                isYes = isDone,
                language = language,
                onToggle = onDoneChange,
                testTag = "${testTag}_switch"
            )
        }
    }
}
