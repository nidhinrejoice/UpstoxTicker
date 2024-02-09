package com.nidhin.upstoxclient.feature_portfolio.domain

import com.google.common.truth.Truth.assertThat
import com.nidhin.upstoxclient.feature_portfolio.data.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.UUID

class SortHoldingsListTest{


    private lateinit var sortHoldingsList: SortHoldingsList
    private lateinit var stocks : MutableList<StockDetails>

    @Before
    fun setUp(){
        sortHoldingsList=  SortHoldingsList()
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
    fun `order stocks by name`()= runBlocking{
        val list = sortHoldingsList(StockOrder.Name(OrderType.Ascending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].company_name).isLessThan(list[i+1].company_name)
        }
    }

    @Test
    fun `order stocks by name descending`()= runBlocking{
        val list = sortHoldingsList(StockOrder.Name(OrderType.Descending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].company_name).isGreaterThan(list[i+1].company_name)
        }
    }

    @Test
    fun `order stocks by gain descending`()= runBlocking{
        val list = sortHoldingsList(StockOrder.Pnl(OrderType.Descending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].total_gain).isGreaterThan(list[i+1].total_gain)
        }
    }

    @Test
    fun `order stocks by gain asc`()= runBlocking{
        val list = sortHoldingsList(StockOrder.Pnl(OrderType.Ascending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].total_gain).isLessThan(list[i+1].total_gain)
        }
    }

    @Test
    fun `order stocks by investment asc`()= runBlocking{
        val list = sortHoldingsList(StockOrder.InvestedAmt(OrderType.Ascending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].invested_amount).isLessThan(list[i+1].invested_amount)
        }
    }

    @Test
    fun `order stocks by investment desc`()= runBlocking{
        val list = sortHoldingsList(StockOrder.InvestedAmt(OrderType.Descending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].invested_amount).isGreaterThan(list[i+1].invested_amount)
        }
    }

    @Test
    fun `order stocks by dailyPerc asc`()= runBlocking{
        val list = sortHoldingsList(StockOrder.DailyPerc(OrderType.Ascending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].day_change_percentage).isLessThan(list[i+1].day_change_percentage)
        }
    }
    @Test
    fun `order stocks by dailyPerc desc`()= runBlocking{
        val list = sortHoldingsList(StockOrder.DailyPerc(OrderType.Descending),stocks)
        for(i in 0.. list.size - 2){
            assertThat(list[i].day_change_percentage).isGreaterThan(list[i+1].day_change_percentage)
        }
    }

}