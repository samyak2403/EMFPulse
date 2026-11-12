package com.samyak.emfpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samyak.emfpulse.data.model.AppMode
import com.samyak.emfpulse.ui.screen.AboutScreen
import com.samyak.emfpulse.ui.screen.EmfDetectorScreen
import com.samyak.emfpulse.ui.screen.EmfDetectorViewModel
import com.samyak.emfpulse.ui.screen.NoSensorScreen
import com.samyak.emfpulse.ui.screen.SettingsScreen
import com.samyak.emfpulse.ui.theme.AppBackgroundGradient
import com.samyak.emfpulse.ui.theme.EMFPulseTheme

enum class ScreenState {
    NO_SENSOR,
    DETECTOR,
    SETTINGS,
    ABOUT
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EMFPulseTheme {
                val viewModel: EmfDetectorViewModel = viewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                // If hardware magnetometer is detected, bypass NO_SENSOR and launch DETECTOR directly.
                // If missing, show the friendly NoSensorScreen with skip / demo options.
                var currentScreen by rememberSaveable {
                    mutableStateOf(
                        if (viewModel.isHardwareSensorAvailable) ScreenState.DETECTOR else ScreenState.NO_SENSOR
                    )
                }

                // Handle back navigation from Settings and About
                BackHandler(enabled = currentScreen == ScreenState.SETTINGS) {
                    currentScreen = ScreenState.DETECTOR
                }
                BackHandler(enabled = currentScreen == ScreenState.ABOUT) {
                    currentScreen = ScreenState.SETTINGS
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppBackgroundGradient)
                        .safeDrawingPadding()
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "ScreenNavigation"
                    ) { screen ->
                        when (screen) {
                            ScreenState.NO_SENSOR -> {
                                NoSensorScreen(
                                    onContinueInTestMode = {
                                        viewModel.setAppMode(AppMode.TEST)
                                        currentScreen = ScreenState.DETECTOR
                                    },
                                    onSkip = {
                                        currentScreen = ScreenState.DETECTOR
                                    }
                                )
                            }
                            ScreenState.DETECTOR -> {
                                EmfDetectorScreen(
                                    viewModel = viewModel,
                                    onOpenSettings = { currentScreen = ScreenState.SETTINGS }
                                )
                            }
                            ScreenState.SETTINGS -> {
                                SettingsScreen(
                                    state = state,
                                    onNavigateBack = { currentScreen = ScreenState.DETECTOR },
                                    onNavigateToAbout = { currentScreen = ScreenState.ABOUT },
                                    onSelectMode = viewModel::setAppMode,
                                    onToggleSound = viewModel::setBeepEnabled,
                                    onSelectUnit = viewModel::setUnit,
                                    onResetCalibration = viewModel::toggleZeroCalibrate,
                                    onToggleSensorLocation = viewModel::setSensorLocationVisible,
                                    onSelectSensorPosition = viewModel::setSensorPosition,
                                    onAutoDetectSensorPosition = viewModel::autoDetectSensorPosition
                                )
                            }
                            ScreenState.ABOUT -> {
                                AboutScreen(
                                    onNavigateBack = { currentScreen = ScreenState.SETTINGS }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
