package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.twoDecimalPlaces

@Composable
fun StockInfo(
    instrumentToken: String,
    symbol: String,
    exchange: String,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = false) {
        viewModel.getMarketOHLC(instrumentToken, symbol, exchange)
    }
    val state = viewModel.state.value
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            state.selectedStock?.let {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        it.ohlc?.let { ohlc ->
                            val negativeDay = (it.last_price - ohlc.open < 0)
                            val negativeGain = (it.day_change_percentage < 0)
                            val color = if (negativeDay) Color.Red else Green

                            Column(modifier = Modifier.weight(0.9f)) {
                                Row(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = it.company_name,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if(negativeGain) Color.Red else Green,
                                        text = "${it.last_price.formatCurrency()} "
                                    )
                                    Text(
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if(negativeGain) Color.Red else Green,
                                        text = "(" + it.day_change_percentage.twoDecimalPlaces() + "%)"
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.labelMedium,
                                        text = "${it.average_price.formatCurrency()} x ${it.quantity} Nos -> ${it.invested_amount.formatCurrency()}"
                                    )

                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.labelMedium,
                                        text = "Current Value : ${it.current_amount.formatCurrency()}"
                                    )

                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.labelMedium,
                                        text = "Portfolio Share : ${it.portfolio_share.twoDecimalPlaces()}%"
                                    )

                                }
                                val gainString = if (negativeGain) "Loss of "
                                else "Gain of "
                                Text(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (negativeGain) Color.Red else Green,
                                    text = "$gainString${it.total_gain.formatCurrency()} (${it.percentage_gain.twoDecimalPlaces()}%)"
                                )
                            }
                            Box(
                                modifier = Modifier.weight(0.1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(80.dp)
                                ) {
                                    if (!negativeDay) {
                                        val shadowLength = size.height / 2
                                        val totalRange = ohlc.high - ohlc.low
                                        val coreStartsAt =
                                            shadowLength - (((ohlc.open - ohlc.low) / totalRange) * shadowLength)
                                        val coreEndsAt =
                                            shadowLength - (((ohlc.close - ohlc.low) / totalRange) * shadowLength)
                                        drawLine(
                                            color = color,
                                            start = Offset(size.width / 2, size.height / 2),
                                            end = Offset(
                                                size.width / 2,
                                                0f
                                            ),
                                            strokeWidth = 5f
                                        )
                                        drawLine(
                                            color = color,
                                            start = Offset(size.width / 2, coreStartsAt.toFloat()),
                                            end = Offset(
                                                size.width / 2,
                                                coreEndsAt.toFloat()
                                            ),
                                            strokeWidth = 15f
                                        )
                                    } else {
                                        val shadowLength = size.height / 2
                                        val totalRange = ohlc.high - ohlc.low
                                        val coreStartsAt =
                                            shadowLength - (((ohlc.close - ohlc.low) / totalRange) * shadowLength)
                                        val coreEndsAt =
                                            shadowLength - (((ohlc.open - ohlc.low) / totalRange) * shadowLength)
                                        drawLine(
                                            color = color,
                                            start = Offset(size.width / 2, size.height / 2),
                                            end = Offset(
                                                size.width / 2,
                                                0f
                                            ),
                                            strokeWidth = 5f
                                        )
                                        drawLine(
                                            color = color,
                                            start = Offset(size.width / 2, coreStartsAt.toFloat()),
                                            end = Offset(
                                                size.width / 2,
                                                coreEndsAt.toFloat()
                                            ),
                                            strokeWidth = 15f
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}