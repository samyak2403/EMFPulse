package com.samyak.emfpulse.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.emfpulse.R
import com.samyak.emfpulse.ui.components.ControlBtn
import com.samyak.emfpulse.ui.theme.AccentRose
import com.samyak.emfpulse.ui.theme.DarkBorder
import com.samyak.emfpulse.ui.theme.DarkSurface
import com.samyak.emfpulse.ui.theme.TextPrimary
import com.samyak.emfpulse.ui.theme.TextSecondary

/**
 * Dedicated screen shown when the device lacks a built-in magnetometer sensor.
 * Allows the user to skip or seamlessly enter Test/Demo Mode.
 */
@Composable
fun NoSensorScreen(
    onContinueInTestMode: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Glowing Icon Backdrop with Custom Magnetic Sensor Logo
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(AccentRose.copy(alpha = 0.12f))
                    .border(2.dp, AccentRose.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_magnetic_sensor),
                    contentDescription = stringResource(R.string.no_sensor_title),
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Status Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF482025))
                    .border(1.dp, AccentRose.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.no_sensor_badge),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentRose,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title
            Text(
                text = stringResource(R.string.no_sensor_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Detailed Explanation
            Text(
                text = stringResource(R.string.no_sensor_desc),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Demo Mode Features Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.no_sensor_features_title),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentRose,
                    letterSpacing = 0.8.sp
                )

                FeatureRow(
                    icon = Icons.Default.Speed,
                    title = stringResource(R.string.feature_speedview_title),
                    description = stringResource(R.string.feature_speedview_desc)
                )

                FeatureRow(
                    icon = Icons.Default.AutoGraph,
                    title = stringResource(R.string.feature_waveform_title),
                    description = stringResource(R.string.feature_waveform_desc)
                )

                FeatureRow(
                    icon = Icons.Default.GraphicEq,
                    title = stringResource(R.string.feature_geiger_title),
                    description = stringResource(R.string.feature_geiger_desc)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Primary Action: Enable Test Mode & Continue
            ControlBtn(
                label = stringResource(R.string.btn_continue_test_mode),
                isActive = true,
                modifier = Modifier.fillMaxWidth(),
                onClick = onContinueInTestMode
            )

            // Secondary Action: Skip
            ControlBtn(
                label = stringResource(R.string.btn_skip_anyway),
                isActive = false,
                modifier = Modifier.fillMaxWidth(),
                onClick = onSkip
            )
        }
    }
}

@Composable
private fun FeatureRow(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(AccentRose.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentRose,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}
