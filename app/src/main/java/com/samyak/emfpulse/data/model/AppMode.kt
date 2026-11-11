package com.samyak.emfpulse.data.model

/**
 * Operational mode of the EMF detector.
 */
enum class AppMode(
    val title: String,
    val description: String
) {
    PRODUCTION(
        title = "Production Mode",
        description = "Uses real built-in hardware magnetometer sensor"
    ),
    TEST(
        title = "Test / Demo Mode",
        description = "Simulates fluctuating EMF electromagnetic signals for testing and demonstration"
    )
}
