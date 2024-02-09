package com.nidhin.upstoxclient.feature_portfolio.domain.models

sealed class StockOrder(val orderType: OrderType, public var label: String) {

    class Name(orderType: OrderType) : StockOrder(orderType, "Name")
    class Price(orderType: OrderType) :StockOrder(orderType, "Price")
    class Perc(orderType: OrderType) : StockOrder(orderType, "Perc.")
    class DailyPerc(orderType: OrderType) : StockOrder(orderType, "DailyPerc.")
    class Pnl(orderType: OrderType) : StockOrder(orderType, "Pnl")
    class DailyPnl(orderType: OrderType) : StockOrder(orderType, "DailyPnl")
    class InvestedAmt(orderType: OrderType) : StockOrder(orderType, "Inv.")
    class CurrentAmt(orderType: OrderType) : StockOrder(orderType, "Curr.")

    fun copy(orderType: OrderType): StockOrder {
        return when (this) {
            is InvestedAmt -> InvestedAmt(orderType)
            is Pnl -> Pnl(orderType)
            is Price -> Price(orderType)
            is CurrentAmt -> CurrentAmt(orderType)
            is DailyPerc -> DailyPerc(orderType)
            is DailyPnl -> DailyPnl(orderType)
            is Name -> Name(orderType)
            is Perc -> Perc(orderType)
        }
    }
}