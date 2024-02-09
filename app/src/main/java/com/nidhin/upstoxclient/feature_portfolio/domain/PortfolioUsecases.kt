package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.models.longtermholdings.GetLongTermHoldingsResponse

data class PortfolioUsecases(
    val checkUserAuthentication: CheckUserAuthentication,
    val generateAccessToken: GenerateAccessToken,
    val getLongTermHoldings: GetLongTermHoldings,
    val sortHoldingsList: SortHoldingsList,
    val getMarketOHLC: GetMarketOHLC
)