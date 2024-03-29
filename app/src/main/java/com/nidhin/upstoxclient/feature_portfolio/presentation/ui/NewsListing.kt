package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nidhin.upstoxclient.R
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.feature_portfolio.presentation.util.Screen
import com.nidhin.upstoxclient.utils.convertIsoSecondFormatToDefaultDate
import kotlinx.coroutines.Dispatchers

@Composable
fun NewsListing(
    navController: NavController,
    viewModel: PortfolioViewModel,
    key: String
) {

    LaunchedEffect(key1 = false) {
        viewModel.getLatestNews(1, key)
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.closeNewsListing()
                navController.popBackStack()
            }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Go Back")
            }
            Text(text = "$key in the NEWS")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(viewModel.state.value.latestNews) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation()
                ) {
                    Column(modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navController.navigate(Screen.NewsListingDetails.route + "?url=${it.url}")
                        }) {
                        it.urlToImage?.apply {
                            val context = LocalContext.current
                            val imageUrl = it.urlToImage

                            // Build an ImageRequest with Coil
                            val listener = object : ImageRequest.Listener {
                                override fun onError(
                                    request: ImageRequest,
                                    result: ErrorResult
                                ) {
                                    super.onError(request, result)
                                }

                                override fun onSuccess(
                                    request: ImageRequest,
                                    result: SuccessResult
                                ) {
                                    super.onSuccess(request, result)
                                }
                            }
                            val imageRequest = ImageRequest.Builder(context)
                                .data(imageUrl)
                                .listener(listener)
//                                .scale(Scale.FILL)
                                .dispatcher(Dispatchers.IO)
                                .memoryCacheKey(imageUrl)
                                .diskCacheKey(imageUrl)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build()

                            // Load and display the image with AsyncImage
                            AsyncImage(
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                                model = imageRequest,
                                contentDescription = null,
                            )
                        }
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 2
                        )
                        Text(
                            text = it.description,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(4.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.source?.name ?: "",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = it.publishedAt.convertIsoSecondFormatToDefaultDate("dd MMM hh:mm a"),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            item {
                if (viewModel.state.value.newsLoading) {
//                        Text(
//                            text = "Fetching news..."
//                        )
                    NewsLoader()
                } else if (viewModel.state.value.latestNews.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .clickable {
                                viewModel.getLatestNews(
                                    viewModel.articleCurrentPage.intValue + 1,
                                    key
                                )
                            }, horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.size(70.dp))
                        Text(
                            text = "No Articles Found. Click to retry",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.size(70.dp))
                    }
                } else if (viewModel.state.value.latestNews.isNotEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(8.dp)
                            .clickable {
                                viewModel.getLatestNews(
                                    viewModel.articleCurrentPage.intValue + 1,
                                    key
                                )
                            }, contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Load more...")
                    }
                }
            }
        }
    }

}


@Composable
fun NewsLoader() {
    val isPlaying by remember {
        mutableStateOf(true)
    }
    val speed by remember {
        mutableStateOf(1f)
    }
    var repeat by remember {
        mutableStateOf(true)
    }
    val composition by rememberLottieComposition(

        LottieCompositionSpec
            .RawRes(R.raw.news_loading)

    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false

    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(80.dp)
        )

    }
}
