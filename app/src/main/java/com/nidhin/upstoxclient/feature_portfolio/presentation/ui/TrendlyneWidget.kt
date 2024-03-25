package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.ai.client.generativeai.type.content
import com.nidhin.upstoxclient.ui.theme.UpstoxClientTheme

@Composable
fun TrendlyneWidget(stock: String) {
    Surface {
        UpstoxClientTheme {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()

                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.setSupportZoom(true)
                    }
                },
                update = { webView ->
                    webView.loadUrl(
                        "https://trendlyne.com/web-widget/swot-widget/Poppins/$stock/?posCol=00A25B&primaryCol=006AFF&negCol=EB3B00&neuCol=F7941E"
                    )
                }
            )
        }
    }
}