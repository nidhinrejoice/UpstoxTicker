package com.nidhin.upstoxclient.feature_portfolio.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.NewsDetails
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.NewsListing
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.PortfolioScreen
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.ProfitLossReport
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.StockAllocationsScreen
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.StockInfo
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.UpstoxLogin
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.UpstoxLoginDialog
import com.nidhin.upstoxclient.feature_portfolio.presentation.util.Screen
import com.nidhin.upstoxclient.ui.theme.UpstoxClientTheme
import com.nidhin.upstoxclient.utils.showCustomToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var toast: Toast
    private val viewModel: PortfolioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toast = Toast(this)
        setContent {
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()

            UpstoxClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(key1 = null) {
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED){
                                viewModel.eventFlow.collect { event ->
                                    when (event) {
                                        is PortfolioViewModel.UiEvent.ShowToast -> {
                                            toast.showCustomToast(this@MainActivity, event.message)
                                        }

                                        is PortfolioViewModel.UiEvent.ShowPortfolio -> {

                                            navController.navigate(
                                                Screen.Portfolio.route
                                            ) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }

                                        is PortfolioViewModel.UiEvent.ProfitLoss -> {

                                            navController.navigate(
                                                Screen.ProfitLossReport.route
                                            )
                                        }
                                        is PortfolioViewModel.UiEvent.UpstoxLogin -> {

                                            navController.navigate(
                                                Screen.UpstoxLogin.route
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
//                    val userState by remember { mutableStateOf( viewModel.state.value.userState) }
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Portfolio.route
                    ) {



//                        composable(route = Screen.UpstoxLogin.route) {
//                            WebViewScreen {
//                                scope.launch {
//                                    viewModel.generateAccessToken(it)
//                                }
//                            }
//                        }

                        composable(route = Screen.UpstoxLogin.route) {

                            UpstoxLogin {
                                viewModel.generateAccessToken(it)
                            }
                        }
                        composable(route = Screen.Portfolio.route) {

                            PortfolioScreen(navController = navController)
                        }
                        composable(route = Screen.StockAllocation.route) {

                            StockAllocationsScreen(navController = navController)
                        }
                        composable(
                            route = Screen.StockDetails.route + "?instrument_token={instrument_token}&symbol={symbol}&exchange={exchange}&company_name={company_name}",
                            arguments = listOf(
                                navArgument(name = "instrument_token") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(name = "exchange") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(name = "symbol") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(name = "company_name") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) {
                            val instrumentToken = it.arguments?.getString("instrument_token") ?: ""
                            val symbol = it.arguments?.getString("symbol") ?: ""
                            val exchange = it.arguments?.getString("exchange") ?: ""
                            val companyName = it.arguments?.getString("company_name") ?: ""

                            LaunchedEffect(key1 = false) {
                                viewModel.getMarketOHLC(
                                    instrumentToken,
                                    symbol,
                                    exchange
                                )
                            }
                            StockInfo(navController, viewModel.state.value, viewModel)
                        }
                        composable(route = Screen.ProfitLossReport.route) {

                            ProfitLossReport(navController = navController, viewModel)
                        }
                        composable(
                            route = Screen.NewsListing.route + "?key={key}",
                            arguments = listOf(
                                navArgument(name = "key") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )) {
                            val key = it.arguments?.getString("key") ?: ""
                            NewsListing(navController = navController, viewModel, key)
                        }
                        composable(
                            route = Screen.NewsListingDetails.route + "?url={url}",
                            arguments = listOf(
                                navArgument(name = "url") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) {
                            val url = it.arguments?.getString("url") ?: ""
                            val article = viewModel.state.value.latestNews.find { it.url == url }
                            article?.let {
                                NewsDetails(navController = navController, article, viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class ListViewItem {
    object StockDetailsSection : ListViewItem()
    object TrendlyneSection : ListViewItem()
    object AiPromptSection : ListViewItem()
    object AiPromptDetails : ListViewItem()
}