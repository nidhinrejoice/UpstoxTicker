package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter

@Composable
fun NetworkImage(imageUrl: String) {
  val painter = rememberImagePainter(
      data = imageUrl,
      // Add placeholder, error handling and other options here (optional)
  )
  Box(modifier = Modifier.fillMaxSize()) {
    Image(painter = painter, contentDescription = null)
  }
}