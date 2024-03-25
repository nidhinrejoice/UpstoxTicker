package com.nidhin.upstoxclient.api

import com.nidhin.upstoxclient.feature_portfolio.data.models.newsapiresponse.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {


    @GET("everything")
    suspend fun newsApi(
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortBy") sortBy: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): NewsApiResponse

}