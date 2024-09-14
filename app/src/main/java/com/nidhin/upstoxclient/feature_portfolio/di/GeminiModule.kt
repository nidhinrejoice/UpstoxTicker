package com.nidhin.upstoxclient.feature_portfolio.di

import com.nidhin.upstoxclient.feature_portfolio.data.GeminiRepository
import com.nidhin.upstoxclient.feature_portfolio.domain.GenerateGeminiResponse
import com.nidhin.upstoxclient.feature_portfolio.domain.IGeminiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
open class GeminiModule {

    @Singleton
    @Provides
    fun provideGeminiRepository(geminiRepository: GeminiRepository): IGeminiRepository {
        return geminiRepository
    }


    @Singleton
    @Provides
    open fun provideGenerateGeminiResponse(geminiRepository: IGeminiRepository): GenerateGeminiResponse {
        return  GenerateGeminiResponse(geminiRepository)
    }
}