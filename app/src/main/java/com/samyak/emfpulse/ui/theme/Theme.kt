package com.samyak.emfpulse.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Matching VeilVPN Dark Palette
private val ColorScheme = darkColorScheme(
    primary = AccentRose,
    secondary = AccentCoral,
    tertiary = SafeGreen,
    background = AppBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

/**
 * Signature atmospheric gradient matching the bottom rose/coral ambient glow.
 */
val AppBackgroundGradient = Brush.verticalGradient(
    0.0f to Color(0xFF18181B),
    0.60f to Color(0xFF18181B),
    0.74f to Color(0xFF231E21),
    0.86f to Color(0xFF6B393D),
    0.95f to Color(0xFF483235),
    1.0f to Color(0xFF362326)
)

@Composable
fun EMFPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insets = WindowCompat.getInsetsController(window, view)
                insets.isAppearanceLightStatusBars = false
                insets.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}
