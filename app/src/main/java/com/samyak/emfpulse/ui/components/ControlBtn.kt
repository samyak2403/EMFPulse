package com.samyak.emfpulse.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.emfpulse.ui.theme.DarkBorder
import com.samyak.emfpulse.ui.theme.InactiveButtonColor
import com.samyak.emfpulse.ui.theme.RoseButtonBrush
import com.samyak.emfpulse.ui.theme.TextPrimary
import com.samyak.emfpulse.ui.theme.TextSecondary

/**
 * Control button styled with the signature coral-rose toggle design from VeilVPN.
 */
@Composable
fun ControlBtn(
    label: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    activeColor: Color? = null,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .then(
                if (isActive) {
                    if (activeColor != null) {
                        Modifier.background(activeColor)
                    } else {
                        Modifier.background(RoseButtonBrush)
                    }
                } else {
                    Modifier
                        .background(InactiveButtonColor)
                        .border(1.dp, DarkBorder, shape)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = if (isActive && activeColor == null) Color(0xFF18181B) else if (isActive) Color.Black else TextSecondary,
            letterSpacing = 0.5.sp
        )
    }
}
