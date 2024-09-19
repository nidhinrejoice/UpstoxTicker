package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import javax.inject.Inject

class GetLongTermHoldings @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(): List<StockDetails> {
        val stocks = portfolioRepository.getLongTermHoldings()
        val totalAmount = stocks.map { it.quantity * it.last_price }
            .reduce { acc, i -> acc + i }
        stocks.map {
            it.portfolio_share = it.current_amount / totalAmount * 100
        }
        return stocks

    }
}