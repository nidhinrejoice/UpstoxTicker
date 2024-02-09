package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nidhin.upstoxclient.ui.theme.Green

@Composable
fun GainersAndLosers(gainersCount: Float,losersCount: Float) {
    val gainersPercent: Float =
        ((gainersCount / (gainersCount + losersCount)))
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "${gainersCount.toInt()} Gainers ",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "${losersCount.toInt()} Losers",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Canvas(modifier = Modifier.fillMaxWidth()) {
            drawLine(
                color = Green,
                start = Offset(0f, size.height / 2),
                end = Offset(
                    size.width.toFloat() * (gainersPercent),
                    size.height / 2
                ),
                strokeWidth = if (gainersCount > losersCount) 15f else 8f
            )
            drawLine(
                color = Color.Red,
                start = Offset(
                    size.width.toFloat() * (gainersPercent),
                    size.height / 2
                ),
                end = Offset(size.width, size.height / 2),
                strokeWidth = if (gainersCount < losersCount) 15f else 8f
            )
        }
    }
}