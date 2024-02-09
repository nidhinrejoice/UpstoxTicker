package com.nidhin.upstoxclient.utils

import java.text.DecimalFormat
import java.util.Currency

fun Float.twoDecimalPlaces(): Float {
    val df = DecimalFormat("#.#")
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = 0

    return df.format(this).toFloat()
}

fun Double.twoDecimalPlaces(): String {
    val df = DecimalFormat("#.#")
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = 0

    return df.format(this)
}

fun Double.formatCurrency(): String {
    val df = DecimalFormat("#.#")
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = 0

    val baseCurrency =  "₹"
    return baseCurrency + df.format(this)

}