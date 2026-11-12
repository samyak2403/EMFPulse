package com.samyak.emfpulse.data.model

import androidx.annotation.StringRes
import com.samyak.emfpulse.R

/**
 * Physical placement of the magnetometer sensor on the device body.
 */
enum class SensorPosition(
    @param:StringRes val labelResId: Int
) {
    LEFT(R.string.sensor_pos_left),
    TOP_LEFT(R.string.sensor_pos_top_left),
    TOP_RIGHT(R.string.sensor_pos_top_right),
    RIGHT(R.string.sensor_pos_right)
}

