package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nidhin.upstoxclient.R
import com.nidhin.upstoxclient.feature_portfolio.domain.models.AiPrompt
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.twoDecimalPlaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockInfo(
    state: PortfolioViewModel.StockScreenState, viewModel: PortfolioViewModel
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
//            val isLatestNewsLoading by remember {
//                mutableStateOf(viewModel.isLatestNewsLoading)
//            }
//            val isMarketDataLoading by remember {
//                mutableStateOf(viewModel.isMarketDataLoading)
//            }
            if (viewModel.isMarketDataLoading.value) {
                Text(text = "Fetching stock OHLC...", style = MaterialTheme.typography.bodySmall)
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
                                .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween
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
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = it.company_name,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.size(10.dp))
                                    }

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
                                                start = Offset(size.width / 2, size.height / 2),
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
                                                start = Offset(size.width / 2, size.height / 2),
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
                var aiPromptSelected by remember {
                    mutableStateOf<AiPrompt?>(null)
                }
                val aiPrompts = listOf<AiPrompt>(
                    AiPrompt.Financials,
                    AiPrompt.LatestNews,
                    AiPrompt.Advantages,
                    AiPrompt.RedFlags
                )
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    verticalItemSpacing = 1.dp,
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
                                    Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                                        Text(text = aiPrompt.label)
                                    }
                                })
                        }
                    },
                    modifier = Modifier.fillMaxWidth()

                )
//                if (aiPromptHeader.isNotEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(12.dp), contentAlignment = Alignment.CenterStart
//                    ) {
//                        Text(text = aiPromptHeader, style = MaterialTheme.typography.titleLarge)
//                    }
//                }
                if (viewModel.isLatestNewsLoading.value) {
                    Loader()
                }
                state.aiContent?.let { news ->
                    val scrollState = rememberLazyListState()

                    LazyColumn(state = scrollState) {
                        item {


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
        Spacer(modifier = Modifier.size(80.dp))
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(80.dp)
        )

    }
}
