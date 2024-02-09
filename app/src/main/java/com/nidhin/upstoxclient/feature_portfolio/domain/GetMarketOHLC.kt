package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.Ohlc
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketOHLC @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(instrumentToken: String, symbol: String, exchange: String): Flow<Ohlc> {
        return portfolioRepository.getMarketOHLC(instrumentToken,symbol.uppercase(),exchange.uppercase())
    }
}