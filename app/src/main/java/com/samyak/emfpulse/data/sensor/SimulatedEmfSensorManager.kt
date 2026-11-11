package com.samyak.emfpulse.data.sensor

import com.samyak.emfpulse.data.model.EmfReading
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Test mode sensor simulator providing dynamic, realistic EMF radiation patterns.
 * Enables verification of gauges, waveforms, audio beeps, and alerts without requiring
 * real electromagnetic sources or physical magnetometers.
 */
class SimulatedEmfSensorManager : EmfSensorManager {

    override val isSensorAvailable: Boolean
        get() = true

    override fun startListening(): Flow<EmfReading> = flow {
        var tick = 0.0
        while (currentCoroutineContext().isActive) {
            tick += 0.12

            // Base earth magnetic field + periodic oscillation
            val base = 45.0 + 20.0 * sin(tick * 0.6) + 12.0 * cos(tick * 1.3)
            // Dynamic electrical spike every few seconds
            val isSpiking = (sin(tick * 0.2) > 0.75)
            val spikeBonus = if (isSpiking) Random.nextDouble(50.0, 160.0) else 0.0
            val jitter = (Random.nextDouble() - 0.5) * 6.0

            val magnitude = (base + spikeBonus + jitter).coerceAtLeast(8.0).toFloat()

            // Realistic 3-axis decomposition
            val x = (magnitude * 0.5f * sin(tick).toFloat())
            val y = (magnitude * 0.6f * cos(tick * 0.75).toFloat())
            val z = sqrt((magnitude * magnitude - x * x - y * y).coerceAtLeast(1f))

            emit(
                EmfReading(
                    x = x,
                    y = y,
                    z = z,
                    magnitude = magnitude,
                    timestamp = System.currentTimeMillis()
                )
            )

            delay(100L) // 10 updates per second
        }
    }
}
