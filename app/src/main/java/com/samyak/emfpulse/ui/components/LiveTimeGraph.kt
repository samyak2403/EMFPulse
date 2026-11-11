package com.samyak.emfpulse.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.emfpulse.R
import com.samyak.emfpulse.ui.theme.AccentRose
import com.samyak.emfpulse.ui.theme.DarkBorder
import com.samyak.emfpulse.ui.theme.DarkSurface
import com.samyak.emfpulse.ui.theme.TextSecondary

/**
 * Real-time scrolling EMF waveform monitor canvas.
 */
@Composable
fun LiveTimeGraph(
    history: List<Float>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.real_time_waveform),
            fontSize = 11.sp,
            color = TextSecondary,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (history.size < 2) return@Canvas
            val maxGraphVal = 250f
            val path = Path()
            val stepX = size.width / 60f

            history.forEachIndexed { index, value ->
                val x = index * stepX
                val normalizedY = (value.coerceIn(0f, maxGraphVal) / maxGraphVal)
                val y = size.height - (normalizedY * size.height)
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
                path = path,
                color = AccentRose,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}
