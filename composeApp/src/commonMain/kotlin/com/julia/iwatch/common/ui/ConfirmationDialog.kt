package com.julia.iwatch.common.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.cancel_label
import iwatch.composeapp.generated.resources.info_24px
import iwatch.composeapp.generated.resources.ok_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    icon: @Composable () -> Unit = { Icon(vectorResource(Res.drawable.info_24px), null) },
    showDismissButton: Boolean = true
) {
    AlertDialog(
        icon = icon,
        title = { Text(text = title) },
        text = { Text(text = message) },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            if (showDismissButton) {
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(Res.string.cancel_label))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.ok_label))
            }
        }
    )
}