package com.samyak.emfpulse.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// VeilVPN Theme Colors (Sampled from design)
val AppBackground = Color(0xFF18181B)
val AppBackgroundGlow = Color(0xFF7F4144)
val AppBackgroundBottom = Color(0xFF483235)

// Surfaces & Cards
val DarkSurface = Color(0xFF222226)
val DarkSurfaceVariant = Color(0xFF27272C)
val DarkBorder = Color(0xFF33333A)
val DarkBorderSubtle = Color(0x2EFFFFFF)

// Signature Coral / Rose Accents (VeilVPN "Pro" & "Connected" style)
val AccentRose = Color(0xFFF99797)
val AccentCoral = Color(0xFFFBA098)
val AccentRoseDeep = Color(0xFFE1868A)
val AccentHalo = Color(0x33F99797)

// Text Hierarchy
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA1A1AA)
val TextMuted = Color(0xFF71717A)

// Status Indicators
val SafeGreen = Color(0xFF4ADE80)
val WarningYellow = Color(0xFFFBBF24)
val AlertOrange = Color(0xFFFB923C)
val DangerRed = Color(0xFFF99797)

// Gradient Brushes
val RoseButtonBrush = Brush.horizontalGradient(
    listOf(AccentCoral, AccentRoseDeep)
)

val InactiveButtonColor = Color(0xFF27272A)
