package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.feature_portfolio.presentation.util.Screen
import com.nidhin.upstoxclient.utils.convertIsoSecondFormatToDefaultDate
import kotlinx.coroutines.Dispatchers

@Composable
fun NewsListing(
    navController: NavController,
    viewModel: PortfolioViewModel
) {

    LaunchedEffect(key1 = false) {
        viewModel.state.value.selectedStock?.let {
            viewModel.getLatestNews(it.company_name, 1)
        }
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Go Back")
            }
            Text(text = viewModel.state.value.selectedStock?.company_name + " in the NEWS")
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
                                .dispatcher(Dispatchers.IO)
                                .memoryCacheKey(imageUrl)
                                .diskCacheKey(imageUrl)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build()

                            // Load and display the image with AsyncImage
                            AsyncImage(
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                        .clickable {
                            viewModel.state.value.selectedStock?.let {
                                viewModel.getLatestNews(
                                    it.company_name,
                                    viewModel.articleCurrentPage.value + 1
                                )
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    if (viewModel.state.value.newsLoading) {
                        Text(
                            text = "Fetching news..."
                        )
                    } else {
                        Text(text = "Load more...")
                    }
                }
            }
        }
    }

}