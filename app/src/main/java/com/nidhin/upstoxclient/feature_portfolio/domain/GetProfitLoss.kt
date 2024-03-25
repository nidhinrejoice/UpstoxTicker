package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.ScriptProfitLoss
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfitLoss @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(financialYear : String): Flow<List<ScriptProfitLoss>> {

        val years = financialYear.split("-")
        return portfolioRepository.getProfitLossReport(years[0].substring(2, 4) + years[1].substring(2, 4))
    }
}