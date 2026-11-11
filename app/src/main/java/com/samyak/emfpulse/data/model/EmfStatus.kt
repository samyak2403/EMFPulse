package com.samyak.emfpulse.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.samyak.emfpulse.R
import com.samyak.emfpulse.ui.theme.AlertOrange
import com.samyak.emfpulse.ui.theme.DangerRed
import com.samyak.emfpulse.ui.theme.SafeGreen
import com.samyak.emfpulse.ui.theme.WarningYellow

/**
 * EMF Radiation safety thresholds and associated display tokens.
 */
enum class EmfStatus(
    val title: String,
    @param:StringRes val titleRes: Int,
    val color: Color
) {
    SAFE(
        title = "SAFE / NORMAL BACKGROUND",
        titleRes = R.string.status_safe,
        color = SafeGreen
    ),
    MODERATE(
        title = "MODERATE MAGNETIC FIELD",
        titleRes = R.string.status_moderate,
        color = WarningYellow
    ),
    HIGH(
        title = "HIGH EMF DETECTED!",
        titleRes = R.string.status_high,
        color = AlertOrange
    ),
    DANGER(
        title = "DANGER: EXTREME RADIATION",
        titleRes = R.string.status_danger,
        color = DangerRed
    );

    companion object {
        fun fromValue(calibratedMagnitude: Float): EmfStatus = when {
            calibratedMagnitude < 60f -> SAFE
            calibratedMagnitude < 120f -> MODERATE
            calibratedMagnitude < 200f -> HIGH
            else -> DANGER
        }
    }
}
