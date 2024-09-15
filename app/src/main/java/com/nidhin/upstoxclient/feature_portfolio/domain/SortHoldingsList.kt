package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder

class SortHoldingsList {


    private inline fun <T : Comparable<T>> List<StockDetails>.sortByOrder(
        stockOrder: StockOrder,
        crossinline selector: (StockDetails) -> T
    ): List<StockDetails> {
        return if (stockOrder.orderType == OrderType.Ascending) {
            this.sortedBy(selector)
        } else {
            this.sortedByDescending(selector)
        }
    }

    operator fun invoke(
        stockOrder: StockOrder,
        stocks: List<StockDetails>
    ): List<StockDetails> {
        return when (stockOrder) {
            is StockOrder.Price -> stocks.sortByOrder(stockOrder) { it.last_price }
            is StockOrder.Pnl -> stocks.sortByOrder(stockOrder) { it.current_amount - it.invested_amount }
            is StockOrder.InvestedAmt -> stocks.sortByOrder(stockOrder) { it.invested_amount }
            is StockOrder.CurrentAmt -> stocks.sortByOrder(stockOrder) { it.current_amount }
            is StockOrder.DailyPerc -> stocks.sortByOrder(stockOrder) { it.day_change_percentage }
            is StockOrder.DailyPnl -> stocks.sortByOrder(stockOrder) { it.day_change }
            is StockOrder.Name -> stocks.sortByOrder(stockOrder) { it.company_name }
            is StockOrder.Perc -> stocks.sortByOrder(stockOrder) { it.percentage_gain }

        }

    }
}