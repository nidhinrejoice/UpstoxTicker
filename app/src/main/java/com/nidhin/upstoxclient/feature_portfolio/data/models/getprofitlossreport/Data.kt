package com.nidhin.upstoxclient.feature_portfolio.data.models.getprofitlossreport

data class Data(
    val buy_amount: Double,
    val buy_average: Double,
    val buy_date: String,
    val isin: String,
    val quantity: Double,
    val scrip_name: String,
    val sell_amount: Double,
    val sell_average: Double,
    val sell_date: String,
    val trade_type: String
)