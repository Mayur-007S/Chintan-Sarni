package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite

@Composable
fun WizardProgressHeader(
    currentStep: Int, // 1, 2, or 3
    completedSteps: Int,
    language: AppLanguage,
    onStepClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        Pair(1, if (language == AppLanguage.MARATHI) "१. पायाभूत" else "1. Foundation"),
        Pair(2, if (language == AppLanguage.MARATHI) "२. भाव-चिंतन" else "2. Devotion"),
        Pair(3, if (language == AppLanguage.MARATHI) "३. दोष-निर्मूलन" else "3. Rectification")
    )

    // Navigation background #D3EFFB
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ColorNavigations // #D3EFFB
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, (stepNum, label) ->
                val isActive = currentStep == stepNum
                val isDone = completedSteps >= stepNum

                val circleBg by animateColorAsState(
                    targetValue = when {
                        isActive -> ColorActiveStatus // #FD0031
                        isDone -> ChaitanyaBluePrimary
                        else -> ColorComponentWhite.copy(alpha = 0.9f)
                    },
                    label = "step_circle_bg"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onStepClick(stepNum) }
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .testTag("wizard_step_$stepNum")
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(circleBg),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone && !isActive) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Step $stepNum Completed",
                                tint = ColorTextWhite, // #FFFFFF
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "$stepNum",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isActive || isDone) ColorTextWhite // #FFFFFF
                                else ChaitanyaBlueDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        color = if (isActive) ColorActiveStatus // #FD0031
                        else if (isDone) ChaitanyaBlueDark
                        else ChaitanyaBlueDark.copy(alpha = 0.7f)
                    )
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.5.dp)
                            .padding(horizontal = 4.dp)
                            .background(
                                if (completedSteps > index) ColorActiveStatus // #FD0031
                                else ChaitanyaCardBorder
                            )
                    )
                }
            }
        }
    }
}
