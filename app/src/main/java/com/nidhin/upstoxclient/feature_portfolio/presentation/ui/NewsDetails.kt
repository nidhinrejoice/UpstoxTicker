package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.nidhin.upstoxclient.feature_portfolio.data.models.newsapiresponse.Article

@Composable
fun NewsDetails(
    navController: NavController,
    article: Article
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Go Back")
            }
            Text(text = "News - ${article.source?.name}", maxLines = 1)
        }

        val webViewState = rememberWebViewState(article.url)

        Box(modifier = Modifier.fillMaxSize()) {
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize()
            )

            if (webViewState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
//        AndroidView(
//            factory = { context ->
//                WebView(context).apply {
//                    settings.javaScriptEnabled = true
//                    webViewClient = WebViewClient()
//                    loadUrl(url)
//                }
//
//            }
//        )
    }
}