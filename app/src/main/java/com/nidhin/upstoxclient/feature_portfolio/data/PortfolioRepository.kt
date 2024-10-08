package com.nidhin.upstoxclient.feature_portfolio.data

import com.google.gson.Gson
import com.nidhin.upstoxclient.api.ApiManager
import com.nidhin.upstoxclient.feature_portfolio.data.models.getprofitlossreport.Data
import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.Ohlc
import com.nidhin.upstoxclient.feature_portfolio.data.models.newsapiresponse.NewsApiResponse
import com.nidhin.upstoxclient.feature_portfolio.domain.IPortfolioRepository
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.persistance.SharedPrefsHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val apiManager: ApiManager,
) : IPortfolioRepository {
    override suspend fun checkUserAuthenticated(): Boolean {

        return (sharedPrefsHelper.hasKey("access_token"))
    }

    override suspend fun generateAccessToken(code: String): Boolean {
        val response = apiManager.generateAuthToken(code)
        response.let {
            sharedPrefsHelper.put("access_token", response.access_token)
        }
        return true
    }

    override suspend fun getLongTermHoldings(): List<StockDetails> {
        return apiManager.getLongTermHoldings(sharedPrefsHelper["access_token", ""]).data
            .filter { it.average_price != 0.0 }
            .map {
                StockDetails(
                    average_price = it.average_price,
                    close_price = it.close_price,
                    company_name = it.company_name,
                    day_change = it.day_change,
                    day_change_percentage = (it.last_price / it.close_price - 1) * 100,
                    exchange = it.exchange,
                    isin = it.isin,
                    last_price = it.last_price,
                    pnl = it.pnl,
                    quantity = it.quantity,
                    t1_quantity = it.t1_quantity,
                    trading_symbol = it.trading_symbol,
                    invested_amount = it.quantity * it.average_price,
                    percentage_gain = 0.0,
                    current_amount = it.quantity * it.last_price,
                    total_gain = 0.0,
                    instrument_token = it.instrument_token,
                    portfolio_share = 0.0
                ).apply {
                    percentage_gain = (current_amount - invested_amount) / invested_amount * 100
                    total_gain = current_amount - invested_amount
                    day_change = (last_price - close_price) * quantity
                }
            }
    }


    override suspend fun getMarketOHLC(
        instrumentToken: String,
        symbol: String,
        exchange: String
    ): Flow<Ohlc> {
        val response =
            apiManager.getMarketOHLC(sharedPrefsHelper["access_token", ""], instrumentToken)
        return response.flatMapLatest { it ->
            val gson = Gson()
            flowOf(
                gson.fromJson(
                    it.getJSONObject("data").getJSONObject("${exchange}_EQ:$symbol")
                        .getJSONObject("ohlc")
                        .toString(), Ohlc::class.java
                )
            )

        }
    }

    override suspend fun getProfitLossRawData(financialYear: String): List<Data> {
        val accessToken = sharedPrefsHelper["access_token", ""]
        val metaData = apiManager.getTradeMetaData(accessToken, financialYear)
        var maxPageSize = metaData.data.trades_count
        maxPageSize += (100 - (metaData.data.trades_count % 100))
        val response =
            apiManager.getProfitLoss(accessToken, financialYear, maxPageSize)
        return response.data.filter { it.scrip_name.isNotEmpty() }

    }

    override suspend fun getProfitLossReport(financialYear: String): Flow<List<ScriptProfitLoss>> {
        val accessToken = sharedPrefsHelper["access_token", ""]
        val metaData = apiManager.getTradeMetaData(accessToken, financialYear)
        var maxPageSize = metaData.data.trades_count
        maxPageSize += (100 - (metaData.data.trades_count % 100))
        val response =
            apiManager.getProfitLoss(accessToken, financialYear, maxPageSize)
        val scripts = response.data.filter { it.scrip_name.isNotEmpty() }.groupBy { it.scrip_name }
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
        return flowOf(profitLossList.toList())
    }

    override suspend fun getNews(key: String, page: Int): Flow<NewsApiResponse> {

        return flowOf(apiManager.getNews("$key shares", 10, page))
    }
}

