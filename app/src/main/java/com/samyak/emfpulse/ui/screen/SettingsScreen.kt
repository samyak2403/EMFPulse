package com.samyak.emfpulse.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.emfpulse.R
import com.samyak.emfpulse.data.model.AppMode
import com.samyak.emfpulse.data.model.EmfUnit
import com.samyak.emfpulse.data.model.SensorPosition
import com.samyak.emfpulse.ui.components.CustomToggleSwitch
import com.samyak.emfpulse.ui.theme.AccentRose
import com.samyak.emfpulse.ui.theme.DarkBorder
import com.samyak.emfpulse.ui.theme.DarkSurface
import com.samyak.emfpulse.ui.theme.DarkSurfaceVariant
import com.samyak.emfpulse.ui.theme.SafeGreen
import com.samyak.emfpulse.ui.theme.TextPrimary
import com.samyak.emfpulse.ui.theme.TextSecondary

private const val GITHUB_REPO_URL = "https://github.com/samyak2403/EMFPulse"
private const val DEVELOPER_PROFILE_URL = "https://github.com/samyak2403"

/**
 * Settings Screen for configuring EMFPulse operation mode, audio alerts, and units.
 * All texts are referenced through string resources.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: EmfUiState,
    onNavigateBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onSelectMode: (AppMode) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onSelectUnit: (EmfUnit) -> Unit,
    onResetCalibration: () -> Unit,
    onToggleSensorLocation: (Boolean) -> Unit = {},
    onSelectSensorPosition: (SensorPosition) -> Unit = {},
    onAutoDetectSensorPosition: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top Toolbar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.settings_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nav_back),
                        tint = TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Settings Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Section 1: Operation Mode (Production vs Test Mode)
            SettingsSectionHeader(title = stringResource(R.string.settings_section_mode))

            ModeSelectionCard(
                title = stringResource(R.string.settings_mode_production_title),
                description = stringResource(R.string.settings_mode_production_desc),
                icon = Icons.Default.Sensors,
                isSelected = state.appMode == AppMode.PRODUCTION,
                onClick = { onSelectMode(AppMode.PRODUCTION) }
            )

            ModeSelectionCard(
                title = stringResource(R.string.settings_mode_test_title),
                description = stringResource(R.string.settings_mode_test_desc),
                icon = Icons.Default.Science,
                isSelected = state.appMode == AppMode.TEST,
                badge = stringResource(R.string.badge_simulation),
                onClick = { onSelectMode(AppMode.TEST) }
            )

            // Section 2: Audio Geiger Alert Setting
            SettingsSectionHeader(title = stringResource(R.string.settings_section_audio))

            SettingsSwitchTile(
                title = stringResource(R.string.settings_geiger_title),
                description = stringResource(R.string.settings_geiger_desc),
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                checked = state.isBeepEnabled,
                onCheckedChange = onToggleSound
            )

            // Section 3: Measurement Units
            SettingsSectionHeader(title = stringResource(R.string.settings_section_unit))

            SettingsSwitchTile(
                title = stringResource(R.string.settings_unit_mg_toggle_title),
                description = if (state.unit == EmfUnit.MILLIGAUSS) {
                    stringResource(R.string.settings_unit_mg_toggle_desc_on)
                } else {
                    stringResource(R.string.settings_unit_mg_toggle_desc_off)
                },
                icon = Icons.Default.Tune,
                checked = state.unit == EmfUnit.MILLIGAUSS,
                onCheckedChange = { isMg ->
                    onSelectUnit(if (isMg) EmfUnit.MILLIGAUSS else EmfUnit.MICROTESLA)
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UnitSelectionCard(
                    symbol = stringResource(R.string.unit_microtesla_symbol),
                    title = stringResource(R.string.unit_microtesla_title),
                    subtitle = stringResource(R.string.unit_microtesla_subtitle),
                    isSelected = state.unit == EmfUnit.MICROTESLA,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectUnit(EmfUnit.MICROTESLA) }
                )

                UnitSelectionCard(
                    symbol = stringResource(R.string.unit_milligauss_symbol),
                    title = stringResource(R.string.unit_milligauss_title),
                    subtitle = stringResource(R.string.unit_milligauss_subtitle),
                    isSelected = state.unit == EmfUnit.MILLIGAUSS,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectUnit(EmfUnit.MILLIGAUSS) }
                )
            }

            // Section 4: Sensor Status & Calibration
            SettingsSectionHeader(title = stringResource(R.string.settings_section_sensor))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.settings_hardware_sensor),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Text(
                        text = if (state.isSensorAvailable) {
                            stringResource(R.string.settings_sensor_detected)
                        } else {
                            stringResource(R.string.settings_sensor_unavailable)
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (state.isSensorAvailable) SafeGreen else Color(0xFFEF4444)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.settings_baseline_offset),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                        Text(
                            text = "%.1f %s".format(state.unit.convert(state.baselineOffset), state.unit.symbol),
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .clickable(onClick = onResetCalibration)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.settings_reset_zero),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentRose
                        )
                    }
                }
            }

            // Sensor Location Indicator Toggle & Position Selector
            SettingsSwitchTile(
                title = stringResource(R.string.settings_show_sensor_location),
                description = stringResource(R.string.settings_show_sensor_location_desc),
                icon = Icons.Default.Sensors,
                checked = state.isSensorLocationVisible,
                onCheckedChange = onToggleSensorLocation
            )

            if (state.isSensorLocationVisible) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.settings_sensor_position),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )

                    TextButton(
                        onClick = onAutoDetectSensorPosition,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AccentRose,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.sensor_pos_auto_detect),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentRose
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SensorPosition.entries.forEach { pos ->
                        val isSelected = pos == state.sensorPosition
                        val shape = RoundedCornerShape(10.dp)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(shape)
                                .background(if (isSelected) AccentRose.copy(alpha = 0.2f) else DarkSurface)
                                .border(1.dp, if (isSelected) AccentRose else DarkBorder, shape)
                                .clickable { onSelectSensorPosition(pos) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(pos.labelResId),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) AccentRose else TextPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Section 5: Source Code, Developer & About
            SettingsSectionHeader(title = stringResource(R.string.settings_section_about))

            SettingsClickableTile(
                title = stringResource(R.string.settings_about_app_title),
                description = stringResource(R.string.settings_about_app_desc),
                icon = Icons.Outlined.Info,
                trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = onNavigateToAbout
            )

            SettingsClickableTile(
                title = stringResource(R.string.settings_developer_title),
                description = stringResource(R.string.settings_developer_desc),
                icon = Icons.Default.Person,
                trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
                onClick = { launchBrowser(context, DEVELOPER_PROFILE_URL) }
            )

            SettingsClickableTile(
                title = stringResource(R.string.settings_source_code_title),
                description = stringResource(R.string.settings_source_code_desc),
                icon = Icons.Default.Code,
                trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
                onClick = { launchBrowser(context, GITHUB_REPO_URL) }
            )

            // Section 6: App Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.settings_app_info),
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextSecondary,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 2.dp)
    )
}

@Composable
private fun ModeSelectionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    badge: String? = null,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) AccentRose else DarkBorder
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(DarkSurface)
            .border(1.5.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(if (isSelected) AccentRose.copy(alpha = 0.15f) else DarkSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) AccentRose else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (badge != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(AccentRose.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentRose
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .border(2.dp, if (isSelected) AccentRose else DarkBorder, CircleShape)
                .background(if (isSelected) AccentRose else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF18181B),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * Settings list tile incorporating the custom emerald I/O pill switch.
 */
@Composable
private fun SettingsSwitchTile(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(DarkSurface)
            .border(1.dp, DarkBorder, shape)
            .clickable { onCheckedChange(!checked) }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (checked) Color(0xFF00CE9C).copy(alpha = 0.15f) else DarkSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) Color(0xFF00CE9C) else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Custom Emerald I/O Toggle Switch from image
        CustomToggleSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            activeTrackColor = Color(0xFF00CE9C),
            inactiveTrackColor = Color(0xFF33333A)
        )
    }
}

@Composable
private fun UnitSelectionCard(
    symbol: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val borderColor = if (isSelected) AccentRose else DarkBorder

    Column(
        modifier = modifier
            .clip(shape)
            .background(DarkSurface)
            .border(1.5.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = symbol,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSelected) AccentRose else TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

/**
 * Clickable tile for navigation and external links with trailing chevron or link icon.
 */
@Composable
private fun SettingsClickableTile(
    title: String,
    description: String,
    icon: ImageVector,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(DarkSurface)
            .border(1.dp, DarkBorder, shape)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AccentRose.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentRose,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun launchBrowser(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}
