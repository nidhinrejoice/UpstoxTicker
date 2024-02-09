package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel

@Composable
    fun OrderSection(
    modifier: Modifier = Modifier,
    stockOrder: StockOrder = StockOrder.InvestedAmt(
            OrderType.Descending
        ),
    onOrderChange: (StockOrder) -> Unit
    ) {
        Column(
            modifier = modifier
        ) {
            Row() {
                DefaultRadioButton(
                    text = "Value",
                    selected = stockOrder is StockOrder.InvestedAmt,
                    onSelect = {
                        onOrderChange(StockOrder.InvestedAmt(stockOrder.orderType))
                    })
                Spacer(modifier = Modifier.width(4.dp))
                DefaultRadioButton(
                    text = "Profit",
                    selected = stockOrder is StockOrder.Pnl,
                    onSelect = {
                        onOrderChange(StockOrder.Pnl(stockOrder.orderType))
                    })
                Spacer(modifier = Modifier.width(4.dp))
                DefaultRadioButton(
                    text = "Price",
                    selected = stockOrder is StockOrder.Price,
                    onSelect = {
                        onOrderChange(StockOrder.Price(stockOrder.orderType))
                    })
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                DefaultRadioButton(
                    text = "Ascending",
                    selected = stockOrder.orderType is OrderType.Ascending,
                    onSelect = {
                        onOrderChange(stockOrder.copy(OrderType.Ascending))
                    })
                Spacer(modifier = Modifier.width(4.dp))
                DefaultRadioButton(
                    text = "Descending",
                    selected = stockOrder.orderType is OrderType.Descending,
                    onSelect = {
                        onOrderChange(stockOrder.copy(OrderType.Descending))
                    })
            }
        }
    }