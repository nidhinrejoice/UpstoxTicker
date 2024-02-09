package com.nidhin.upstoxclient.feature_portfolio.di

import com.nidhin.upstoxclient.feature_portfolio.data.PortfolioRepository
import com.nidhin.upstoxclient.feature_portfolio.domain.CheckUserAuthentication
import com.nidhin.upstoxclient.feature_portfolio.domain.GenerateAccessToken
import com.nidhin.upstoxclient.feature_portfolio.domain.GetLongTermHoldings
import com.nidhin.upstoxclient.feature_portfolio.domain.GetMarketOHLC
import com.nidhin.upstoxclient.feature_portfolio.domain.IPortfolioRepository
import com.nidhin.upstoxclient.feature_portfolio.domain.PortfolioUsecases
import com.nidhin.upstoxclient.feature_portfolio.domain.SortHoldingsList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.http.GET
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
open class PortfolioModule {

    @Singleton
    @Provides
    fun providePortfolioRepository(portfolioRepository: PortfolioRepository): IPortfolioRepository {
        return portfolioRepository
    }


    @Singleton
    @Provides
    open fun providePortfolioUsecases(portfolioRepository: IPortfolioRepository): PortfolioUsecases {
        return PortfolioUsecases(
            CheckUserAuthentication(portfolioRepository),
            GenerateAccessToken(portfolioRepository),
            GetLongTermHoldings(portfolioRepository),
            SortHoldingsList(),
            GetMarketOHLC(portfolioRepository)
        )
    }
}