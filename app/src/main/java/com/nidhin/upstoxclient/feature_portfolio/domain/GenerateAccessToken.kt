package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.models.GenerateAccessTokenResponse
import javax.inject.Inject

class GenerateAccessToken @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(code:String): Boolean {
        return portfolioRepository.generateAccessToken(code)
    }
}