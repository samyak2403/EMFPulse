package com.samyak.emfpulse.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.emfpulse.R
import com.samyak.emfpulse.data.model.SensorPosition
import com.samyak.emfpulse.ui.theme.AccentRose
import com.samyak.emfpulse.ui.theme.DarkBorder
import com.samyak.emfpulse.ui.theme.DarkSurface
import com.samyak.emfpulse.ui.theme.DarkSurfaceVariant
import com.samyak.emfpulse.ui.theme.TextPrimary
import com.samyak.emfpulse.ui.theme.TextSecondary

/**
 * Visual indicator pinned to the physical location of the device's magnetic sensor.
 * Displays a sleek edge tab with ic_magnetic_sensor matching the reference design.
 */
@Composable
fun SensorLocationIndicator(
    position: SensorPosition,
    onPositionChanged: (SensorPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    val (offsetModifier, tabShape, iconRotation) = when (position) {
        SensorPosition.LEFT -> {
            Triple(
                Modifier.offset(x = 0.dp, y = 210.dp),
                RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp),
                90f // Base flush with left bezel, magnet loop pointing inward into screen
            )
        }
        SensorPosition.RIGHT -> {
            Triple(
                Modifier.offset(x = 0.dp, y = 210.dp),
                RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp, topEnd = 0.dp, bottomEnd = 0.dp),
                270f // Base flush with right bezel, magnet loop pointing inward into screen
            )
        }
        SensorPosition.TOP_LEFT -> {
            Triple(
                Modifier.offset(x = 16.dp, y = 60.dp),
                RoundedCornerShape(8.dp),
                180f
            )
        }
        SensorPosition.TOP_RIGHT -> {
            Triple(
                Modifier.offset(x = (-16).dp, y = 60.dp),
                RoundedCornerShape(8.dp),
                180f
            )
        }
    }

    Box(
        modifier = modifier
            .then(offsetModifier)
            .clip(tabShape)
            .background(Color(0xFF000000))
            .border(1.dp, Color(0xFF2E2E34), tabShape)
            .clickable { showDialog = true }
            .padding(horizontal = 4.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_magnetic_sensor),
            contentDescription = stringResource(R.string.sensor_location_title),
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(24.dp)
                .rotate(iconRotation)
        )
    }

    if (showDialog) {
        SensorLocationDialog(
            currentPosition = position,
            onPositionSelect = {
                onPositionChanged(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun SensorLocationDialog(
    currentPosition: SensorPosition,
    onPositionSelect: (SensorPosition) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_magnetic_sensor),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(R.string.sensor_location_dialog_title),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.sensor_location_dialog_desc),
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                // Position Selector Chips
                Text(
                    text = stringResource(R.string.settings_sensor_position),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SensorPosition.entries.forEach { pos ->
                        val isSelected = pos == currentPosition
                        val shape = RoundedCornerShape(8.dp)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(shape)
                                .background(if (isSelected) AccentRose.copy(alpha = 0.2f) else DarkSurfaceVariant)
                                .border(1.dp, if (isSelected) AccentRose else DarkBorder, shape)
                                .clickable { onPositionSelect(pos) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(pos.labelResId),
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) AccentRose else TextPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Practical calibration tip
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF23232A)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.sensor_location_tip),
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(10.dp),
                        lineHeight = 15.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.action_close),
                    color = AccentRose,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}
