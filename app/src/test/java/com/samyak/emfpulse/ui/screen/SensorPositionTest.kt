package com.samyak.emfpulse.ui.screen

import com.samyak.emfpulse.data.model.SensorPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SensorPositionTest {

    @Test
    fun testDefaultSensorPositionIsLeft() {
        val state = EmfUiState()
        assertEquals(SensorPosition.LEFT, state.sensorPosition)
        assertTrue(state.isSensorLocationVisible)
    }

    @Test
    fun testSensorPositionEntries() {
        assertEquals(4, SensorPosition.entries.size)
        assertTrue(SensorPosition.entries.contains(SensorPosition.LEFT))
        assertTrue(SensorPosition.entries.contains(SensorPosition.RIGHT))
        assertTrue(SensorPosition.entries.contains(SensorPosition.TOP_LEFT))
        assertTrue(SensorPosition.entries.contains(SensorPosition.TOP_RIGHT))
    }
}
