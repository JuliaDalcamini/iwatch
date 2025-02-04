package com.julia.iwatch.common.ui.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.load_error_message
import iwatch.composeapp.generated.resources.refresh_24px
import iwatch.composeapp.generated.resources.try_again_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LoadingError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.load_error_message),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        TextButton(
            modifier = Modifier.padding(top = 4.dp),
            onClick = onRetryClick
        ) {
            Icon(vectorResource(Res.drawable.refresh_24px), null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.try_again_label))
        }
    }
}