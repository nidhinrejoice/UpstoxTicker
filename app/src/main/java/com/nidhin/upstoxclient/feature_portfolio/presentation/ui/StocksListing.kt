package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockDetails
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.twoDecimalPlaces

@Composable
fun StocksListing(
    it: StockDetails,
    stockOrder: StockOrder,
    onStockClicked: (StockDetails) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onStockClicked(it) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.65f)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.company_name.uppercase(),
                        style = MaterialTheme.typography.labelLarge
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${it.last_price}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    val negativeDay =
                        it.day_change_percentage < 0

                    Text(
                        text = "  " + (it.day_change_percentage).twoDecimalPlaces() + "% (${it.day_change.formatCurrency()})",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (!negativeDay) Color(
                            0,
                            100,
                            100
                        ) else Color.Red
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(0.35f)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                if(stockOrder is StockOrder.InvestedAmt || stockOrder is StockOrder.CurrentAmt) {
                    Text(
                        text = "₹ ${it.current_amount.toInt()} (${it.portfolio_share.twoDecimalPlaces()}%)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }else{
                    Text(
                        text = "₹ ${it.current_amount.toInt()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row() {
                    Text(
                        text = (it.total_gain).twoDecimalPlaces() + " (" + (it.percentage_gain).twoDecimalPlaces() + "%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (it.percentage_gain > 0) Green else Color.Red
                    )
                }
            }
        }

    }
}