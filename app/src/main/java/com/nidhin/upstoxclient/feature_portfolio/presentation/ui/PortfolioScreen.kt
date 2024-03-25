package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material.icons.twotone.Send
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.SortOrder
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StocksEvent
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.feature_portfolio.presentation.util.Screen
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.twoDecimalPlaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    navController: NavController,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    if (state.stocks.isNotEmpty()) {
        val currentAmount =
            state.stocks.map { it.quantity * it.last_price }
                .reduce { acc, i -> acc + i }
        val investedAmount =
            state.stocks.map { it.quantity * it.average_price }
                .reduce { acc, i -> acc + i }
        val percentageGain =
            ((currentAmount - investedAmount) / investedAmount) * 100
        val losersCount: Float =
            state.stocks.count { it.percentage_gain < 0 }.toFloat()
        val gainersCount: Float =
            (state.stocks.count() - losersCount).toFloat()
        val dailyLosersCount: Float =
            state.stocks.count { it.day_change_percentage < 0 }.toFloat()
        val dailyGainersCount: Float =
            (state.stocks.count() - dailyLosersCount).toFloat()
        val dailyPnl =
            state.stocks.map { it.quantity * (it.last_price - it.close_price) }
                .reduce { acc, i -> acc + i }
        val dailyPnlPercentage =
            dailyPnl / investedAmount * 100

        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.isRefreshing.value),
            onRefresh = { viewModel.getUserHoldings() },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Holdings",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                            IconButton(onClick = {
                                navController.navigate(
                                    Screen.ProfitLossReport.route
                                )
                            }) {
                                Icon(imageVector = Icons.Rounded.Money, contentDescription = "Profit Report")
                            }
                        }
                    }
                }
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "Current Value",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "₹" + currentAmount.toInt(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End,
                                    /*modifier = Modifier.clickable {
                                        navController.navigate(
                                            (Screen.StockAllocation.route)
                                        )
                                    }*/) {
                                    Text(
                                        text = "Total Investment",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "₹" + investedAmount.toInt(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "Total P&L",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = (currentAmount - investedAmount).formatCurrency() + " (${percentageGain.twoDecimalPlaces()}%)",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Daily P&L",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = dailyPnl.formatCurrency() + " (${dailyPnlPercentage.twoDecimalPlaces()}%)",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                            GainersAndLosers(gainersCount = gainersCount, losersCount = losersCount)
                        }
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            Text(
                                text = "Sort By :", modifier = Modifier.padding(4.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        val sortingList = listOf(
                            StockOrder.Name(OrderType.Ascending),
                            StockOrder.InvestedAmt(OrderType.Ascending),
                            StockOrder.DailyPnl(OrderType.Ascending),
                            StockOrder.DailyPerc(OrderType.Ascending),
                            StockOrder.Pnl(OrderType.Ascending),
                            StockOrder.Perc(OrderType.Ascending),
                            StockOrder.CurrentAmt(OrderType.Ascending),
                            StockOrder.Price(OrderType.Ascending)
                            )
                        itemsIndexed(
                            sortingList
                        ) { index, it ->
                            val ascending =
                                state.stockOrder.orderType == OrderType.Ascending
                            val arrowText =
                                if (ascending) "\u2191" else "\u2193"
                            val stockOrder =
                                it.copy(orderType = state.stockOrder.orderType)
                            ElevatedFilterChip(
                                selected = it::class == state.stockOrder::class,
                                onClick = {
                                    viewModel.onEvent(
                                        StocksEvent.Order(
                                            stockOrder
                                        )
                                    )
                                },
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ),
                                label = {
                                    Text(text = arrowText + " " + it.label)
                                }
                            )
                        }
                    }
                }
                item {
                    GainersAndLosers(
                        gainersCount = dailyGainersCount,
                        losersCount = dailyLosersCount
                    )
                }

                items(state.stocks, key = {
                    it.instrument_token
                }) {
                    StocksListing(it, state.stockOrder) { stock ->
                        navController.navigate(
                            (Screen.StockDetails.route + "?instrument_token=${stock.instrument_token}&symbol=${
                                stock.trading_symbol.replace(
                                    "&",
                                    "%26"
                                )
                            }&exchange=${stock.exchange}&company_name=${stock.company_name}")
                        )
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Please wait while stocks are being fetched...")
        }
    }
}