package com.samyak.emfpulse.data.sensor

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SimulatedEmfSensorManagerTest {

    @Test
    fun testSimulatedSensorEmitsPositiveReadings() = runBlocking {
        val manager = SimulatedEmfSensorManager()
        assertTrue("Sensor must be available in simulation mode", manager.isSensorAvailable)

        val reading = manager.startListening().first()
        assertTrue("Magnitude should be greater than 0", reading.magnitude > 0f)
        assertTrue("Timestamp should be populated", reading.timestamp > 0L)
    }
}
