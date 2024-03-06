package com.nidhin.upstoxclient.feature_portfolio.domain.models

sealed class AiPrompt( var label: String, var prompt : String) {

    data object LatestNews : AiPrompt( "Latest News", "What is the latest news about ")
    data object RedFlags : AiPrompt( "Red Flags", "Are there any red flags for the stock of ")
    data object Financials : AiPrompt( "Financials", "Give a quick summary about the financials of the stock of ")
    data object Advantages : AiPrompt( "Advantages", "What are the advantages against the competitors for ")

//    fun copy(orderType: OrderType): AiPrompt {
//        return when (this) {
//            is InvestedAmt -> InvestedAmt(orderType)
//            is Pnl -> Pnl(orderType)
//            is Price -> Price(orderType)
//            is CurrentAmt -> CurrentAmt(orderType)
//            is DailyPerc -> DailyPerc(orderType)
//            is DailyPnl -> DailyPnl(orderType)
//            is Name -> Name(orderType)
//            is Perc -> Perc(orderType)
//        }
//    }
}