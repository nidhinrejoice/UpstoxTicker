package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import android.annotation.SuppressLint
import android.net.Uri
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
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpstoxLogin(
    onAuthCodeReceived: (String) -> Unit
    ) {
//    Dialog(
//        properties = DialogProperties(usePlatformDefaultWidth = false),
//        onDismissRequest = onDismiss
//    ) {
//        Surface(modifier = Modifier.fillMaxHeight()) {
//
//
//            // Use the AndroidView composable to embed the WebView
//
//        }
        val scope = rememberCoroutineScope()
        // Specify the URL you want to load in the WebView
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

//@Composable
//fun UpstoxLoginWebView(authUrl : String , onAuthCodeReceived : (String)-> Unit) {
//    AndroidView(
//        factory = { context ->
//            WebView(context).apply {
//                // Enable JavaScript (optional, depending on your requirements)
//                settings.javaScriptEnabled = true
//                settings.domStorageEnabled = true
//                textDirection = android.view.View.TEXT_DIRECTION_LTR
//                settings.defaultTextEncodingName = "utf-8"
////                        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
//
//                // Set a WebViewClient to handle redirects and other events
//                settings.loadWithOverviewMode = true
////                        settings.useWideViewPort = true
////                        settings.setSupportZoom(true)
//                webViewClient = object : WebViewClient() {
//                    override fun shouldOverrideUrlLoading(
//                        view: WebView?,
//                        request: WebResourceRequest?
//                    ): Boolean {
//                        val url = request?.url.toString()
////                            // Check if the URL contains the authorization callback URL
////                            // Check if the URL contains the authorization callback URL
//                        if (url.startsWith("https://api-v2.upstox.com/login/authorization/redirect")) {
//                            // Extract the code parameter from the URL
//                            val uri = Uri.parse(url)
//                            val code = uri.getQueryParameter("code")
//
//                            // Use the code to get the access token
//                            code?.let {
////                                scope.launch {
////                                    onCodeGenerated(code)
////                                }
////                                onDismiss()
//                                onAuthCodeReceived(code)
//                            }
//
//                            // Return true to indicate that the URL has been handled
//                            return true
//                        }
//                        return super.shouldOverrideUrlLoading(view, request)
//                    }
//                }
//
//                // Load the specified URL
//                loadUrl(authUrl)
//            }
//        },
//        modifier = Modifier.fillMaxSize()
//    )
//}