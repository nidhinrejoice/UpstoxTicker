package com.nidhin.upstoxclient.feature_portfolio.domain.models

    sealed class OrderType {

        object Ascending : OrderType()
        object Descending : OrderType()

    }