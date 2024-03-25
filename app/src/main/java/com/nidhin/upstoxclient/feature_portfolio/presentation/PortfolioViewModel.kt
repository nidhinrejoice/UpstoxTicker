package com.nidhin.upstoxclient.feature_portfolio.presentation

import android.os.Build
import android.os.ext.SdkExtensions
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.asTextOrNull
import com.nidhin.upstoxclient.feature_portfolio.data.ScriptProfitLoss
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.PortfolioUsecases
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.SortOrder
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StocksEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val portfolioUsecases: PortfolioUsecases
) : ViewModel() {

    var isRefreshing = mutableStateOf(false)
        private set
    var isMarketDataLoading = mutableStateOf(false)
        private set
    var isLatestNewsLoading = mutableStateOf(false)
        private set

    //    var profitLossShown = mutableStateOf(false)
//        private set
    private var _state = mutableStateOf(StockScreenState())
    val state: State<StockScreenState> = _state
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

//    private var _stockList = mutableStateListOf<StockDetails>()
//    val stockList: List<StockDetails> = _stockList

    var showLoginPopup = mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            if (portfolioUsecases.checkUserAuthentication()) {
                getUserHoldings()
            } else {
                showLoginPopup.value = true
//                _eventFlow.emit(UiEvent.UpstoxLogin)
            }
        }
    }

    suspend fun generateAccessToken(code: String) {
        viewModelScope.launch {
            try {
                showLoginPopup.value = false
                if (portfolioUsecases.generateAccessToken(code)) {
                    getUserHoldings()
                }
            } catch (ex: Exception) {
                _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
            }

        }
    }

    fun getUserHoldings() {
        isRefreshing.value = true
        viewModelScope.launch {
//            _eventFlow.emit(UiEvent.ShowPortfolio)
            try {

                _state.value = state.value.copy(
                    stocks = portfolioUsecases.getLongTermHoldings(state.value.stockOrder),
                    stockOrder = state.value.stockOrder
                )
                isRefreshing.value = false
                _eventFlow.emit(UiEvent.ShowPortfolio)
            } catch (ex: Exception) {
                isRefreshing.value = false
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
//                        Build.VERSION_CODES.S
//                    ) >= 7
//                ) {
                if (ex is retrofit2.HttpException) {
                    showLoginPopup.value = true
                } else {
                    _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
                }
//                } else {
//                    _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
//                }

            }
        }
    }

    private var job: Job? = null
    fun getMarketOHLC(
        instrument_token: String,
        symbol: String,
        exchange: String,
        companyName: String
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            isMarketDataLoading.value = true
            try {
                portfolioUsecases.getMarketOHLC(instrument_token, symbol, exchange).collectLatest {
                    _state.value = state.value.copy(
                        selectedStock = state.value.stocks.find { it.instrument_token == instrument_token },
                        aiContent = ""
                    )
                    _state.value.selectedStock?.ohlc = it
                    isMarketDataLoading.value = false
                }
            } catch (ex: Exception) {
                isMarketDataLoading.value = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S
                    ) >= 7
                ) {
                    if (ex is retrofit2.HttpException) {
                        showLoginPopup.value = true
                    } else {
                        _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
                    }
                } else {
                    _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
                }

            }
        }
    }

    fun geminiPrompt(prompt: String) {
        job?.cancel()
        job = viewModelScope.launch {
            _state.value = state.value.copy(
                aiContent = ""
            )
            isLatestNewsLoading.value = true
            try {
                portfolioUsecases.getGeminiResponse(prompt).collect { contentRes ->
                    _state.value = state.value.copy(
                        aiContent = state.value.aiContent + contentRes.candidates[0].content.parts[0].asTextOrNull()
                    )
                    isLatestNewsLoading.value = false
                }
            } catch (ex: Exception) {
                isLatestNewsLoading.value = false
                _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
            }
        }
    }

    private fun sortHoldings(
        stockOrder: StockOrder = StockOrder.Name(OrderType.Ascending)
    ) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                stockOrder = stockOrder
            )
            _state.value = state.value.copy(
                stocks = portfolioUsecases.sortHoldingsList(stockOrder, state.value.stocks),
                stockOrder = stockOrder
            )
        }
    }


    data class StockScreenState(
        val stocks: List<StockDetails> = emptyList(),
        val profitLoss: List<ScriptProfitLoss> = emptyList(),
        val selectedStock: StockDetails? = null,
        val stockOrder: StockOrder = StockOrder.Name(OrderType.Ascending),
        val isOrderSectionVisible: Boolean = false,
        var showProfitLoss: Boolean = false,
        val aiContent: String? = null
    )


    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()

        //        object UpstoxLogin : UiEvent()
        object ShowPortfolio : UiEvent()
        object ProfitLoss : UiEvent()
//        object StockDetails : UiEvent()
    }


    fun onEvent(event: StocksEvent) {
        when (event) {

            is StocksEvent.Order -> {

                if (state.value.stockOrder::class == event.stockOrder::class
                    && state.value.stockOrder.orderType == event.stockOrder.orderType
                ) {
                    when (event.stockOrder.orderType) {
                        OrderType.Descending -> {
                            event.stockOrder = event.stockOrder.copy(
                                orderType = OrderType.Ascending
                            )
                        }

                        else -> {
                            event.stockOrder = event.stockOrder.copy(
                                orderType = OrderType.Descending
                            )
                        }
                    }
                }
                sortHoldings(event.stockOrder)

            }


            StocksEvent.ToggleOrderSection -> {

                _state.value =
                    state.value.copy(isOrderSectionVisible = !state.value.isOrderSectionVisible)
            }
        }
    }

    fun getProfitLoss(financialYear: String = "2324") {
        viewModelScope.launch {
//            _eventFlow.emit(UiEvent.ShowPortfolio)
            try {
                _state.value.showProfitLoss = false
                portfolioUsecases.getProfitLoss(financialYear).collectLatest {
                    _state.value = state.value.copy(
                        profitLoss = it,
                        showProfitLoss = true
                    )
                }
            } catch (ex: Exception) {
                isRefreshing.value = false
                _state.value = state.value.copy(
                    showProfitLoss = true
                )
                _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))

            }
        }
    }

    fun sortPnl(sortOrder: StockOrder) {
        if (sortOrder is StockOrder.Name) {
            if (sortOrder.orderType is OrderType.Descending)
                _state.value = state.value.copy(
                    profitLoss = state.value.profitLoss.sortedByDescending {
                        it.scriptName
                    }
                )
            else
                _state.value = state.value.copy(
                    profitLoss = state.value.profitLoss.sortedBy {
                        it.scriptName
                    }
                )
        } else {
            if (sortOrder.orderType is OrderType.Descending)
                _state.value = state.value.copy(
                    profitLoss = state.value.profitLoss.sortedByDescending {
                        it.pnl
                    }
                )
            else
                _state.value = state.value.copy(
                    profitLoss = state.value.profitLoss.sortedBy {
                        it.pnl
                    }
                )
        }
    }

}