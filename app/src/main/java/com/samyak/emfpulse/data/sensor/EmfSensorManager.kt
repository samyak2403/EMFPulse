package com.samyak.emfpulse.data.sensor

import com.samyak.emfpulse.data.model.EmfReading
import com.samyak.emfpulse.data.model.SensorPosition
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction layer for device magnetometer / EMF sensor data.
 */
interface EmfSensorManager {
    val isSensorAvailable: Boolean
    fun startListening(): Flow<EmfReading>
    fun detectHardwareSensorPosition(): SensorPosition?
}
