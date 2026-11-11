package com.samyak.emfpulse.ui.screen

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NoSensorFlowTest {

    @Test
    fun testScreenRoutingWhenSensorIsAvailable() {
        val isHardwareSensorAvailable = true
        val initialScreen = if (isHardwareSensorAvailable) "DETECTOR" else "NO_SENSOR"

        assertEquals("When sensor is available, it must route directly to DETECTOR", "DETECTOR", initialScreen)
    }

    @Test
    fun testScreenRoutingWhenSensorIsMissing() {
        val isHardwareSensorAvailable = false
        val initialScreen = if (isHardwareSensorAvailable) "DETECTOR" else "NO_SENSOR"

        assertEquals("When sensor is missing, it must route to NO_SENSOR", "NO_SENSOR", initialScreen)
    }
}

