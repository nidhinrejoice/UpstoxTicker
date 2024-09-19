package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.ScriptProfitLoss
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.utils.sortByOrder


class SortProfitLossReport {

    operator fun invoke(
        stockOrder: StockOrder,
        stocks: List<ScriptProfitLoss>
    ): List<ScriptProfitLoss> {

        val strategy =
            if (stockOrder is StockOrder.Pnl) TotalGainsSortingStrategy
            else CompanyGainSortingStrategy

        return strategy.sort(stocks, stockOrder.orderType)

    }
}