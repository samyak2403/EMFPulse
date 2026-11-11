package com.samyak.emfpulse.ui.screen

import com.samyak.emfpulse.data.model.EmfReading
import com.samyak.emfpulse.data.model.EmfStatus
import com.samyak.emfpulse.data.model.EmfUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class EmfUiStateTest {

    @Test
    fun testDefaultDisplayState() {
        val state = EmfUiState(
            currentReading = EmfReading(x = 10f, y = 20f, z = 30f, magnitude = 37.4f),
            calibratedMagnitude = 37.4f,
            minReading = 12.0f,
            peakReading = 85.0f,
            unit = EmfUnit.MICROTESLA
        )

        assertEquals(37.4f, state.displayMagnitude, 0.001f)
        assertEquals(10f, state.displayX, 0.001f)
        assertEquals(20f, state.displayY, 0.001f)
        assertEquals(30f, state.displayZ, 0.001f)
        assertEquals(12.0f, state.displayMin, 0.001f)
        assertEquals(85.0f, state.displayPeak, 0.001f)
        assertEquals(EmfStatus.SAFE, state.status)
    }

    @Test
    fun testMilligaussConversionState() {
        val state = EmfUiState(
            currentReading = EmfReading(x = 5f, y = 10f, z = 15f, magnitude = 18.7f),
            calibratedMagnitude = 18.7f,
            minReading = 5.0f,
            peakReading = 25.0f,
            unit = EmfUnit.MILLIGAUSS
        )

        assertEquals(187.0f, state.displayMagnitude, 0.001f)
        assertEquals(50f, state.displayX, 0.001f)
        assertEquals(100f, state.displayY, 0.001f)
        assertEquals(150f, state.displayZ, 0.001f)
        assertEquals(50.0f, state.displayMin, 0.001f)
        assertEquals(250.0f, state.displayPeak, 0.001f)
    }

    @Test
    fun testMinReadingFallbackWhenUnset() {
        val state = EmfUiState(
            minReading = Float.MAX_VALUE
        )
        assertEquals(0f, state.displayMin, 0.001f)
    }
}
