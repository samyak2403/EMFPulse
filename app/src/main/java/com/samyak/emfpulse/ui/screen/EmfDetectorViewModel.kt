package com.samyak.emfpulse.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samyak.emfpulse.data.audio.EmfTonePlayer
import com.samyak.emfpulse.data.model.AppMode
import com.samyak.emfpulse.data.model.EmfReading
import com.samyak.emfpulse.data.model.EmfUnit
import com.samyak.emfpulse.data.sensor.AndroidEmfSensorManager
import com.samyak.emfpulse.data.sensor.EmfSensorManager
import com.samyak.emfpulse.data.sensor.SimulatedEmfSensorManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing state transitions, sensor feeds, audio triggers, and user settings.
 */
class EmfDetectorViewModel(
    application: Application,
    private val productionSensorManager: EmfSensorManager,
    private val simulatedSensorManager: EmfSensorManager,
    private val tonePlayer: EmfTonePlayer
) : AndroidViewModel(application) {

    /**
     * Required by ViewModelProvider.AndroidViewModelFactory for reflection instantiation.
     */
    constructor(application: Application) : this(
        application = application,
        productionSensorManager = AndroidEmfSensorManager(application),
        simulatedSensorManager = SimulatedEmfSensorManager(),
        tonePlayer = EmfTonePlayer()
    )

    private val _uiState = MutableStateFlow(
        EmfUiState(
            isSensorAvailable = productionSensorManager.isSensorAvailable,
            appMode = AppMode.PRODUCTION
        )
    )
    val uiState: StateFlow<EmfUiState> = _uiState.asStateFlow()

    /**
     * Whether the device has a physical magnetic sensor present.
     */
    val isHardwareSensorAvailable: Boolean
        get() = productionSensorManager.isSensorAvailable

    private var sensorJob: Job? = null

    init {
        observeCurrentSensor()
        startAudioLoop()
    }

    private fun observeCurrentSensor() {
        sensorJob?.cancel()
        val currentManager = if (_uiState.value.appMode == AppMode.TEST) {
            simulatedSensorManager
        } else {
            productionSensorManager
        }

        if (!currentManager.isSensorAvailable) return

        sensorJob = viewModelScope.launch {
            currentManager.startListening().collect { reading ->
                onNewSensorReading(reading)
            }
        }
    }

    private fun startAudioLoop() {
        tonePlayer.startAlertLoop(
            scope = viewModelScope,
            shouldBeep = { _uiState.value.isBeepEnabled && !_uiState.value.isHolding },
            getEmfValue = { _uiState.value.calibratedMagnitude }
        )
    }

    private fun onNewSensorReading(reading: EmfReading) {
        _uiState.update { state ->
            if (state.isHolding) return@update state

            val currentCalibrated = (reading.magnitude - state.baselineOffset).coerceAtLeast(0f)
            val updatedMax = if (currentCalibrated > state.peakReading) currentCalibrated else state.peakReading
            val updatedMin = if (currentCalibrated < state.minReading) currentCalibrated else state.minReading

            val updatedHistory = if (state.history.size >= 60) {
                state.history.drop(1) + currentCalibrated
            } else {
                state.history + currentCalibrated
            }

            state.copy(
                currentReading = reading,
                calibratedMagnitude = currentCalibrated,
                peakReading = updatedMax,
                minReading = updatedMin,
                history = updatedHistory
            )
        }
    }

    fun setAppMode(mode: AppMode) {
        if (_uiState.value.appMode == mode) return

        _uiState.update {
            it.copy(
                appMode = mode,
                isSensorAvailable = if (mode == AppMode.TEST) true else productionSensorManager.isSensorAvailable,
                baselineOffset = 0f,
                peakReading = 0f,
                minReading = Float.MAX_VALUE,
                history = emptyList()
            )
        }
        observeCurrentSensor()
    }

    fun setBeepEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isBeepEnabled = enabled) }
    }

    fun setUnit(unit: EmfUnit) {
        _uiState.update { it.copy(unit = unit) }
    }

    fun toggleHold() {
        _uiState.update { it.copy(isHolding = !it.isHolding) }
    }

    fun toggleBeep() {
        _uiState.update { it.copy(isBeepEnabled = !it.isBeepEnabled) }
    }

    fun toggleUnit() {
        _uiState.update {
            val nextUnit = if (it.unit == EmfUnit.MICROTESLA) EmfUnit.MILLIGAUSS else EmfUnit.MICROTESLA
            it.copy(unit = nextUnit)
        }
    }

    fun toggleZeroCalibrate() {
        _uiState.update { state ->
            val isCalibrated = state.baselineOffset != 0f
            val newOffset = if (isCalibrated) 0f else state.currentReading.magnitude
            state.copy(
                baselineOffset = newOffset,
                peakReading = 0f,
                minReading = Float.MAX_VALUE
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorJob?.cancel()
        tonePlayer.release()
    }
}
