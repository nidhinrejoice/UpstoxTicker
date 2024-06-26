package com.nidhin.upstoxclient.utils

import androidx.compose.ui.graphics.Color
import com.nidhin.upstoxclient.ui.theme.Green
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

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

fun JSONObject.convertToReqBody(): RequestBody {
    return toString()
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

fun Date.getCurrentFinancialYear():String{
    var finYear = ""
    if(this.month>2)
        finYear = "${this.year+1900}-${this.year+1900+1}"
    else
        finYear = "${this.year+1900-1}-${this.year+1900}"
    return finYear
}

fun Double.getColor():Color{
    return if(this>0)
        Green
    else
        Color.Red
}
fun Date.formattedDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(this)
}
fun String.convertIsoSecondFormatToDefaultDate(format: String): String {
    return try {
        val isoFormatter = DateTimeFormatter.ISO_DATE_TIME

        // Define formatter for desired output format
        val outputFormatter = DateTimeFormatter.ofPattern(format)

        // Parse the ISO date string
        val parsedDate = LocalDateTime.parse(this, isoFormatter)

        // Format the parsed date in the desired output format
        return parsedDate.format(outputFormatter)
    } catch (e: Exception) {
        return ""
    }
}

fun Date.convertDate(format: String): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(this)
}
