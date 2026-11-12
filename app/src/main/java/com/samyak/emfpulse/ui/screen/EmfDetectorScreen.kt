package com.samyak.emfpulse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samyak.emfpulse.R
import com.samyak.emfpulse.data.model.AppMode
import com.samyak.emfpulse.data.model.EmfUnit
import com.samyak.emfpulse.data.model.SensorPosition
import com.samyak.emfpulse.ui.components.ControlBtn
import com.samyak.emfpulse.ui.components.LedSegmentIndicator
import com.samyak.emfpulse.ui.components.LiveTimeGraph
import com.samyak.emfpulse.ui.components.ReadoutBox
import com.samyak.emfpulse.ui.components.SensorLocationIndicator
import com.samyak.emfpulse.ui.components.SpeedometerGauge
import com.samyak.emfpulse.ui.components.StatusIndicatorBanner
import com.samyak.emfpulse.ui.theme.AccentCoral
import com.samyak.emfpulse.ui.theme.AccentRose
import com.samyak.emfpulse.ui.theme.AppBackgroundGradient
import com.samyak.emfpulse.ui.theme.EMFPulseTheme
import com.samyak.emfpulse.ui.theme.SafeGreen
import com.samyak.emfpulse.ui.theme.TextPrimary
import com.samyak.emfpulse.ui.theme.WarningYellow

/**
 * Stateful entry composable for EMF Radiation Detector.
 */
@Composable
fun EmfDetectorScreen(
    viewModel: EmfDetectorViewModel = viewModel(),
    onOpenSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    EmfDetectorContent(
        state = state,
        onOpenSettings = onOpenSettings,
        onToggleHold = viewModel::toggleHold,
        onToggleZeroCalibrate = viewModel::toggleZeroCalibrate,
        onToggleBeep = viewModel::toggleBeep,
        onToggleUnit = viewModel::toggleUnit,
        onPositionChanged = viewModel::setSensorPosition,
        onAutoDetect = viewModel::autoDetectSensorPosition
    )
}

/**
 * Stateless UI rendering of the detector screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmfDetectorContent(
    state: EmfUiState,
    onOpenSettings: () -> Unit,
    onToggleHold: () -> Unit,
    onToggleZeroCalibrate: () -> Unit,
    onToggleBeep: () -> Unit,
    onToggleUnit: () -> Unit,
    onPositionChanged: (SensorPosition) -> Unit,
    onAutoDetect: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Top Toolbar with Settings Icon
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.app_title_first),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = stringResource(R.string.app_title_second),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentRose,
                            letterSpacing = 0.5.sp
                        )

                        if (state.appMode == AppMode.TEST) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AccentRose.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.badge_test_mode),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentRose
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.action_settings),
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // 2. Main Scrollable Dashboard Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Missing Sensor Warning (shown only in production mode when no magnetometer is present)
                if (!state.isSensorAvailable && state.appMode == AppMode.PRODUCTION) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF482025)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.no_sensor_detector_warning),
                            color = TextPrimary,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp
                        )
                    }
                }

                // 1. Hazard Status Indicator Banner
                StatusIndicatorBanner(status = state.status)

                // 2. LED Segment Level Indicator
                LedSegmentIndicator(emfValue = state.calibratedMagnitude)

                // 3. Dynamic SpeedView Gauge Meter (from libs.speedometer)
                val gaugeMax = if (state.unit == EmfUnit.MILLIGAUSS) 3000f else 300f
                SpeedometerGauge(
                    speed = state.displayMagnitude,
                    maxSpeed = gaugeMax,
                    unit = state.unit.symbol
                )

                // 4. Axis Readouts (X, Y, Z)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReadoutBox(
                        title = stringResource(R.string.axis_x),
                        value = "%.1f".format(state.displayX),
                        accentColor = AccentCoral,
                        modifier = Modifier.weight(1f)
                    )
                    ReadoutBox(
                        title = stringResource(R.string.axis_y),
                        value = "%.1f".format(state.displayY),
                        accentColor = SafeGreen,
                        modifier = Modifier.weight(1f)
                    )
                    ReadoutBox(
                        title = stringResource(R.string.axis_z),
                        value = "%.1f".format(state.displayZ),
                        accentColor = AccentRose,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Min & Peak Readouts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReadoutBox(
                        title = stringResource(R.string.readout_min, state.unit.symbol),
                        value = "%.1f".format(state.displayMin),
                        accentColor = WarningYellow,
                        modifier = Modifier.weight(1f)
                    )
                    ReadoutBox(
                        title = stringResource(R.string.readout_peak, state.unit.symbol),
                        value = "%.1f".format(state.displayPeak),
                        accentColor = AccentRose,
                        modifier = Modifier.weight(1f)
                    )
                }

                // 5. Real-Time Scrolling Waveform Graph
                LiveTimeGraph(
                    history = state.history,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )

                // 6. Action Control Buttons (Hold & Zero Calibration)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ControlBtn(
                        label = if (state.isHolding) stringResource(R.string.btn_resume) else stringResource(R.string.btn_hold),
                        isActive = state.isHolding,
                        modifier = Modifier.weight(1f),
                        onClick = onToggleHold
                    )

                    ControlBtn(
                        label = if (state.baselineOffset == 0f) stringResource(R.string.btn_zero_cal) else stringResource(R.string.btn_reset_cal),
                        isActive = state.baselineOffset != 0f,
                        modifier = Modifier.weight(1f),
                        onClick = onToggleZeroCalibrate
                    )
                }

                // Secondary Buttons (Sound & Unit Quick Toggles)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ControlBtn(
                        label = if (state.isBeepEnabled) stringResource(R.string.btn_sound_on) else stringResource(R.string.btn_sound_off),
                        isActive = state.isBeepEnabled,
                        modifier = Modifier.weight(1f),
                        onClick = onToggleBeep
                    )

                    ControlBtn(
                        label = stringResource(R.string.btn_unit, state.unit.symbol),
                        isActive = state.unit != EmfUnit.MICROTESLA,
                        modifier = Modifier.weight(1f),
                        onClick = onToggleUnit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // 3. Sensor Location Indicator Badge on Device Edge (matching image example)
        if (state.isSensorLocationVisible) {
            val alignment = when (state.sensorPosition) {
                SensorPosition.LEFT -> Alignment.TopStart
                SensorPosition.RIGHT -> Alignment.TopEnd
                SensorPosition.TOP_LEFT -> Alignment.TopStart
                SensorPosition.TOP_RIGHT -> Alignment.TopEnd
            }
            SensorLocationIndicator(
                position = state.sensorPosition,
                onPositionChanged = onPositionChanged,
                onAutoDetect = onAutoDetect,
                modifier = Modifier.align(alignment)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmfDetectorContentPreview() {
    EMFPulseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackgroundGradient)
        ) {
            EmfDetectorContent(
                state = EmfUiState(
                    calibratedMagnitude = 487.6f,
                    peakReading = 487.6f,
                    minReading = 487.6f,
                    unit = EmfUnit.MILLIGAUSS,
                    history = listOf(200f, 250f, 300f, 400f, 487.6f)
                ),
                onOpenSettings = {},
                onToggleHold = {},
                onToggleZeroCalibrate = {},
                onToggleBeep = {},
                onToggleUnit = {},
                onPositionChanged = {}
            )
        }
    }
}
