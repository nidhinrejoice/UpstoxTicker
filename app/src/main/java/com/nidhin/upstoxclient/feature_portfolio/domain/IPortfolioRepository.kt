package com.nidhin.upstoxclient.feature_portfolio.domain

import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.nidhin.upstoxclient.feature_portfolio.data.ScriptProfitLoss
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.Ohlc
import com.nidhin.upstoxclient.feature_portfolio.data.models.newsapiresponse.NewsApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface IPortfolioRepository {

    suspend fun checkUserAuthenticated(): Boolean
    suspend fun generateAccessToken(code: String): Boolean
    suspend fun getLongTermHoldings(): List<StockDetails>
    suspend fun getMarketOHLC(instrumentToken: String, symbol: String, exchange: String): Flow<Ohlc>

    suspend fun getLatestNewsFromGemini(companyName: String): Flow<GenerateContentResponse>
    suspend fun getProfitLossReport(financialYear: String): Flow<List<ScriptProfitLoss>>
    suspend fun getNews(query: String, page: Int): Flow<NewsApiResponse>
}