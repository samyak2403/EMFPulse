package com.samyak.emfpulse.data.model

/**
 * Raw 3-axis sensor vector and calculated total magnitude in Microtesla (µT).
 */
data class EmfReading(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val magnitude: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)
