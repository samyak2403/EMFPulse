package com.samyak.emfpulse.ui.screen

import com.samyak.emfpulse.data.model.AppMode
import com.samyak.emfpulse.data.model.EmfReading
import com.samyak.emfpulse.data.model.EmfStatus
import com.samyak.emfpulse.data.model.EmfUnit
import com.samyak.emfpulse.data.model.SensorPosition

/**
 * Immutable UI state model for the EMF Detector screen and settings.
 */
data class EmfUiState(
    val currentReading: EmfReading = EmfReading(),
    val calibratedMagnitude: Float = 0f,
    val baselineOffset: Float = 0f,
    val minReading: Float = Float.MAX_VALUE,
    val peakReading: Float = 0f,
    val isHolding: Boolean = false,
    val isBeepEnabled: Boolean = false,
    val unit: EmfUnit = EmfUnit.MICROTESLA,
    val history: List<Float> = emptyList(),
    val isSensorAvailable: Boolean = true,
    val appMode: AppMode = AppMode.PRODUCTION,
    val sensorPosition: SensorPosition = SensorPosition.LEFT,
    val isSensorLocationVisible: Boolean = true
) {
    val status: EmfStatus
        get() = EmfStatus.fromValue(calibratedMagnitude)

    val displayMagnitude: Float
        get() = unit.convert(calibratedMagnitude)

    val displayX: Float
        get() = unit.convert(currentReading.x)

    val displayY: Float
        get() = unit.convert(currentReading.y)

    val displayZ: Float
        get() = unit.convert(currentReading.z)

    val displayPeak: Float
        get() = unit.convert(peakReading)

    val displayMin: Float
        get() = if (minReading == Float.MAX_VALUE) 0f else unit.convert(minReading)
}
