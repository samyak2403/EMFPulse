package com.samyak.emfpulse.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.samyak.emfpulse.data.model.EmfReading
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.sqrt

/**
 * Android hardware implementation of [EmfSensorManager] using [Sensor.TYPE_MAGNETIC_FIELD].
 * Employs a low-pass filter to dampen ambient electrical jitter.
 */
class AndroidEmfSensorManager(
    context: Context,
    private val filterAlpha: Float = 0.15f
) : EmfSensorManager {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    override val isSensorAvailable: Boolean
        get() = magnetometer != null

    override fun startListening(): Flow<EmfReading> = callbackFlow {
        if (sensorManager == null || magnetometer == null) {
            close()
            return@callbackFlow
        }

        val listener = object : SensorEventListener {
            private var filteredX = 0f
            private var filteredY = 0f
            private var filteredZ = 0f

            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    val rawX = event.values[0]
                    val rawY = event.values[1]
                    val rawZ = event.values[2]

                    filteredX = filterAlpha * rawX + (1f - filterAlpha) * filteredX
                    filteredY = filterAlpha * rawY + (1f - filterAlpha) * filteredY
                    filteredZ = filterAlpha * rawZ + (1f - filterAlpha) * filteredZ

                    val total = sqrt(
                        filteredX * filteredX +
                        filteredY * filteredY +
                        filteredZ * filteredZ
                    )

                    trySend(
                        EmfReading(
                            x = filteredX,
                            y = filteredY,
                            z = filteredZ,
                            magnitude = total,
                            timestamp = event.timestamp
                        )
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Accuracy updates can be monitored if required
            }
        }

        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
