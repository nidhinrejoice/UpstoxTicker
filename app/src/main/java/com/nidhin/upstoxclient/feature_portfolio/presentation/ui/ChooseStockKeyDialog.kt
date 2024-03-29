package com.nidhin.upstoxclient.feature_portfolio.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ChooseStockKeyDialog(
    onDismiss: () -> Unit,
    keysList: List<String>,
    onKeySelected: (String) -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = true),
        onDismissRequest = onDismiss
    ) {
        Surface(modifier = Modifier.padding(12.dp).wrapContentHeight()) {

            LazyColumn {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Choose the key word you want to search",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                items(keysList) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .clickable {
                                    onKeySelected(it)
                                    onDismiss()
                                }
                                .border(
                                    2.dp, MaterialTheme.colorScheme.secondary,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}