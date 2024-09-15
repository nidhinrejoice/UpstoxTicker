package com.nidhin.upstoxclient.feature_portfolio.domain.models

sealed class StocksEvent {
    data class Order(var stockOrder: StockOrder) : StocksEvent()
    object ToggleOrderSection : StocksEvent()
    data class FilterMonth(var month: Month) : StocksEvent()
}

enum class Month {
    Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec, Jan, Feb, Mar
}