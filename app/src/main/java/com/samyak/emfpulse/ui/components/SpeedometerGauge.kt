package com.samyak.emfpulse.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.anastr.speedometer.SpeedView
import com.github.anastr.speedometer.components.Section
import com.github.anastr.speedometer.components.cneter.SvCenterCircle
import com.github.anastr.speedometer.components.indicators.LineIndicator
import com.samyak.emfpulse.ui.theme.AccentRose
import com.samyak.emfpulse.ui.theme.TextPrimary
import com.samyak.emfpulse.ui.theme.TextSecondary
import kotlinx.collections.immutable.persistentListOf

/**
 * Modern SpeedView gauge using com.github.anastr:speedometer library.
 * Matches the multi-colored arc, scale ticks, and blue pointer design.
 */
@Composable
fun SpeedometerGauge(
    speed: Float,
    maxSpeed: Float,
    unit: String,
    modifier: Modifier = Modifier
) {
    val sections = persistentListOf(
        Section(0.0f, 0.20f, Color(0xFF51CF66), width = 26.dp),
        Section(0.20f, 0.45f, Color(0xFFFCC419), width = 26.dp),
        Section(0.45f, 0.75f, Color(0xFFFF922B), width = 26.dp),
        Section(0.75f, 1.0f, Color(0xFFFF6B6B), width = 26.dp)
    )

    val ticks = persistentListOf(0f, 0.25f, 0.5f, 0.75f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        SpeedView(
            speed = speed,
            minSpeed = 0f,
            maxSpeed = maxSpeed,
            startDegree = 135,
            endDegree = 405,
            unit = unit,
            unitUnderSpeed = true,
            unitSpeedSpace = 2.dp,
            speedUnitAlignment = Alignment.BottomCenter,
            speedUnitPadding = 12.dp,
            backgroundCircleColor = Color.Transparent,
            sections = sections,
            ticks = ticks,
            tickPadding = 24.dp,
            tickRotate = false,
            tickLabel = { _, tickSpeed ->
                Text(
                    text = "${tickSpeed.toInt()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            },
            marksCount = 12,
            marksColor = Color(0x66FFFFFF),
            marksWidth = 2.dp,
            marksHeight = 6.dp,
            marksPadding = 4.dp,
            marksCap = StrokeCap.Round,
            indicator = {
                LineIndicator(
                    color = Color(0xFF339AF0),
                    width = 3.dp,
                    cap = StrokeCap.Round
                )
            },
            centerContent = {
                SvCenterCircle(
                    size = 38.dp,
                    color = Color(0xFF27272C)
                )
            },
            speedText = {
                Text(
                    text = "%.1f".format(speed),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            unitText = {
                Text(
                    text = unit,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentRose
                )
            }
        )
    }
}

