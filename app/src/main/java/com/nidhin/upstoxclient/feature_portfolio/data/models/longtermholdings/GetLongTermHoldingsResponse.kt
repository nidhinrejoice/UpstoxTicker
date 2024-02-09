package com.nidhin.upstoxclient.feature_portfolio.data.models.longtermholdings

data class GetLongTermHoldingsResponse(
    val `data`: List<StockInfo>,
    val status: String
)