package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import javax.inject.Inject

class SortHoldingsList {

    operator fun invoke(
        stockOrder: StockOrder,
        stocks: List<StockDetails>
    ): List<StockDetails> {
        return when (stockOrder.orderType) {
            OrderType.Ascending -> {
                when (stockOrder) {
                    is StockOrder.Price -> stocks.sortedBy { it.last_price }
                    is StockOrder.Pnl -> stocks.sortedBy { it.current_amount - it.invested_amount }
                    is StockOrder.InvestedAmt -> stocks.sortedBy { it.invested_amount }
                    is StockOrder.CurrentAmt -> stocks.sortedBy { it.current_amount }
                    is StockOrder.DailyPerc -> stocks.sortedBy { it.day_change_percentage }
                    is StockOrder.DailyPnl -> stocks.sortedBy { it.day_change }
                    is StockOrder.Name -> stocks.sortedBy { it.company_name }
                    is StockOrder.Perc -> stocks.sortedBy { it.percentage_gain }
                }
            }

            OrderType.Descending -> {
                when (stockOrder) {
                    is StockOrder.Price -> stocks.sortedByDescending { it.last_price }
                    is StockOrder.Pnl -> stocks.sortedByDescending { it.current_amount - it.invested_amount }
                    is StockOrder.InvestedAmt -> stocks.sortedByDescending { it.invested_amount }
                    is StockOrder.CurrentAmt -> stocks.sortedByDescending { it.current_amount }
                    is StockOrder.DailyPerc -> stocks.sortedByDescending { it.day_change_percentage }
                    is StockOrder.DailyPnl -> stocks.sortedByDescending { it.day_change }
                    is StockOrder.Name -> stocks.sortedByDescending { it.company_name }
                    is StockOrder.Perc -> stocks.sortedByDescending { it.percentage_gain }
                }
            }

        }

    }
}