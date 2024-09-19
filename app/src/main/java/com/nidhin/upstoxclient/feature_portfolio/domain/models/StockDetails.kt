package com.nidhin.upstoxclient.feature_portfolio.domain.models

import androidx.compose.runtime.Immutable
import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.Ohlc

@Immutable
data class StockDetails(
    val average_price: Double,
    val close_price: Double,
    val company_name: String,
    var day_change: Double,
    val day_change_percentage: Double,
    val exchange: String,
    val isin: String,
    val last_price: Double,
    val pnl: Double,
    val quantity: Int,
    val t1_quantity: Int,
    val trading_symbol: String,
    val invested_amount: Double,
    val current_amount: Double,
    var percentage_gain: Double,
    var total_gain: Double,
    var instrument_token : String,
    var ohlc: Ohlc? = null,
    var portfolio_share : Double,
    var marketCap : String? = null
)
