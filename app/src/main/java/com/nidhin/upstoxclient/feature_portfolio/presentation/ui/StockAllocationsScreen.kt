package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.SortOrder
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StocksEvent
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.feature_portfolio.presentation.util.Screen
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.twoDecimalPlaces

@Composable
fun StockAllocationsScreen(
    navController: NavController,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val colorList = listOf<Color>(Color.Cyan, Color.Red, Color.Green, Color.DarkGray, Color.White)
    if (state.stocks.isNotEmpty()) {

        val currentAmount =
            state.stocks.map { it.quantity * it.last_price }
                .reduce { acc, i -> acc + i }
        val investedAmount =
            state.stocks.map { it.quantity * it.average_price }
                .reduce { acc, i -> acc + i }


//        val sweepAngle =
//            ((investedAmount - item.invested_amount) / investedAmount) * 360f
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            drawArc(
                color = Color(
                    ColorUtils.blendARGB(
                        android.graphics.Color.GREEN,
                        android.graphics.Color.BLACK,
                        0.6f
                    )
                ),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = true,
                size = Size(560f, 560f)
            )
            var startPie = 0f
            state.stocks.sortedByDescending { it.invested_amount }.mapIndexed { index, item ->
                if (startPie > 360)
                    startPie = 0f
                val sweepAngleTwo =
                    ((item.invested_amount) / investedAmount) * 360f
                val pieColor = colorList[index % 5]
                drawArc(
                    color = Color(
                        ColorUtils.blendARGB(
                            android.graphics.Color.GREEN,
                            android.graphics.Color.BLACK,
                            1 / (index + 2f).toFloat()
                        )
                    ),
                    startAngle = startPie,
                    sweepAngle = sweepAngleTwo.toFloat(),
                    useCenter = true,
                    size = Size(560f, 560f)
                )
                startPie += sweepAngleTwo.toFloat()
            }
        }
    }
}