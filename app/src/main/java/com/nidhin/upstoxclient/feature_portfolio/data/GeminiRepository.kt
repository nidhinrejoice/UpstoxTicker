package com.nidhin.upstoxclient.feature_portfolio.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.nidhin.upstoxclient.feature_portfolio.domain.IGeminiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class GeminiRepository @Inject constructor(
    @Named("GEMINI_API_KEY") val geminiKey: String) : IGeminiRepository {

    override suspend fun getGeminiResponseStream(prompt: String): Flow<GenerateContentResponse> {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = geminiKey
        )

        return generativeModel.generateContentStream(prompt)
    }
    override suspend fun getGeminiResponse(prompt: String): Flow<GenerateContentResponse> {
        return flow {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = geminiKey
            )

            emit(generativeModel.generateContent(prompt))
        }
    }

}