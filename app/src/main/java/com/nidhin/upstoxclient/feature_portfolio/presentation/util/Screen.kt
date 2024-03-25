package com.nidhin.upstoxclient.feature_portfolio.presentation.util

sealed class Screen(val route: String) {

    data object UpstoxLogin: Screen("upstox_login")
    data object Portfolio : Screen("portfolio")
    data object StockDetails : Screen("stock_details")
    data object StockAllocation : Screen("allocation")
    data object ProfitLossReport : Screen("profit_loss_report")
    data object NewsListing : Screen("news_listing")
    data object NewsListingDetails : Screen("news_details_listing")

}