package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import GoogleSearchView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nidhin.upstoxclient.R
import com.nidhin.upstoxclient.feature_portfolio.domain.models.AiPrompt
import com.nidhin.upstoxclient.feature_portfolio.presentation.ListViewItem
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.feature_portfolio.presentation.util.Screen
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.twoDecimalPlaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockInfo(
    navController: NavController,
    state: PortfolioViewModel.StockScreenState, viewModel: PortfolioViewModel
) {
    val list: List<ListViewItem> = listOf(
        ListViewItem.TrendlyneSection,
        ListViewItem.StockDetailsSection,
        ListViewItem.AiPromptSection,
        ListViewItem.AiPromptDetails
    )
    var googled by remember {
        mutableStateOf(false)
    }
    Surface {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Go Back")
                    }
                    Text(text = "${state.selectedStock?.company_name}")
                }

            }
            items(list) {
                when (it) {
                    ListViewItem.StockDetailsSection -> {
                        if (viewModel.isMarketDataLoading.value) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = "Fetching stock OHLC...",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        } else {
                            state.selectedStock?.let {
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        it.ohlc?.let { ohlc ->
                                            val negativeDay = (it.last_price - ohlc.open < 0)
                                            val negativeGain = (it.total_gain < 0)
                                            val color = if (negativeDay) Color.Red else Green

                                            Column(modifier = Modifier.weight(0.9f)) {

                                                Row(
                                                    modifier = Modifier
                                                        .padding(4.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Start
                                                ) {
                                                    Text(
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = if (negativeGain) Color.Red else Green,
                                                        text = "${it.last_price.formatCurrency()} "
                                                    )
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = if (negativeGain) Color.Red else Green,
                                                        text = "(" + it.day_change_percentage.twoDecimalPlaces() + "%)"
                                                    )
                                                }

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp)
                                                ) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "${it.average_price.formatCurrency()} x ${it.quantity} Nos -> ${it.invested_amount.formatCurrency()}"
                                                    )

                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp)
                                                ) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "Current Value : ${it.current_amount.formatCurrency()}"
                                                    )

                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp)
                                                ) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "Portfolio Share : ${it.portfolio_share.twoDecimalPlaces()}%"
                                                    )

                                                }
                                                val gainString = if (negativeGain) "Loss of "
                                                else "Gain of "
                                                Text(
                                                    modifier = Modifier
                                                        .padding(4.dp),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = if (negativeGain) Color.Red else Green,
                                                    text = "$gainString${it.total_gain.formatCurrency()} (${it.percentage_gain.twoDecimalPlaces()}%)"
                                                )
                                                it.marketCap?.let { it1 ->
                                                    Text(
                                                        text = "$it1 Cap",
                                                        modifier = Modifier
                                                            .padding(4.dp),
                                                        style = MaterialTheme.typography.labelMedium
                                                    )
                                                }
                                            }
                                            Box(
                                                modifier = Modifier.weight(0.1f),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Canvas(
                                                    modifier = Modifier
                                                        .width(25.dp)
                                                        .height(80.dp)
                                                ) {
                                                    if (!negativeDay) {
                                                        val shadowLength = size.height / 2
                                                        val totalRange = ohlc.high - ohlc.low
                                                        val coreStartsAt =
                                                            shadowLength - (((ohlc.open - ohlc.low) / totalRange) * shadowLength)
                                                        val coreEndsAt =
                                                            shadowLength - (((ohlc.close - ohlc.low) / totalRange) * shadowLength)
                                                        drawLine(
                                                            color = color,
                                                            start = Offset(
                                                                size.width / 2,
                                                                size.height / 2
                                                            ),
                                                            end = Offset(
                                                                size.width / 2,
                                                                0f
                                                            ),
                                                            strokeWidth = 5f
                                                        )
                                                        drawLine(
                                                            color = color,
                                                            start = Offset(
                                                                size.width / 2,
                                                                coreStartsAt.toFloat()
                                                            ),
                                                            end = Offset(
                                                                size.width / 2,
                                                                coreEndsAt.toFloat()
                                                            ),
                                                            strokeWidth = 15f
                                                        )
                                                    } else {
                                                        val shadowLength = size.height / 2
                                                        val totalRange = ohlc.high - ohlc.low
                                                        val coreStartsAt =
                                                            shadowLength - (((ohlc.close - ohlc.low) / totalRange) * shadowLength)
                                                        val coreEndsAt =
                                                            shadowLength - (((ohlc.open - ohlc.low) / totalRange) * shadowLength)
                                                        drawLine(
                                                            color = color,
                                                            start = Offset(
                                                                size.width / 2,
                                                                size.height / 2
                                                            ),
                                                            end = Offset(
                                                                size.width / 2,
                                                                0f
                                                            ),
                                                            strokeWidth = 5f
                                                        )
                                                        drawLine(
                                                            color = color,
                                                            start = Offset(
                                                                size.width / 2,
                                                                coreStartsAt.toFloat()
                                                            ),
                                                            end = Offset(
                                                                size.width / 2,
                                                                coreEndsAt.toFloat()
                                                            ),
                                                            strokeWidth = 15f
                                                        )

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ListViewItem.TrendlyneSection -> {
                        state.selectedStock?.let { TrendlyneWidget(stock = it.trading_symbol) }
                    }

                    ListViewItem.AiPromptSection -> {
                        var aiPromptSelected by remember {
                            mutableStateOf<AiPrompt?>(null)
                        }
                        val aiPrompts = listOf<AiPrompt>(
                            AiPrompt.Financials,
                            AiPrompt.Advantages,
                            AiPrompt.RedFlags
                        )
                        LazyRow(
//                            columns = StaggeredGridCells.Fixed(3),
//                            verticalItemSpacing = 1.dp,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            content = {
                                items(aiPrompts) { aiPrompt ->
                                    ElevatedFilterChip(
                                        modifier = Modifier.padding(
                                            horizontal = 2.dp
                                        ),
                                        selected = aiPromptSelected?.label == aiPrompt.label,
                                        onClick = {
                                            state.selectedStock?.apply {
                                                aiPromptSelected = aiPrompt
                                                viewModel.geminiPrompt(aiPrompt.prompt + company_name)
                                            }
                                        },
                                        label = {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(text = aiPrompt.label)
                                            }
                                        })
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        var showKeySelection by remember {
                            mutableStateOf(false)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            ElevatedButton(onClick = {
                                showKeySelection = true
                            }) {
                                Text(text = "Latest News")
                            }
                            ElevatedButton(onClick = { googled = true }) {
                                Text(text = "Check Google")
                            }
                        }
                        if (showKeySelection) {
                            val keys = "${state.selectedStock?.company_name}".split(" ")
                            var optionFirst = keys[0]
                            var optionSecond = ""
                            var optionThird = ""
                            if (keys.size > 1)
                                optionFirst += " " + keys[1]
                            if (keys.size > 2) {
                                optionSecond += optionFirst + " " + keys[2]
                            }
                            if (keys.size > 3) {
                                keys.forEach { key ->
                                    if (optionThird.isNotEmpty())
                                        optionThird += " "
                                    optionThird += key
                                }
                            }
                            val keysList = mutableListOf(
                                "${state.selectedStock?.trading_symbol}",
                                optionFirst
                            )
                            if (optionSecond.isNotEmpty())
                                keysList.add(optionSecond)
                            if (optionThird.isNotEmpty())
                                keysList.add(optionThird)
                            ChooseStockKeyDialog(
                                onDismiss = {
                                    showKeySelection = false
                                },
                                keysList = keysList,
                                onKeySelected = { key ->
                                    showKeySelection = false
                                    navController.navigate(Screen.NewsListing.route + "?key=$key")
                                }
                            )
                        }
                        if (viewModel.isLatestNewsLoading.value) {
                            Loader()
                        }
                    }

                    ListViewItem.AiPromptDetails -> {
                        state.aiContent?.let { news ->
                            if (news.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    val text = news.split("**").filter { it.isNotEmpty() }
                                    Text(text = buildAnnotatedString {
                                        for (i in text.indices) {
                                            if (i % 2 == 0) {
                                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                    append(text[i])
                                                }
                                            } else {
                                                append(text[i])

                                            }
                                        }
                                    })
                                }
                            }

//                            val hasScrolledToEnd = !scrollState.isScrollInProgress
////                            if (hasScrolledToEnd) {
//                                LaunchedEffect(news) { // Launch effect on data change
//                                    // Scroll to bottom only if not already scrolled
//                                    if (!scrollState.isScrollInProgress) {
//                                        scrollState.animateScrollToItem(1)
//                                    }
//                                }
////                            }
                        }
                    }
                }
            }
//                if (aiPromptHeader.isNotEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(12.dp), contentAlignment = Alignment.CenterStart
//                    ) {
//                        Text(text = aiPromptHeader, style = MaterialTheme.typography.titleLarge)
//                    }
//                }
            item {
                if (googled) {
                    val splitStr = state.selectedStock?.company_name?.split(" ")
                    var symbol = splitStr?.get(0)
                    if ((splitStr?.size ?: 0) > 1) {
                        symbol += " ${splitStr?.get(1)}"
                    }
                    GoogleSearchView(query = "$symbol shares")
                }

            }
        }
    }
}

@Composable
fun Loader() {
    val isPlaying by remember {
        mutableStateOf(true)
    }
    val speed by remember {
        mutableStateOf(1f)
    }
    var repeat by remember {
        mutableStateOf(true)
    }
    val composition by rememberLottieComposition(

        LottieCompositionSpec
            .RawRes(R.raw.loading)

    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false

    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(80.dp)
        )

    }
}
