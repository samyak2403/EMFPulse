package com.samyak.emfpulse.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom toggle switch matching the emerald-green I/O pill switch design.
 * Features an animated sliding white thumb with '|' (ON) and 'O' (OFF) indicators.
 */
@Composable
fun CustomToggleSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    width: Dp = 56.dp,
    height: Dp = 32.dp,
    thumbSize: Dp = 26.dp,
    activeTrackColor: Color = Color(0xFF00CE9C),
    inactiveTrackColor: Color = Color(0xFF33333A),
    thumbColor: Color = Color.White
) {
    val trackPadding = 3.dp
    val thumbMaxOffset = width - thumbSize - (trackPadding * 2)

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) thumbMaxOffset else 0.dp,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "ThumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) activeTrackColor else inactiveTrackColor,
        animationSpec = tween(durationMillis = 220),
        label = "TrackColor"
    )

    val shape = RoundedCornerShape(height / 2)

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(shape)
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                role = Role.Switch
            ) {
                onCheckedChange?.invoke(!checked)
            }
            .padding(trackPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        // Internal I/O Indicator Canvas (White '|' on left when ON, 'O' on right when OFF)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val markAlpha = if (checked) 0.95f else 0.45f

            if (checked) {
                // Draw vertical line '|' on the left side
                val lineX = size.height * 0.45f
                val topY = size.height * 0.28f
                val bottomY = size.height * 0.72f

                drawLine(
                    color = Color.White.copy(alpha = markAlpha),
                    start = Offset(lineX, topY),
                    end = Offset(lineX, bottomY),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            } else {
                // Draw circle 'O' on the right side
                val circleRadius = size.height * 0.22f
                val circleCenter = Offset(size.width - (size.height * 0.45f), size.height / 2f)

                drawCircle(
                    color = Color.White.copy(alpha = markAlpha),
                    radius = circleRadius,
                    center = circleCenter,
                    style = Stroke(width = 2.2.dp.toPx())
                )
            }
        }

        // Sliding Circular Thumb with soft elevation shadow
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .shadow(elevation = 3.dp, shape = CircleShape)
                .background(thumbColor, CircleShape)
        )
    }
}

