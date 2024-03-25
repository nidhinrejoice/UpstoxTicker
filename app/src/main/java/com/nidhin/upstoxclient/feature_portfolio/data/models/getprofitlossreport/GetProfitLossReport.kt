package com.nidhin.upstoxclient.feature_portfolio.data.models.getprofitlossreport

data class GetProfitLossReport(
    val `data`: List<Data>,
    val metadata: Metadata,
    val status: String
)