package com.nidhin.upstoxclient.feature_portfolio.presentation

import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch

@Composable
fun UpstoxLoginDialog(
    onDismiss: () -> Unit,
    onCodeGenerated: (String) -> Unit
    ) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = true),
        onDismissRequest = onDismiss
    ) {
        Surface(modifier = Modifier.fillMaxHeight(0.85f)) {

            val scope = rememberCoroutineScope()
            // Specify the URL you want to load in the WebView
            val url =
                "https://api.upstox.com/v2/login/authorization/dialog?client_id=15ff5c6b-7d2c-47f3-80c6-4753ef65fa0a&redirect_uri=https://www.upstox.com&response_type=code"

            // Use the AndroidView composable to embed the WebView
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        // Enable JavaScript (optional, depending on your requirements)
                        settings.javaScriptEnabled = true
//                        settings.pluginState = WebSettings.PluginState.ON
//                        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
//                        // Enable handling of redirects within the WebView
//                        settings.javaScriptCanOpenWindowsAutomatically = true
//                        settings.loadsImagesAutomatically = true
                        settings.domStorageEnabled = true
                        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

                        // Set a WebViewClient to handle redirects and other events
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val url = request?.url.toString()
//                            // Check if the URL contains the authorization callback URL
//                            // Check if the URL contains the authorization callback URL
                                if (url.startsWith("https://api-v2.upstox.com/login/authorization/redirect")) {
                                    // Extract the code parameter from the URL
                                    val uri = Uri.parse(url)
                                    val code = uri.getQueryParameter("code")

                                    // Use the code to get the access token
                                    code?.let {
                                        scope.launch {
                                            onCodeGenerated(code)
                                        }
                                        onDismiss()
                                    }

                                    // Return true to indicate that the URL has been handled
                                    return true
                                }
//                            view?.loadUrl(url);

                                return false
                            }
                        }

                        // Load the specified URL
                        loadUrl(url)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}