package com.nidhin.upstoxclient.di

import android.content.Context
import android.content.SharedPreferences
import com.nidhin.upstoxclient.BuildConfig
import com.nidhin.upstoxclient.api.ApiManager
import com.nidhin.upstoxclient.api.NewsApiService
import com.nidhin.upstoxclient.api.UpstoxApiService
import com.nidhin.upstoxclient.persistance.SharedPrefsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
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
    open fun provideUpstoxApi(retrofit: Retrofit): UpstoxApiService {
        return retrofit.create(UpstoxApiService::class.java)
    }

    @Singleton
    @Provides
    open fun provideNewsApi(
        context: Context,
        @Named("NEWS_API_ENDPOINT") NEWS_API_ENDPOINT: String
    ): NewsApiService {
        val onlineInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge =
                3600000 // read from cache for 1 hour even if there is internet connection
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val cache = Cache(context.cacheDir, cacheSize)
        val okHttpClient = OkHttpClient().newBuilder()
            .addNetworkInterceptor(onlineInterceptor)
            .cache(cache)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(NEWS_API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsApiService::class.java)
    }
//    @Singleton
//    @Provides
//    open fun provideGeminiApi(@ApplicationContext context: Context): GeminiApiService {
//        val onlineInterceptor = Interceptor { chain ->
//            val response = chain.proceed(chain.request())
//            val maxAge = 3600000 // read from cache for 300 seconds even if there is internet connection
//            response.newBuilder()
//                .header("Cache-Control", "public, max-age=$maxAge")
//                .removeHeader("Pragma")
//                .build()
//        }
//        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
//        val cache = Cache(context.cacheDir, cacheSize)
//        val okHttpClient = OkHttpClient().newBuilder()
//            .addNetworkInterceptor(onlineInterceptor)
//            .cache(cache)
//            .connectTimeout(40, TimeUnit.SECONDS)
//            .readTimeout(40, TimeUnit.SECONDS).build()
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        return retrofit.create(GeminiApiService::class.java)
//    }

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
    @Named("NEWS_API_ENDPOINT")
    fun provideNewsBaseUrl(): String {
        return "https://newsapi.org/v2/"
    }

    @Provides
    @Named("CLIENT_ID")
    fun provideClientId(): String {
        return BuildConfig.clientId
    }

    @Provides
    @Named("CLIENT_SECRET")
    fun provideClientSecret(): String {
        return BuildConfig.clientSecret
    }

    @Provides
    @Named("GEMINI_API_KEY")
    fun provideGeminiKey(): String {
        return BuildConfig.geminiApiKey
    }

    @Provides
    @Named("NEWS_API_KEY")
    fun provideNewsApiKey(): String {
        return BuildConfig.newsApiKey
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
        newsApiService: NewsApiService,
        @Named("NEWS_API_KEY") newsApiKey: String,
        @Named("CLIENT_ID") clientId: String,
        @Named("CLIENT_SECRET") clientSecret: String
    ): ApiManager {
        return ApiManager(apiService, newsApiService, newsApiKey, clientId, clientSecret)
    }


}