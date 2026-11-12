package com.samyak.emfpulse.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.samyak.emfpulse.data.model.SensorPosition
import com.samyak.emfpulse.ui.theme.AlertOrange
import com.samyak.emfpulse.ui.theme.DangerRed
import com.samyak.emfpulse.ui.theme.SafeGreen
import com.samyak.emfpulse.ui.theme.WarningYellow
import kotlin.math.PI
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Animated white cable overlay connecting [ic_magnetic_sensor] to the LED Level Indicator card.
 * Dynamically flexes as the screen is scrolled and renders live electrical signal wave pulses.
 */
@Composable
fun SensorCableWave(
    position: SensorPosition,
    cardBounds: Rect?,
    sensorCenter: Offset?,
    emfValue: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CableWaveAnimation")

    // Continuous wave oscillation along the cable
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WavePhase"
    )

    // Signal energy pulse traveling from sensor probe into LED card
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseProgress"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // 1. Determine Start Point (Sensor Probe)
        val p0: Offset
        val p3: Offset
        val c1: Offset
        val c2: Offset

        when (position) {
            SensorPosition.LEFT -> {
                val sensorY = sensorCenter?.y ?: 224.dp.toPx()
                p0 = Offset(x = 28.dp.toPx(), y = sensorY)

                val targetX = cardBounds?.left ?: 16.dp.toPx()
                val targetY = cardBounds?.let { (it.bottom - 12.dp.toPx()).coerceIn(40.dp.toPx(), height) }
                    ?: 168.dp.toPx()
                p3 = Offset(x = targetX + 4.dp.toPx(), y = targetY)

                // Cubic control points curling upward to the left side of the card
                c1 = Offset(x = p0.x + 38.dp.toPx(), y = p0.y - 12.dp.toPx())
                c2 = Offset(x = p3.x - 22.dp.toPx(), y = p3.y + 26.dp.toPx())
            }
            SensorPosition.RIGHT -> {
                val sensorY = sensorCenter?.y ?: 224.dp.toPx()
                p0 = Offset(x = width - 28.dp.toPx(), y = sensorY)

                val targetX = cardBounds?.right ?: (width - 16.dp.toPx())
                val targetY = cardBounds?.let { (it.bottom - 12.dp.toPx()).coerceIn(40.dp.toPx(), height) }
                    ?: 168.dp.toPx()
                p3 = Offset(x = targetX - 4.dp.toPx(), y = targetY)

                // Cubic control points curling upward to the right side of the card
                c1 = Offset(x = p0.x - 38.dp.toPx(), y = p0.y - 12.dp.toPx())
                c2 = Offset(x = p3.x + 22.dp.toPx(), y = p3.y + 26.dp.toPx())
            }
            SensorPosition.TOP_LEFT -> {
                p0 = Offset(x = 28.dp.toPx(), y = 88.dp.toPx())
                val targetX = cardBounds?.left ?: 16.dp.toPx()
                val targetY = cardBounds?.top ?: 110.dp.toPx()
                p3 = Offset(x = targetX + 16.dp.toPx(), y = targetY)

                c1 = Offset(x = p0.x + 12.dp.toPx(), y = p0.y + 16.dp.toPx())
                c2 = Offset(x = p3.x - 12.dp.toPx(), y = p3.y - 12.dp.toPx())
            }
            SensorPosition.TOP_RIGHT -> {
                p0 = Offset(x = width - 28.dp.toPx(), y = 88.dp.toPx())
                val targetX = cardBounds?.right ?: (width - 16.dp.toPx())
                val targetY = cardBounds?.top ?: 110.dp.toPx()
                p3 = Offset(x = targetX - 16.dp.toPx(), y = targetY)

                c1 = Offset(x = p0.x - 12.dp.toPx(), y = p0.y + 16.dp.toPx())
                c2 = Offset(x = p3.x + 12.dp.toPx(), y = p3.y - 12.dp.toPx())
            }
        }

        // 2. Sample Cubic Bezier Curve with Animated Normal Displacement
        val steps = 36
        val wavyPoints = ArrayList<Offset>(steps + 1)
        val waveAmplitude = 4.5.dp.toPx()

        for (i in 0..steps) {
            val t = i.toFloat() / steps
            val inv = 1f - t

            // Base Bezier Point
            val bx = inv * inv * inv * p0.x +
                    3f * inv * inv * t * c1.x +
                    3f * inv * t * t * c2.x +
                    t * t * t * p3.x
            val by = inv * inv * inv * p0.y +
                    3f * inv * inv * t * c1.y +
                    3f * inv * t * t * c2.y +
                    t * t * t * p3.y

            // Bezier Tangent Vector
            val dx = 3f * inv * inv * (c1.x - p0.x) +
                    6f * inv * t * (c2.x - c1.x) +
                    3f * t * t * (p3.x - c2.x)
            val dy = 3f * inv * inv * (c1.y - p0.y) +
                    6f * inv * t * (c2.y - c1.y) +
                    3f * t * t * (p3.y - c2.y)

            val len = hypot(dx, dy).coerceAtLeast(0.001f)
            val nx = -dy / len
            val ny = dx / len

            // Sine wave envelope: zero at endpoints (t=0 and t=1), peaks in center
            val envelope = sin(PI.toFloat() * t)
            val displacement = waveAmplitude * sin(2.5f * (2f * PI.toFloat()) * t - wavePhase) * envelope

            wavyPoints.add(Offset(bx + nx * displacement, by + ny * displacement))
        }

        // 3. Construct Path & Render White Cable
        val cablePath = Path().apply {
            moveTo(wavyPoints[0].x, wavyPoints[0].y)
            for (i in 1 until wavyPoints.size) {
                lineTo(wavyPoints[i].x, wavyPoints[i].y)
            }
        }

        // Subtle ambient glow behind cable
        drawPath(
            path = cablePath,
            color = Color.White.copy(alpha = 0.22f),
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
        )

        // Core White Cable
        drawPath(
            path = cablePath,
            color = Color.White.copy(alpha = 0.95f),
            style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
        )

        // Terminal Connectors at endpoints
        drawCircle(
            color = Color.White,
            radius = 3.5.dp.toPx(),
            center = p0
        )
        drawCircle(
            color = Color.White,
            radius = 3.5.dp.toPx(),
            center = p3
        )

        // 4. Moving Energy Signal Pulses along the Cable
        val pulseColor = when {
            emfValue < 30f -> SafeGreen
            emfValue < 80f -> WarningYellow
            emfValue < 150f -> AlertOrange
            else -> DangerRed
        }

        for (pulseOffset in listOf(0f, 0.5f)) {
            val activeT = (pulseProgress + pulseOffset) % 1f
            val idx = (activeT * steps).toInt().coerceIn(0, steps - 1)
            val frac = (activeT * steps) - idx
            val ptA = wavyPoints[idx]
            val ptB = wavyPoints[idx + 1]
            val pulsePos = Offset(
                x = ptA.x + (ptB.x - ptA.x) * frac,
                y = ptA.y + (ptB.y - ptA.y) * frac
            )

            // Outer pulse glow
            drawCircle(
                color = pulseColor.copy(alpha = 0.45f),
                radius = 5.5.dp.toPx(),
                center = pulsePos
            )
            // Core energetic packet
            drawCircle(
                color = Color.White,
                radius = 2.5.dp.toPx(),
                center = pulsePos
            )
        }
    }
}
