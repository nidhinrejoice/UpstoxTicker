package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.nidhin.upstoxclient.BuildConfig
import com.nidhin.upstoxclient.R
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpstoxLogin(
    onAuthCodeReceived: (String) -> Unit
    ) {
        val url =
            "https://api.upstox.com/v2/login/authorization/dialog?" +
                    "client_id=${BuildConfig.CLIENT_ID}" +
                    "&redirect_uri=https://www.upstox.com" +
                    "&response_type=code"

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Upstox Login") }
                )
            }
        ) { _ ->
            Surface (modifier = Modifier.fillMaxSize()){
                UpstoxLoginWebView(authUrl = url, onAuthCodeReceived = onAuthCodeReceived)

            }
        }
//    }
}

@Composable
fun UpstoxLoginWebView(authUrl : String , onAuthCodeReceived : (String)-> Unit) {
    AndroidView(
        factory = { context ->
            // Inflate the WebView layout
            val view = LayoutInflater.from(context).inflate(R.layout.view_upstox_login, null, false)
            val webView = view.findViewById<WebView>(R.id.webView)

            // Configure WebView settings
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null && url.startsWith("https://api-v2.upstox.com/login/authorization/redirect")) {
                        val uri = Uri.parse(url)
                        val code = uri.getQueryParameter("code")
                        code?.let {
                            onAuthCodeReceived(code)
                        }

                        return true // Stop loading after capturing the auth code
                    }
                    return false
                }
            }

            // Load the Upstox login URL
            webView.loadUrl(authUrl)
            view

//            WebView(context).apply {
//                // Enable JavaScript (optional, depending on your requirements)
//                settings.javaScriptEnabled = true
//                settings.domStorageEnabled = true
//                webViewClient = object : WebViewClient() {
//                    override fun shouldOverrideUrlLoading(
//                        view: WebView?,
//                        request: WebResourceRequest?
//                    ): Boolean {
//                        val url = request?.url.toString()
//                        if (url.startsWith("https://api-v2.upstox.com/login/authorization/redirect")) {
//                            val uri = Uri.parse(url)
//                            val code = uri.getQueryParameter("code")
//                            code?.let {
//                                onAuthCodeReceived(code)
//                            }
//
//                            return true
//                        }
//                        return super.shouldOverrideUrlLoading(view, request)
//                    }
//                }
//
//                // Load the specified URL
//                loadUrl(authUrl)
//            }
        },
        modifier = Modifier.fillMaxSize()
    )
}