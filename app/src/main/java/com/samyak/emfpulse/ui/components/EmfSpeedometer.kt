package com.samyak.emfpulse.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.anastr.speedometer.SpeedView
import com.github.anastr.speedometer.components.Section
import com.github.anastr.speedometer.components.cneter.SvCenterCircle
import com.github.anastr.speedometer.components.indicators.NeedleIndicator
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

/**
 * Modern analog speedometer gauge powered by com.github.anastr:speedometer SpeedView.
 * Displays EMF radiation levels with color-coded safety zones, needle indicator,
 * dynamic ticks, and central digital readout.
 */
@Composable
fun EmfSpeedometer(
    value: Float,
    maxValue: Float = 300f,
    unit: String = "µT",
    modifier: Modifier = Modifier,
    size: Dp = 260.dp
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = value.coerceIn(0f, maxValue),
        animationSpec = tween(durationMillis = 150),
        label = "EmfSpeedometerAnimation"
    )

    // EMF Danger Level color sections:
    // Safe (0% - 20%), Moderate (20% - 40%), High (40% - 67%), Extreme (67% - 100%)
    val sections = remember {
        persistentListOf(
            Section(0f, 0.20f, color = Color(0xFF51CF66), width = 18.dp, style = StrokeCap.Butt),
            Section(0.20f, 0.40f, color = Color(0xFFFCC419), width = 18.dp, style = StrokeCap.Butt),
            Section(0.40f, 0.67f, color = Color(0xFFFF922B), width = 18.dp, style = StrokeCap.Butt),
            Section(0.67f, 1.00f, color = Color(0xFFFF6B6B), width = 18.dp, style = StrokeCap.Butt),
        )
    }

    val ticks = remember {
        persistentListOf(0f, 0.25f, 0.5f, 0.75f, 1f)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        SpeedView(
            modifier = Modifier.size(size),
            speed = animatedSpeed,
            minSpeed = 0f,
            maxSpeed = maxValue,
            unit = unit,
            unitUnderSpeed = true,
            unitSpeedSpace = 2.dp,
            speedUnitAlignment = Alignment.BottomCenter,
            speedUnitPadding = 24.dp,
            sections = sections,
            marksCount = 8,
            marksColor = Color(0xFF484F58),
            marksPadding = 20.dp,
            marksWidth = 2.dp,
            marksHeight = 6.dp,
            ticks = ticks,
            tickPadding = 28.dp,
            tickRotate = false,
            tickLabel = { _, tickSpeed ->
                Text(
                    text = "${tickSpeed.roundToInt()}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B949E)
                )
            },
            indicator = {
                NeedleIndicator(
                    color = Color(0xFF58A6FF),
                    width = 10.dp
                )
            },
            centerContent = {
                SvCenterCircle(
                    size = 32.dp,
                    color = Color(0xFF21262D)
                )
            },
            speedText = {
                Text(
                    text = "%.1f".format(animatedSpeed),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            },
            unitText = {
                Text(
                    text = unit,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B949E)
                )
            }
        )
    }
}
