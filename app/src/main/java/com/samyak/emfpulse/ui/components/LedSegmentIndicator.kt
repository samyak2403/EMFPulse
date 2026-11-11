package com.samyak.emfpulse.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.emfpulse.R
import com.samyak.emfpulse.ui.theme.AlertOrange
import com.samyak.emfpulse.ui.theme.DangerRed
import com.samyak.emfpulse.ui.theme.DarkBorder
import com.samyak.emfpulse.ui.theme.DarkSurface
import com.samyak.emfpulse.ui.theme.SafeGreen
import com.samyak.emfpulse.ui.theme.TextPrimary
import com.samyak.emfpulse.ui.theme.TextSecondary
import com.samyak.emfpulse.ui.theme.WarningYellow

/**
 * Multi-segment LED bar meter (K2 style) indicating signal intensity.
 */
@Composable
fun LedSegmentIndicator(
    emfValue: Float,
    modifier: Modifier = Modifier
) {
    val totalSegments = 15
    val activeSegments = ((emfValue / 250f) * totalSegments).toInt().coerceIn(0, totalSegments)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.led_level_indicator),
                fontSize = 11.sp,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "${(emfValue / 2.5f).toInt().coerceAtMost(100)}%",
                fontSize = 11.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 0 until totalSegments) {
                val segmentColor = when {
                    i < 5 -> SafeGreen
                    i < 9 -> WarningYellow
                    i < 12 -> AlertOrange
                    else -> DangerRed
                }
                val isLit = i < activeSegments
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isLit) segmentColor else segmentColor.copy(alpha = 0.15f))
                )
            }
        }
    }
}
