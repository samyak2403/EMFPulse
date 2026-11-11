package com.samyak.emfpulse.data.model

/**
 * Units of measurement for Electromagnetic Fields (EMF).
 * Standard conversion: 1 µT (Microtesla) = 10 mG (Milligauss).
 */
enum class EmfUnit(val symbol: String, val multiplier: Float) {
    MICROTESLA(symbol = "µT", multiplier = 1.0f),
    MILLIGAUSS(symbol = "mG", multiplier = 10.0f);

    fun convert(valueInMicrotesla: Float): Float = valueInMicrotesla * multiplier
}
