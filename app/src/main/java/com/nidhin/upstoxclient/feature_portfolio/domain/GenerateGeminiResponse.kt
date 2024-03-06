package com.nidhin.upstoxclient.feature_portfolio.domain

import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateGeminiResponse @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(prompt: String): Flow<GenerateContentResponse> {
        return portfolioRepository.getLatestNewsFromGemini(prompt)
    }
}