package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.utils.sortByOrder


class SortHoldingsList {
    interface SortingStrategy {
        fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails>


    }
    object PriceSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.last_price }
        }
    }

    object PnlSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.current_amount-it.invested_amount }
        }
    }

    object InvestedAmountSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.invested_amount }
        }
    }

    object CurrentAmountSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.current_amount }
        }
    }

    object DailyPercentSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.day_change_percentage }
        }
    }

    object DailyGainSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.day_change }
        }
    }

    object CompanyNameSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.company_name }
        }
    }

    object PercentGainSortingStrategy : SortingStrategy {
        override fun sort(stocks: List<StockDetails>, orderType: OrderType): List<StockDetails> {
            return stocks.sortByOrder(orderType) { it.percentage_gain }
        }
    }

    operator fun invoke(
        stockOrder: StockOrder,
        stocks: List<StockDetails>
    ): List<StockDetails> {

        val strategy =  when (stockOrder) {
            is StockOrder.Price -> PriceSortingStrategy
            is StockOrder.Pnl -> PnlSortingStrategy
            is StockOrder.InvestedAmt -> InvestedAmountSortingStrategy
            is StockOrder.CurrentAmt -> CurrentAmountSortingStrategy
            is StockOrder.DailyPerc -> DailyPercentSortingStrategy
            is StockOrder.DailyPnl -> DailyGainSortingStrategy
            is StockOrder.Name -> CompanyNameSortingStrategy
            is StockOrder.Perc -> PercentGainSortingStrategy

        }
        return strategy.sort(stocks,stockOrder.orderType)

    }
}