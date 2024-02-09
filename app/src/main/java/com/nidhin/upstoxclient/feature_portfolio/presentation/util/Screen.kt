package com.nidhin.upstoxclient.feature_portfolio.presentation.util

sealed class Screen(val route: String) {

    object UpstoxLogin: Screen("upstox_login")
    object Portfolio : Screen("portfolio")
    object StockDetails : Screen("stock_details")

}