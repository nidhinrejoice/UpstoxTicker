package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.ScriptProfitLoss
import com.nidhin.upstoxclient.feature_portfolio.domain.models.Month
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StocksEvent
import com.nidhin.upstoxclient.utils.convertIsoSecondFormatToDefaultDate
import com.nidhin.upstoxclient.utils.toDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetProfitLoss @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(
        financialYear: String,
        filterMonth: Month?
    ): Flow<List<ScriptProfitLoss>> {
        val years = financialYear.split("-")
        val year = years[0].substring(2, 4) + years[1].substring(2, 4)
        if (filterMonth == null) {
            val report = portfolioRepository.getProfitLossReport(
                year
            )
            return report
        }
        val rawData = portfolioRepository.getProfitLossRawData(year)
        var monthValue = filterMonth.ordinal.plus(4)
        if (monthValue > 12)
            monthValue -= 12
        val monthString = if (monthValue < 10) "0$monthValue" else monthValue
        val scripts = rawData.filter {   it.sell_date.substring(3, 5) == monthString }.groupBy { it.scrip_name }
        val profitLossList: MutableList<ScriptProfitLoss> = mutableListOf()
        val iterator = scripts.keys.iterator()
        while (iterator.hasNext()) {
            val sName = iterator.next()
            val scriptDetails = scripts[sName]
            val totalPnL =
                scriptDetails?.map { it.sell_amount - it.buy_amount }?.reduce { acc, i ->
                    acc + i
                } ?: 0.0
            profitLossList.add(
                ScriptProfitLoss(
                    sName,
                    totalPnL,
                    dataList = scriptDetails ?: emptyList()
                )
            )
        }
        return flowOf(profitLossList)
    }
}