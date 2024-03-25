package com.nidhin.upstoxclient.api

import com.google.gson.JsonElement
import com.nidhin.upstoxclient.feature_portfolio.data.models.GenerateAccessTokenResponse
import com.nidhin.upstoxclient.feature_portfolio.data.models.getmetadataprofitloss.GetTradeMetaData
import com.nidhin.upstoxclient.feature_portfolio.data.models.getprofitlossreport.GetProfitLossReport
import com.nidhin.upstoxclient.feature_portfolio.data.models.longtermholdings.GetLongTermHoldingsResponse
import com.nidhin.upstoxclient.feature_portfolio.data.models.marketohlc.MarketOHLCResponse
import com.nidhin.upstoxclient.feature_portfolio.domain.GenerateAccessToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class ApiManager @Inject constructor(
    private val apiService: UpstoxApiService,
    private val clientId: String,
    private val clientSecret: String
) {

    suspend fun generateAuthToken(code: String): GenerateAccessTokenResponse {
        return apiService.generateAuthToken(
            clientId,
            clientSecret,
            "https://www.upstox.com",
            "authorization_code",
            code
        )
    }

    suspend fun getLongTermHoldings(accessToken: String): GetLongTermHoldingsResponse {
        return apiService.getLongTermHoldings("Bearer $accessToken")
    }

    suspend fun getMarketOHLC(accessToken: String, instrumentToken: String): Flow<JSONObject> {

        return callbackFlow {

            val callback = object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    launch {
                        if (response.isSuccessful)
                            send(JSONObject(response.body()?.string().toString()))
                        else {
                            throw Exception(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            }
            apiService.getMarketOHLC("Bearer $accessToken", instrumentToken, "1d")
                .enqueue(callback)
            awaitClose()
        }.distinctUntilChanged()
    }

    suspend fun getProfitLoss(
        accessToken: String,
        financialYear: String,
        pageSize: Int
    ): GetProfitLossReport {
        return apiService.getProfitLoss(
            "Bearer $accessToken",
            segment = "EQ",
            financialYear,
            page_number = 1,
            page_size = pageSize
        )
    }
    suspend fun getTradeMetaData(
        accessToken: String,
        financialYear: String
    ): GetTradeMetaData {
        return apiService.getTradeMetaData(
            "Bearer $accessToken",
            segment = "EQ",
            financialYear
        )
    }
}