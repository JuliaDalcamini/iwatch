package com.julia.iwatch.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.app_name
import iwatch.composeapp.generated.resources.create_account_title
import iwatch.composeapp.generated.resources.email_lable
import iwatch.composeapp.generated.resources.login_title
import iwatch.composeapp.generated.resources.password_lable
import iwatch.composeapp.generated.resources.sign_in_lable
import iwatch.composeapp.generated.resources.sign_up_lable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoginScreen(
    presetEmail: String? = null,
    presetPassword: String? = null
) {
    Scaffold(Modifier.imePadding()) { paddingValues ->
        val scrollState = rememberScrollState()

        BoxWithConstraints(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val compact = maxWidth < 480.dp
            val maxFormWidth = if (compact) Dp.Unspecified else 480.dp

            Column(
                modifier = Modifier
                    .widthIn(max = maxFormWidth)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .consumeWindowInsets(PaddingValues(horizontal = 24.dp))
                    .consumeWindowInsets(paddingValues)
                    .padding(horizontal = 24.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (compact) Spacer(Modifier.weight(1f))

                LoginFormTitle(Modifier.padding(top = 24.dp))

                LoginFormFields(
                    "",
                    password = "",
                    onEmailChange = { "ok" },
                    Modifier.fillMaxWidth()
                )

                if (compact) Spacer(Modifier.weight(1f))

                LoginFormButtons(
                    compact = compact,
                    onLoginClick = {},
                    onSignUpClick = {},
                    modifier = Modifier.fillMaxWidth()
                )

                if (compact) Spacer(Modifier.padding(top = 24.dp))
            }
        }
    }

}

@Composable
fun LoginFormTitle(modifier: Modifier = Modifier) {
    val appName = stringResource(Res.string.app_name)
    val title = stringResource(Res.string.login_title)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = appName,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.fillMaxSize(),
            text = title,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginFormFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = { Text(stringResource(Res.string.email_lable)) },
            singleLine = true,
            enabled = true,
            onValueChange = onEmailChange
        )
    }

    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            label = { Text(stringResource(Res.string.password_lable)) },
            singleLine = true,
            enabled = true,
            onValueChange = onEmailChange
        )
    }
}

@Composable
fun LoginFormButtons(
    compact: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier
) {
    if (compact) {
        Column(modifier) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLoginClick,
                enabled = true,
                content = { Text(stringResource(Res.string.sign_in_lable)) }
            )

            Spacer(Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.create_account_title),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSignUpClick,
                enabled = true,
                content = { Text(stringResource(Res.string.sign_up_lable)) }
            )
        }
    }
    else {
        Row(modifier) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onLoginClick,
                enabled = true,
                content = { Text(stringResource(Res.string.sign_in_lable)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onSignUpClick,
                enabled = true,
                content = { Text(stringResource(Res.string.sign_up_lable)) }
            )
        }
    }
}
