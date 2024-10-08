package com.nidhin.upstoxclient.feature_portfolio.presentation

import android.os.Build
import android.os.ext.SdkExtensions
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.asTextOrNull
import com.nidhin.upstoxclient.feature_portfolio.data.ScriptProfitLoss
import com.nidhin.upstoxclient.feature_portfolio.data.models.newsapiresponse.Article
import com.nidhin.upstoxclient.feature_portfolio.domain.GenerateGeminiResponse
import com.nidhin.upstoxclient.feature_portfolio.domain.GetGeminiPortfolioAnalysis
import com.nidhin.upstoxclient.feature_portfolio.domain.PortfolioUsecases
import com.nidhin.upstoxclient.feature_portfolio.domain.models.Month
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StocksEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PortfolioViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    private val portfolioUsecases: PortfolioUsecases,
    private val generateGeminiResponse: GenerateGeminiResponse,
    private val getGeminiPortfolioAnalysis: GetGeminiPortfolioAnalysis
) : ViewModel() {

    var isRefreshing = mutableStateOf(false)
        private set
    var isMarketDataLoading = mutableStateOf(false)
        private set
    var isLatestNewsLoading = mutableStateOf(false)
        private set
    var articleCurrentPage = mutableIntStateOf(1)
        private set

    //    var profitLossShown = mutableStateOf(false)
//        private set
    private var _state = mutableStateOf(StockScreenState())
    val state: State<StockScreenState> = _state
    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 3)
    val eventFlow = _eventFlow.asSharedFlow()

    private val _getGeminiAnalysis = MutableSharedFlow<Unit>()

    init {
        viewModelScope.launch {
            _getGeminiAnalysis
                .debounce(500)
                .onEach {
                    getGeminiAnalysis()
                }
                .launchIn(this)
            if (portfolioUsecases.checkUserAuthentication()) {
                getUserHoldings()
            } else {
                _state.value = _state.value.copy(
                    userState = UserState.Expired
                )
                _eventFlow.emit(UiEvent.UpstoxLogin)
            }
        }
    }

    fun onGetGeminiAnalysis() {
        viewModelScope.launch {
            _getGeminiAnalysis.emit(Unit)
        }
    }

    fun generateAccessToken(code: String) {
        job =
            viewModelScope.launch {
                try {
                    if (portfolioUsecases.generateAccessToken(code)) {
                        _state.value = _state.value.copy(
                            userState = UserState.LoggedIn
                        )
                        getUserHoldings()
                    }
                } catch (ex: Exception) {
                    _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
                    _eventFlow.emit(UiEvent.UpstoxLogin)
                }

            }
    }

    var cancelJobs = {
        try {
            job?.cancel()
        } catch (_: Exception) {

        }
    }

    fun getUserHoldings() {
        job =
            viewModelScope.launch {
                isRefreshing.value = true
//            _eventFlow.emit(UiEvent.ShowPortfolio)
                try {

                    _state.value = _state.value.copy(
                        stocks = portfolioUsecases.getLongTermHoldings()
                    )
                    isRefreshing.value = false
                    _eventFlow.emit(UiEvent.ShowPortfolio)
                    sortHoldings(state.value.stockOrder)
                } catch (ex: Exception) {
                    isRefreshing.value = false
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
//                        Build.VERSION_CODES.S
//                    ) >= 7
//                ) {
                    if (ex is retrofit2.HttpException) {
                        _state.value = _state.value.copy(
                            userState = UserState.Expired
                        )
                        _eventFlow.emit(UiEvent.ShowToast("Login expired"))
                        _eventFlow.emit(UiEvent.UpstoxLogin)
                    } else {
                        _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
                    }

                }
            }
    }

    private var job: Job? = null
    fun getMarketOHLC(
        instrumentToken: String,
        symbol: String,
        exchange: String
    ) {
        cancelJobs()
        job = viewModelScope.launch {
            isMarketDataLoading.value = true
            try {
                portfolioUsecases.getMarketOHLC(instrumentToken, symbol, exchange).collectLatest { ohlc ->
                    _state.value = state.value.copy(
                        selectedStock = state.value.stocks.find { it.instrument_token == instrumentToken },
                        aiContent = ""
                    )
                    _state.value.selectedStock?.ohlc = ohlc
                    isMarketDataLoading.value = false
                }
            } catch (ex: Exception) {
                isMarketDataLoading.value = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S
                    ) >= 7
                ) {
                    if (ex is retrofit2.HttpException) {
                        _state.value = _state.value.copy(
                            userState = UserState.Expired
                        )
                        _eventFlow.emit(UiEvent.UpstoxLogin)
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

        cancelJobs()
        job = viewModelScope.launch {
            _state.value = state.value.copy(
                aiContent = ""
            )
            isLatestNewsLoading.value = true
            try {
                generateGeminiResponse(prompt).collect { contentRes ->
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


    private fun getGeminiAnalysis() {
        cancelJobs()
        job = viewModelScope.launch {
            try {
                _state.value = state.value.copy(
                    newsLoading = true
                )
                getGeminiPortfolioAnalysis(state.value.stocks).collect { contentRes ->
                    _state.value = state.value.copy(
                        stocks = contentRes,
                        newsLoading = false
                    )
                }
            } catch (ex: Exception) {
                _state.value = state.value.copy(
                    newsLoading = false
                )
                _eventFlow.emit(UiEvent.ShowToast("Something went wrong"))
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
        val stockOrder: StockOrder = StockOrder.DailyPnl(OrderType.Descending),
        val isOrderSectionVisible: Boolean = false,
        var showProfitLoss: Boolean = false,
        val aiContent: String? = null,
        val latestNews: MutableList<Article> = mutableListOf(),
        var newsLoading: Boolean = false,
        var userState: UserState = UserState.Anonymous,
        var financialYear: String = "2024-2025"
    )

    enum class UserState {
        LoggedIn, Expired, Anonymous
    }


    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()

        data object UpstoxLogin : UiEvent()
        data object ShowPortfolio : UiEvent()
        data object ProfitLoss : UiEvent()
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

            is StocksEvent.FilterMonth -> {
                viewModelScope.launch {
                    getProfitLoss(financialYear = state.value.financialYear, event.month)
                }
            }
        }
    }

    fun getProfitLoss(financialYear: String = state.value.financialYear, filterMonth: Month?) {
        cancelJobs()
        job =
            viewModelScope.launch {
                try {
                    state.value.financialYear = financialYear
                    _state.value.showProfitLoss = false
                    portfolioUsecases.getProfitLoss(financialYear, filterMonth).collectLatest {
                        _state.value = state.value.copy(
                            profitLoss = it,
                            showProfitLoss = true
                        )
                    }
                } catch (ex: Exception) {
                    _state.value = state.value.copy(
                        showProfitLoss = true
                    )
                    _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))

                }
            }
    }

    fun sortProfitLossReport(sortOrder: StockOrder) {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                profitLoss = portfolioUsecases.sortProfitLossReport(
                    sortOrder,
                    state.value.profitLoss
                )
            )
        }
    }

    private var totalArticles = 0

    fun getLatestNews(page: Int, key: String) {
        cancelJobs()
        job = viewModelScope.launch {
            if (totalArticles > 9 && totalArticles / 10 < page) {
                _eventFlow.emit(UiEvent.ShowToast("No more articles found"))
            } else {
                try {
                    articleCurrentPage.intValue = page
                    if (page == 1)
                        state.value.latestNews.clear()
                    _state.value = state.value.copy(
                        newsLoading = true
                    )
                    portfolioUsecases.getLatestNews(key, articleCurrentPage.intValue)
                        .collectLatest {
                            _state.value = state.value.copy(
                                newsLoading = false
                            )
                            totalArticles = it.totalResults
                            _state.value.latestNews.addAll(it.articles)
                        }
                } catch (ex: Exception) {
                    _eventFlow.emit(UiEvent.ShowToast(ex.message.toString()))
                    _state.value = state.value.copy(
                        newsLoading = false
                    )

                }
            }
        }
    }

    fun closeNewsDetails() {
        articleCurrentPage.intValue = 1
    }

    fun closeNewsListing() {
        _state.value = state.value.copy(
            latestNews = mutableListOf()
        )
        job?.cancel()
    }


}