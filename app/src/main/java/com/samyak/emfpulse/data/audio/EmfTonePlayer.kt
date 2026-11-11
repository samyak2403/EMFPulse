package com.samyak.emfpulse.data.audio

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Handles audio beep pulses (Geiger counter style) proportional to EMF intensity.
 */
class EmfTonePlayer {

    private var toneGenerator: ToneGenerator? = null
    private var alertJob: Job? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    /**
     * Starts continuous audio feedback loop based on real-time EMF magnitude.
     */
    fun startAlertLoop(
        scope: CoroutineScope,
        shouldBeep: () -> Boolean,
        getEmfValue: () -> Float
    ) {
        stopAlertLoop()
        alertJob = scope.launch {
            while (isActive) {
                val enabled = shouldBeep()
                val magnitude = getEmfValue()

                if (enabled && magnitude > 60f) {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
                    // Dynamic delay: higher EMF -> faster beeps
                    val delayTime = (700L - (magnitude * 2.5f).toLong()).coerceIn(80L, 700L)
                    delay(delayTime)
                } else {
                    delay(250L)
                }
            }
        }
    }

    fun stopAlertLoop() {
        alertJob?.cancel()
        alertJob = null
    }

    fun release() {
        stopAlertLoop()
        try {
            toneGenerator?.release()
        } catch (_: Exception) {
        }
        toneGenerator = null
    }
}
