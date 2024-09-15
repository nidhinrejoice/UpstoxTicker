package com.nidhin.upstoxclient.feature_portfolio.domain

import com.google.common.truth.Truth
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import org.junit.Before
import org.junit.Test
import java.util.UUID

class PriceSortingStrategyTest{

    private lateinit var stocks : MutableList<StockDetails>
    @Before
    fun setUp(){
        stocks = mutableListOf()
        val stock1 = StockDetails(
            average_price = 1555.0,
            close_price = 100.0,
            company_name = "HDFC BANK",
            day_change = 1.12,
            day_change_percentage = 1.0,
            exchange = "NSE",
            isin = UUID.randomUUID().toString(),
            last_price = 1445.0,
            pnl = -3000.0,
            quantity = 40,
            t1_quantity = 0,
            trading_symbol = "HDFCBANK",
            invested_amount = 1555.0*40.0,
            percentage_gain = -4.0,
            current_amount = 1455.0*40,
            total_gain = -4000.0,
            instrument_token = UUID.randomUUID().toString(),
            portfolio_share = 10.0
        )
        val stock2 = StockDetails(
            average_price = 560.0,
            close_price = 660.0,
            company_name = "SBI",
            day_change = 1.1,
            day_change_percentage = 1.10,
            exchange = "NSE",
            isin = UUID.randomUUID().toString(),
            last_price = 660.0,
            pnl = 1200.0,
            quantity = 12,
            t1_quantity = 0,
            trading_symbol = "SBI BANK",
            invested_amount = 6000.0,
            percentage_gain =10.0,
            current_amount = 7000.0,
            total_gain = 1200.0,
            instrument_token = UUID.randomUUID().toString(),
            portfolio_share = 3.0
        )
        stocks.add(stock1)
        stocks.add(stock2)
        stocks.shuffle()
    }


    @Test
    fun testPricingSortStrategy(){
        val list = PriceSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].last_price).isGreaterThan(list[i+1].last_price)
        }
    }
    @Test
    fun testPnlSortStrategy(){
        val list = PnlSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].total_gain).isGreaterThan(list[i+1].total_gain)
        }
    }
    @Test
    fun testCurrentAmountSortStrategy(){
        val list = CurrentAmountSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].current_amount).isGreaterThan(list[i+1].current_amount)
        }
    }
    @Test
    fun testInvestedAmountSortStrategy(){
        val list = InvestedAmountSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].invested_amount).isGreaterThan(list[i+1].invested_amount)
        }
    }
    @Test
    fun testDailyGainSortStrategy(){
        val list = DailyGainSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].day_change).isGreaterThan(list[i+1].day_change)
        }
    }
    @Test
    fun testDailyPercGainSortStrategy(){
        val list = DailyPercentSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].day_change_percentage).isGreaterThan(list[i+1].day_change_percentage)
        }
    }
    @Test
    fun testPercGainSortStrategy(){
        val list = PercentGainSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].percentage_gain).isGreaterThan(list[i+1].percentage_gain)
        }
    }
    @Test
    fun testCompanySortStrategy(){
        val list = CompanyNameSortingStrategy.sort(stocks,OrderType.Descending)
        for(i in 0.. list.size - 2){
            Truth.assertThat(list[i].company_name).isGreaterThan(list[i+1].company_name)
        }
    }

}