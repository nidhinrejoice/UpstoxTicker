package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.utils.sortByOrder


class SortHoldingsList {


    operator fun invoke(
        stockOrder: StockOrder,
        stocks: List<StockDetails>
    ): List<StockDetails> {

        val strategy =  when (stockOrder) {
            is StockOrder.Price -> PriceSortingStrategy
            is StockOrder.Pnl -> PnlSortingStrategy
            is StockOrder.InvestedAmt -> InvestedAmountSortingStrategy
            is StockOrder.CurrentAmt -> CurrentAmountSortingStrategy
            is StockOrder.DailyPerc -> DailyPercentSortingStrategy
            is StockOrder.DailyPnl -> DailyGainSortingStrategy
            is StockOrder.Name -> CompanyNameSortingStrategy
            is StockOrder.Perc -> PercentGainSortingStrategy

        }
        return strategy.sort(stocks,stockOrder.orderType)

    }
}