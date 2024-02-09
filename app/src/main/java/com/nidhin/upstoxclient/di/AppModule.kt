package com.nidhin.upstoxclient.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.nidhin.upstoxclient.api.ApiManager
import com.nidhin.upstoxclient.api.UpstoxApiService
import com.nidhin.upstoxclient.feature_portfolio.data.PortfolioRepository
import com.nidhin.upstoxclient.feature_portfolio.domain.CheckUserAuthentication
import com.nidhin.upstoxclient.feature_portfolio.domain.GenerateAccessToken
import com.nidhin.upstoxclient.feature_portfolio.domain.GetLongTermHoldings
import com.nidhin.upstoxclient.feature_portfolio.domain.IPortfolioRepository
import com.nidhin.upstoxclient.feature_portfolio.domain.PortfolioUsecases
import com.nidhin.upstoxclient.persistance.SharedPrefsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SharedPrefsHelper.PREF_NAME, Context.MODE_PRIVATE
        )
    }

    @Provides
    open fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
        return builder.build()
    }

    @Singleton
    @Provides
    open fun provideApi(retrofit: Retrofit): UpstoxApiService {
        return retrofit.create(UpstoxApiService::class.java)
    }

    @Singleton
    @Provides
    open fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Named("API_ENDPOINT")
    fun provideBaseUrl(): String {
        return "https://api.upstox.com/v2/"
    }

    @Provides
    @Named("CLIENT_ID")
    fun provideClientId(): String {
        return "15ff5c6b-7d2c-47f3-80c6-4753ef65fa0a"
    }

    @Provides
    @Named("CLIENT_SECRET")
    fun provideClientSecret(): String {
        return "kyj80oez7a"
    }

    @Singleton
    @Provides
    open fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named("API_ENDPOINT") API_ENDPOINT: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    open fun provideApiManager(
        apiService: UpstoxApiService,
        @Named("CLIENT_ID") clientId: String,
        @Named("CLIENT_SECRET") clientSecret: String
    ): ApiManager {
        return ApiManager(apiService, clientId, clientSecret)
    }


}