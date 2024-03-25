import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.nidhin.upstoxclient.ui.theme.UpstoxClientTheme

@Composable
fun GoogleSearchView(query: String) {
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
                        "https://cse.google.com/cse?cx=8219b4368611741f7&q=$query"
                    )
                }
            )
        }
    }
}