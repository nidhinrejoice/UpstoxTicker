package com.nidhin.upstoxclient.feature_portfolio.data

import com.google.gson.Gson
import com.nidhin.upstoxclient.api.ApiManager
import com.nidhin.upstoxclient.feature_portfolio.data.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.Ohlc
import com.nidhin.upstoxclient.feature_portfolio.domain.IPortfolioRepository
import com.nidhin.upstoxclient.persistance.SharedPrefsHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val apiManager: ApiManager
) : IPortfolioRepository {
    override suspend fun checkUserAuthenticated(): Boolean {

        return if (sharedPrefsHelper.hasKey("access_token")) {
            val authDoneAt = sharedPrefsHelper["user_authenticated_at", 0L]
            Calendar.getInstance().timeInMillis - authDoneAt <= 36000000L
        } else
            false
    }

    override suspend fun generateAccessToken(code: String): Boolean {
        val response = apiManager.generateAuthToken(code)
        response?.let {
            sharedPrefsHelper.put("access_token", response.access_token)
            sharedPrefsHelper.put("user_authenticated_at", Calendar.getInstance().timeInMillis)
        }
        return true
    }

    override suspend fun getLongTermHoldings(): List<StockDetails> {
        return apiManager.getLongTermHoldings(sharedPrefsHelper["access_token", ""]).data.filter { it.average_price != 0.0 }
            .map {
                StockDetails(
                    average_price = it.average_price,
                    close_price = it.close_price,
                    company_name = it.company_name,
                    day_change = it.day_change,
                    day_change_percentage = it.day_change_percentage,
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


    override suspend fun getMarketOHLC(instrumentToken: String, symbol: String, exchange: String): Flow<Ohlc> {
        val response =
            apiManager.getMarketOHLC(sharedPrefsHelper["access_token", ""], instrumentToken)
        return response.flatMapLatest { it ->
            val gson = Gson()
            flowOf(
                gson.fromJson(
                    it.getJSONObject("data").getJSONObject("${exchange}_EQ:$symbol").getJSONObject("ohlc")
                        .toString(), Ohlc::class.java
                )
            )

        }
    }

}