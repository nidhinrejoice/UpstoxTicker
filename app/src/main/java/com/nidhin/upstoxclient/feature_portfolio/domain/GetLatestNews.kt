package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.models.newsapiresponse.NewsApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestNews @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(key: String, page: Int): Flow<NewsApiResponse> {

        return portfolioRepository.getNews(key, page)
    }
}