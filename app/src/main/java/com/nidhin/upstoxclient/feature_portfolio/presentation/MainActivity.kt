package com.nidhin.upstoxclient.feature_portfolio.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.NewsDetails
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.NewsListing
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.PortfolioAnalysis
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.PortfolioScreen
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.ProfitLossReport
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.StockAllocationsScreen
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.StockInfo
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.UpstoxLogin
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
            val navController = rememberNavController()
            var bottomBarState = rememberSaveable { (mutableStateOf(true)) }

            UpstoxClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(key1 = null) {
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.CREATED) {
                                viewModel.eventFlow.collect { event ->
                                    when (event) {
                                        is PortfolioViewModel.UiEvent.ShowToast -> {
                                            toast.showCustomToast(this@MainActivity, event.message)
                                        }

                                        is PortfolioViewModel.UiEvent.ShowPortfolio -> {

                                            bottomBarState.value = true
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
                                            ) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                navController = navController,
                                bottomBarState = bottomBarState
                            )
                        }, content = { padding ->
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Portfolio.route
                            ) {
                                composable(route = Screen.UpstoxLogin.route) {

                                    bottomBarState.value = false
                                    UpstoxLogin {
                                        viewModel.generateAccessToken(it)
                                    }
                                }
                                composable(route = Screen.Portfolio.route) {
                                    bottomBarState.value = true
                                    PortfolioScreen(
                                        navController = navController,
                                        viewModel,
                                        padding
                                    )
                                }
                                composable(route = Screen.StockAllocation.route) {

                                    bottomBarState.value = false
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
                                    val instrumentToken =
                                        it.arguments?.getString("instrument_token") ?: ""
                                    val symbol = it.arguments?.getString("symbol") ?: ""
                                    val exchange = it.arguments?.getString("exchange") ?: ""
                                    val companyName = it.arguments?.getString("company_name") ?: ""

                                    bottomBarState.value = false
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
                                composable(route = Screen.PortfolioAnalysis.route) {

                                    PortfolioAnalysis(
                                        navController = navController,
                                        viewModel = viewModel
                                    )
                                }
                                composable(
                                    route = Screen.NewsListing.route + "?key={key}",
                                    arguments = listOf(
                                        navArgument(name = "key") {
                                            type = NavType.StringType
                                            defaultValue = ""
                                        }
                                    )) {
                                    bottomBarState.value = false
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
                                    val article =
                                        viewModel.state.value.latestNews.find { it.url == url }
                                    article?.let {
                                        NewsDetails(
                                            navController = navController,
                                            article,
                                            viewModel
                                        )
                                    }
                                }
                            }
                        }
                    )

                }
            }
        }
    }
}

// Define Bottom Navigation Items
sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    data object Portfolio : BottomNavItem(Screen.Portfolio.route, Icons.Rounded.List, "Portfolio")
    data object Reports :
        BottomNavItem(Screen.ProfitLossReport.route, Icons.Rounded.Report, "Reports")

    data object Analysis :
        BottomNavItem(Screen.PortfolioAnalysis.route, Icons.Rounded.Analytics, "Analytics")
}

@Composable
fun BottomNavigationBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf(
        BottomNavItem.Portfolio,
        BottomNavItem.Reports,
//        BottomNavItem.Analysis
    )
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            if(item is BottomNavItem.Portfolio){
                                navController.navigate(
                                    item.route
                                ) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }else{
                                navController.navigate(item.route)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = Color.Gray
                        )
                    )
                }
            }
        })
}

sealed class ListViewItem {
    object StockDetailsSection : ListViewItem()
    object TrendlyneSection : ListViewItem()
    object AiPromptSection : ListViewItem()
    object AiPromptDetails : ListViewItem()
}