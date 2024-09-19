package com.nidhin.upstoxclient.feature_portfolio.domain

import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow

interface IGeminiRepository {
    suspend fun getGeminiResponseStream(prompt: String): Flow<GenerateContentResponse>
    suspend fun getGeminiResponse(prompt: String): Flow<GenerateContentResponse>

}