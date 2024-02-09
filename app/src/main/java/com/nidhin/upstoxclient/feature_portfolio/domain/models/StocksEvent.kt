package com.nidhin.upstoxclient.feature_portfolio.domain.models
    sealed class StocksEvent {
        data class Order(var stockOrder: StockOrder) : StocksEvent()
        object ToggleOrderSection : StocksEvent()
    }