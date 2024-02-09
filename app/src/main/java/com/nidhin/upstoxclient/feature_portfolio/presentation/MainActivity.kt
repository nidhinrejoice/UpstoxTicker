package com.nidhin.upstoxclient.feature_portfolio.presentation

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.PortfolioScreen
import com.nidhin.upstoxclient.feature_portfolio.presentation.ui.StockInfo
import com.nidhin.upstoxclient.feature_portfolio.presentation.util.Screen
import com.nidhin.upstoxclient.ui.theme.UpstoxClientTheme
import com.nidhin.upstoxclient.utils.showCustomToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var toast: Toast
    private val viewModel: PortfolioViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toast = Toast(this)
        setContent {
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()
            var showLogin by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = false) {

                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is PortfolioViewModel.UiEvent.ShowToast -> {
                            toast.showCustomToast(this@MainActivity, event.message)
                        }

                        is PortfolioViewModel.UiEvent.ShowPortfolio -> {

                            navController.navigate(
                                Screen.Portfolio.route
                            )
                        }
                    }
                }
            }
            UpstoxClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var showLogin by remember { viewModel.showLoginPopup }
                    if (showLogin) {
                        UpstoxLoginDialog(onDismiss = {
                            showLogin = false
                        }, onCodeGenerated = {
                            lifecycleScope.launch {
                                viewModel.generateAccessToken(it)
                            }
                        })
                    }
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
                        composable(route = Screen.Portfolio.route) {

                            PortfolioScreen(navController = navController)
                        }
                        composable(
                            route = Screen.StockDetails.route + "?instrument_token={instrument_token}&symbol={symbol}&exchange={exchange}",
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
                                }
                            )
                        ) {
                            val instrument_token = it.arguments?.getString("instrument_token") ?: ""
                            val symbol = it.arguments?.getString("symbol") ?: ""
                            val exchange = it.arguments?.getString("exchange") ?: ""
                            StockInfo(
                                instrument_token, symbol, exchange
                            )
                        }
                    }
                }
            }
        }
    }

}