package com.julia.iwatch.common.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.error_24px
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    icon: @Composable () -> Unit = { Icon(vectorResource(Res.drawable.error_24px), null) }
) {
    ConfirmationDialog(
        icon = icon,
        title = title,
        message = message,
        onDismissRequest = onDismissRequest,
        onConfirm = {},
        showDismissButton = false
    )
}