package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.twoDecimalPlaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioAnalysis(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PortfolioViewModel
) {

    LaunchedEffect(key1 = false) {
        viewModel.getGeminiAnalysis()
    }
    Scaffold (
        topBar = {
            Row(modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
                .clickable {
                    navController.popBackStack()
                }) {
                Text(text = "< Portfolio Analysis", style = MaterialTheme.typography.titleLarge)
            }
        },
        content = { padding->
            Column {
                if (viewModel.state.value.newsLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = padding.calculateTopPadding()), contentAlignment = Alignment.Center) {
                        val marketMap = viewModel.state.value.stocks.groupBy { it.marketCap }
                        val largeCapTotal =
                            marketMap["Large"]?.map { it.quantity * it.last_price }
                                ?.reduce { acc, d -> acc + d } ?: 0.0
                        val mediumCapTotal =
                            marketMap["Mid"]?.map { it.quantity * it.last_price }
                                ?.reduce { acc, d -> acc + d } ?: 0.0
                        val smallCapTotal =
                            marketMap["Small"]?.map { it.quantity * it.last_price }
                                ?.reduce { acc, d -> acc + d } ?: 0.0
                        val totalInvestedAmount = (largeCapTotal.plus(mediumCapTotal)
                            .plus(smallCapTotal))
                        ElevatedCard(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.elevatedCardElevation()
                        ) {
                            Column {
                                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                                    Text(text = "Total : ${totalInvestedAmount.formatCurrency()}",
                                        style = MaterialTheme.typography.titleMedium, color = Green)
                                }
                                Divider()
                                Column (modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.End){
                                    Text(text = "Large Cap  : ${largeCapTotal.formatCurrency()} (${(largeCapTotal / totalInvestedAmount * 100).twoDecimalPlaces()}%)")
                                    Text(text = "Mid Cap  : ${mediumCapTotal.formatCurrency()} (${(mediumCapTotal / totalInvestedAmount * 100).twoDecimalPlaces()}%)")
                                    Text(text = "Small Cap  : ${smallCapTotal.formatCurrency()} (${(smallCapTotal / totalInvestedAmount * 100).twoDecimalPlaces()}%)")
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}