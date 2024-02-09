package com.nidhin.upstoxclient.feature_portfolio.domain

import com.nidhin.upstoxclient.feature_portfolio.data.PortfolioRepository
import javax.inject.Inject

class CheckUserAuthentication @Inject constructor(
    private val portfolioRepository: IPortfolioRepository
) {

    suspend operator fun invoke(): Boolean {
        return portfolioRepository.checkUserAuthenticated()
    }
}