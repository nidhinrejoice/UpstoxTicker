package com.nidhin.upstoxclient.feature_portfolio.data.models.longtermholdings

data class StockInfo(
    val average_price: Double,
    val close_price: Double,
    val cnc_used_quantity: Int,
    val collateral_quantity: Int,
    val collateral_type: String,
    val collateral_update_quantity: Int,
    val company_name: String,
    val day_change: Double,
    val day_change_percentage: Double,
    val exchange: String,
    val haircut: Double,
    val instrument_token: String,
    val isin: String,
    val last_price: Double,
    val pnl: Double,
    val product: String,
    val quantity: Int,
    val t1_quantity: Int,
    val trading_symbol: String,
    val tradingsymbol: String
)