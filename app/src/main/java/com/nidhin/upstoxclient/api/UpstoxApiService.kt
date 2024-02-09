package com.nidhin.upstoxclient.api

import com.google.gson.JsonElement
import com.nidhin.upstoxclient.feature_portfolio.data.models.GenerateAccessTokenResponse
import com.nidhin.upstoxclient.feature_portfolio.data.models.longtermholdings.GetLongTermHoldingsResponse
import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.MarketOHLCResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UpstoxApiService {

    @FormUrlEncoded
    @POST("login/authorization/token")
    suspend fun generateAuthToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("grant_type") grant_type: String,
        @Field("code") code: String
    ): GenerateAccessTokenResponse

    @GET("portfolio/long-term-holdings")
    suspend fun getLongTermHoldings(
        @Header("Authorization") accessToken: String
    ): GetLongTermHoldingsResponse

    @GET("market-quote/ohlc")
    fun getMarketOHLC(
        @Header("Authorization") accessToken: String,
        @Query("instrument_key") symbol :String,@Query("interval") interval :String
    ): Call<ResponseBody>
}