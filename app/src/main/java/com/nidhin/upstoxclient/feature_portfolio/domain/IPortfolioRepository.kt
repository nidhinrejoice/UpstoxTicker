package com.nidhin.upstoxclient.feature_portfolio.domain

import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.Ohlc
import kotlinx.coroutines.flow.Flow

interface IPortfolioRepository {

    suspend fun checkUserAuthenticated() : Boolean
    suspend fun generateAccessToken(code: String): Boolean
    suspend fun getLongTermHoldings(): List<StockDetails>
    suspend fun getMarketOHLC(instrumentToken: String, symbol: String, exchange: String): Flow<Ohlc>

    suspend fun getLatestNewsFromGemini(companyName : String) : Flow<GenerateContentResponse>
}