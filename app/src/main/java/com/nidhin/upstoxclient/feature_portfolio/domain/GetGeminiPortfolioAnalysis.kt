package com.nidhin.upstoxclient.feature_portfolio.domain

import com.google.ai.client.generativeai.type.asTextOrNull
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import org.json.JSONArray
import javax.inject.Inject

class GetGeminiPortfolioAnalysis @Inject constructor(
    private val geminiRepository: IGeminiRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(list : List<StockDetails>): Flow<List<StockDetails>> {
        if(list.find { it.marketCap!=null } != null){
            return flowOf(list)
        }
        val stringBuilder = StringBuilder().apply {
            list.forEach{
                if(this.isNotEmpty())
                    append(",")
                append(it.company_name)
            }
            append("\n")
            append("from the above list, identify the stocks cap (large/mid/small). present it in a json form\n" +
                    "for eg: [{\"company\":\"HDFC BANK\", \"market_cap\":\"Large\"}]\n" +
                    "\n" +
                    "the response should only contain the json")
        }

        return geminiRepository.getGeminiResponse(stringBuilder.toString())
            .flatMapConcat { res->

                val aiContent = res.candidates[0].content.parts[0].asTextOrNull()
                val jsonArray = JSONArray(aiContent)
                for(i in 0 until jsonArray.length()){
                    val json =jsonArray.getJSONObject(i)
                    val stockDetails = list.find { it.company_name == json.getString("company") }
                    stockDetails?.marketCap = json.getString("market_cap")
                }
                flowOf(list)
            }
    }
}