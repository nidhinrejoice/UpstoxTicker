package com.nidhin.upstoxclient.feature_portfolio.domain

data class PortfolioUsecases(
    val checkUserAuthentication: CheckUserAuthentication,
    val generateAccessToken: GenerateAccessToken,
    val getLongTermHoldings: GetLongTermHoldings,
    val sortHoldingsList: SortHoldingsList,
    val getMarketOHLC: GetMarketOHLC,
    val getProfitLoss: GetProfitLoss,
    val getLatestNews: GetLatestNews,
    val sortProfitLossReport: SortProfitLossReport
)