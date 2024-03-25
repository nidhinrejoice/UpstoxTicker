package com.nidhin.upstoxclient.feature_portfolio.data

import com.nidhin.upstoxclient.feature_portfolio.data.models.getprofitlossreport.Data

data class ScriptProfitLoss(
    val scriptName : String,
    val pnl : Double,
    val dataList : List<Data>
)
