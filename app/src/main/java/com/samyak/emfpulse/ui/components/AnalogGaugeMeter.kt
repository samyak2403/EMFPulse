package com.samyak.emfpulse.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.samyak.emfpulse.ui.theme.AccentRose
import com.samyak.emfpulse.ui.theme.DarkBorder
import com.samyak.emfpulse.ui.theme.SafeGreen
import com.samyak.emfpulse.ui.theme.WarningYellow
import kotlin.math.cos
import kotlin.math.sin

/**
 * Semi-circular analog gauge with color zones, tick marks, and animated needle.
 */
@Composable
fun AnalogGaugeMeter(
    value: Float,
    maxValue: Float = 300f,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(0f, maxValue),
        animationSpec = tween(durationMillis = 150),
        label = "NeedleAnimation"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        val strokeWidth = 22.dp.toPx()
        val arcWidth = size.width
        val arcHeight = size.height * 1.6f
        val topLeftOffset = Offset(0f, 10.dp.toPx())
        val center = Offset(size.width / 2f, topLeftOffset.y + arcHeight / 2f)

        // Background track arc
        drawArc(
            color = DarkBorder,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = topLeftOffset,
            size = Size(arcWidth, arcHeight),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Color Zones Arc (Mint -> Amber -> Coral/Rose)
        val sweepGradient = Brush.sweepGradient(
            0.5f to SafeGreen,
            0.75f to WarningYellow,
            1.0f to AccentRose
        )
        drawArc(
            brush = sweepGradient,
            startAngle = 180f,
            sweepAngle = (animatedValue / maxValue) * 180f,
            useCenter = false,
            topLeft = topLeftOffset,
            size = Size(arcWidth, arcHeight),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Draw Radial Scale Ticks
        val tickCount = 9
        val outerRadius = (arcWidth / 2f) - strokeWidth
        val innerRadius = outerRadius - 10.dp.toPx()
        for (i in 0 until tickCount) {
            val fraction = i / (tickCount - 1).toFloat()
            val angleDeg = 180f + fraction * 180f
            val rad = Math.toRadians(angleDeg.toDouble())

            val startTick = Offset(
                x = center.x + (outerRadius * cos(rad)).toFloat(),
                y = center.y + (outerRadius * sin(rad)).toFloat()
            )
            val endTick = Offset(
                x = center.x + (innerRadius * cos(rad)).toFloat(),
                y = center.y + (innerRadius * sin(rad)).toFloat()
            )

            drawLine(
                color = Color.White.copy(alpha = 0.35f),
                start = startTick,
                end = endTick,
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Draw Needle
        val needleAngleDegrees = 180f + (animatedValue / maxValue) * 180f
        val needleAngleRad = Math.toRadians(needleAngleDegrees.toDouble())
        val needleLength = outerRadius - 6.dp.toPx()

        val needleTip = Offset(
            x = center.x + (needleLength * cos(needleAngleRad)).toFloat(),
            y = center.y + (needleLength * sin(needleAngleRad)).toFloat()
        )

        drawLine(
            color = Color.White,
            start = center,
            end = needleTip,
            strokeWidth = 3.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Central Needle Pivot
        drawCircle(
            color = Color(0xFF27272C),
            radius = 18.dp.toPx(),
            center = center
        )
        drawCircle(
            color = AccentRose,
            radius = 6.dp.toPx(),
            center = center
        )
    }
}
