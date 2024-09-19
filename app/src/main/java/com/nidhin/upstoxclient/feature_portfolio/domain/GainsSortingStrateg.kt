package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.ScriptProfitLoss
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.utils.sortGainsByOrder

interface GainsSortingStrategy {
    fun sort(stocks: List<ScriptProfitLoss>, orderType: OrderType): List<ScriptProfitLoss>
}

object TotalGainsSortingStrategy : GainsSortingStrategy {
    override fun sort(stocks: List<ScriptProfitLoss>, orderType: OrderType): List<ScriptProfitLoss> {
        return stocks.sortGainsByOrder(orderType) { it.pnl }
    }
}

object CompanyGainSortingStrategy : GainsSortingStrategy {
    override fun sort(stocks: List<ScriptProfitLoss>, orderType: OrderType): List<ScriptProfitLoss> {
        return stocks.sortGainsByOrder(orderType) { it.scriptName }
    }
}
