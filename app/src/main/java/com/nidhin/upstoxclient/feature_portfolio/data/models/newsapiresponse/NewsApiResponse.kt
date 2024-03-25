package com.nidhin.upstoxclient.feature_portfolio.data.models.newsapiresponse

data class NewsApiResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)