package com.samyak.emfpulse.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorAdditionalInfo
import android.hardware.SensorEvent
import android.hardware.SensorEventCallback
import android.hardware.SensorManager
import android.os.Build
import com.samyak.emfpulse.data.model.EmfReading
import com.samyak.emfpulse.data.model.SensorPosition
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.sqrt

/**
 * Android hardware implementation of [EmfSensorManager] using [Sensor.TYPE_MAGNETIC_FIELD].
 * Employs a low-pass filter to dampen ambient electrical jitter and supports
 * automatic hardware sensor location detection via HAL placement metadata and device heuristics.
 */
class AndroidEmfSensorManager(
    context: Context,
    private val filterAlpha: Float = 0.15f
) : EmfSensorManager {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var hardwareDetectedPosition: SensorPosition? = null

    override val isSensorAvailable: Boolean
        get() = magnetometer != null

    override fun detectHardwareSensorPosition(): SensorPosition? {
        // 1. If HAL returned TYPE_SENSOR_PLACEMENT, use it directly
        hardwareDetectedPosition?.let { return it }

        if (sensorManager == null || magnetometer == null) return null

        // 2. Fallback to OEM / hardware platform heuristics
        val mfr = Build.MANUFACTURER.lowercase()
        val model = Build.MODEL.lowercase()

        return when {
            mfr.contains("google") || model.contains("pixel") -> SensorPosition.TOP_LEFT
            mfr.contains("samsung") -> SensorPosition.RIGHT
            mfr.contains("xiaomi") || mfr.contains("redmi") || mfr.contains("poco") -> SensorPosition.TOP_LEFT
            mfr.contains("oneplus") || mfr.contains("oppo") || mfr.contains("realme") -> SensorPosition.LEFT
            mfr.contains("motorola") || mfr.contains("moto") -> SensorPosition.TOP_RIGHT
            else -> SensorPosition.LEFT
        }
    }

    override fun startListening(): Flow<EmfReading> = callbackFlow {
        if (sensorManager == null || magnetometer == null) {
            close()
            return@callbackFlow
        }

        val listener = object : SensorEventCallback() {
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

            override fun onSensorAdditionalInfo(info: SensorAdditionalInfo?) {
                if (info?.type == SensorAdditionalInfo.TYPE_SENSOR_PLACEMENT) {
                    val f = info.floatValues
                    if (f != null && f.size >= 12) {
                        // Indices 9..11 are tx, ty, tz in mm relative to device geometric origin
                        val tx = f[9]
                        val ty = f[10]
                        hardwareDetectedPosition = when {
                            ty > 25f && tx < -10f -> SensorPosition.TOP_LEFT
                            ty > 25f && tx > 10f -> SensorPosition.TOP_RIGHT
                            tx < 0f -> SensorPosition.LEFT
                            else -> SensorPosition.RIGHT
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
