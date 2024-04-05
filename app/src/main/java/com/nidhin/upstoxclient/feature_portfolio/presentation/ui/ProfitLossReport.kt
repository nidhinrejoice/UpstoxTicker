package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nidhin.upstoxclient.feature_portfolio.domain.models.OrderType
import com.nidhin.upstoxclient.feature_portfolio.domain.models.StockOrder
import com.nidhin.upstoxclient.feature_portfolio.presentation.PortfolioViewModel
import com.nidhin.upstoxclient.ui.theme.Green
import com.nidhin.upstoxclient.utils.formatCurrency
import com.nidhin.upstoxclient.utils.getColor
import com.nidhin.upstoxclient.utils.getCurrentFinancialYear
import java.util.Calendar

@Composable
fun ProfitLossReport(
    navController: NavController,
    viewModel: PortfolioViewModel
) {
    val date = Calendar.getInstance().time
    var selectedFinancialYear by remember {
        mutableStateOf(date.getCurrentFinancialYear())
    }

    var expandedStock by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = false) {
        viewModel.getProfitLoss(selectedFinancialYear)
    }
    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Go Back")
            }
            Text(text = "PnL Report")
        }
        if (!viewModel.state.value.showProfitLoss) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Fetching profit loss report...",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            var orderType: OrderType by remember {
                mutableStateOf(OrderType.Ascending)
            }

            var sortOrder: StockOrder by remember {
                mutableStateOf(StockOrder.Name(orderType))
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    ExpandedDropDown(
                        items = listOf("2024-2025","2023-2024", "2022-2023", "2021-2022", "2020-2021"),
                        selectedItem = selectedFinancialYear, label = "Financial Year:"
                    ) {
                        selectedFinancialYear = it
                        viewModel.getProfitLoss(selectedFinancialYear)
                    }
                }
                item {
                    ElevatedCard(elevation = CardDefaults.elevatedCardElevation()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Total PNL : ", style = MaterialTheme.typography.labelLarge)
                            if (viewModel.state.value.profitLoss.isNotEmpty()) {
                                Text(
                                    text = viewModel.state.value.profitLoss.map { it.pnl }
                                        .reduce { acc, d -> acc + d }.formatCurrency(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Green
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.secondaryContainer)
                            .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val arrowStockOrder =
                            if (sortOrder is StockOrder.Name) {
                                if (orderType is OrderType.Ascending) "\u2191" else "\u2193"
                            } else {
                                ""
                            }
                        val arrowPnlOrder =
                            if (sortOrder is StockOrder.Pnl) {
                                if (orderType is OrderType.Ascending) "\u2191" else "\u2193"
                            } else {
                                ""
                            }
                        Text(text = "Stock$arrowStockOrder", modifier = Modifier.clickable {
                            orderType = if (orderType is OrderType.Descending)
                                OrderType.Ascending
                            else
                                OrderType.Descending
                            sortOrder = StockOrder.Name(orderType)
                            viewModel.sortPnl(sortOrder)
                        })
                        Text(text = "PnL$arrowPnlOrder", modifier = Modifier.clickable {
                            orderType = if (orderType is OrderType.Descending)
                                OrderType.Ascending
                            else
                                OrderType.Descending
                            sortOrder = StockOrder.Pnl(orderType)
                            viewModel.sortPnl(sortOrder)
                        })
                    }
                }
                items(viewModel.state.value.profitLoss) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedStock =
                                    if (expandedStock.isNotEmpty() && expandedStock == it.scriptName)
                                        ""
                                    else
                                        it.scriptName
                            }
                            .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (it.scriptName.isNotEmpty()) {
                            Text(text = it.scriptName, style = MaterialTheme.typography.labelMedium)
                        } else {
                            Text(text = "N/A", style = MaterialTheme.typography.labelMedium)

                        }
                        Text(
                            text = it.pnl.formatCurrency(),
                            color = it.pnl.getColor(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    AnimatedVisibility(
                        visible = expandedStock == it.scriptName,
                        enter = slideInHorizontally()
                    ) {
                        Column {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Absolute.SpaceAround
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(0.1f)
                                ) {
                                    Text(
                                        text = "Bought On",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(0.1f)
                                ) {
                                    Text(
                                        text = "Buy Price",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(0.1f)
                                ) {
                                    Text(
                                        text = "Sold On",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(0.1f)
                                ) {
                                    Text(
                                        text = "Sell Price",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(0.1f)
                                ) {
                                    Text(text = "Qty", style = MaterialTheme.typography.bodySmall)
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(0.1f)
                                ) {
                                    Text(text = "PnL", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            it.dataList.forEach { details ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight(0.1f)
                                    ) {
                                        Text(
                                            text = details.buy_date,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight(0.1f)
                                    ) {
                                        Text(
                                            text = details.buy_average.formatCurrency(),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight(0.1f)
                                    ) {
                                        Text(
                                            text = details.sell_date,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight(0.1f)
                                    ) {
                                        Text(
                                            text = details.sell_average.formatCurrency(),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight(0.1f)
                                    ) {
                                        Text(
                                            text = details.quantity.toInt().toString(),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight(0.1f)
                                    ) {
                                        Text(
                                            text = (details.sell_amount - details.buy_amount).formatCurrency(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = (details.sell_amount - details.buy_amount).getColor()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandedDropDown(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.CenterEnd) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = { onItemSelected(it) },
                textStyle = MaterialTheme.typography.labelMedium,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = "Dropdown")
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { item ->
                    DropdownMenuItem(text = {

                        Text(text = item)
                    }, onClick = {
                        onItemSelected(item)
                        expanded = false
                    })
                }
            }

        }
    }

}
