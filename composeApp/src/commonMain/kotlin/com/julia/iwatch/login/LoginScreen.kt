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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.iwatch.auth.UserCredentials
import com.julia.iwatch.common.ui.button.PrimaryButton
import com.julia.iwatch.common.ui.button.SecondaryButton
import com.julia.iwatch.common.ui.dialog.ErrorDialog
import iwatch.composeapp.generated.resources.Res
import iwatch.composeapp.generated.resources.app_name
import iwatch.composeapp.generated.resources.create_account_label
import iwatch.composeapp.generated.resources.email_label
import iwatch.composeapp.generated.resources.login_error_message
import iwatch.composeapp.generated.resources.login_error_title
import iwatch.composeapp.generated.resources.login_title
import iwatch.composeapp.generated.resources.password_label
import iwatch.composeapp.generated.resources.sign_in_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoginScreen(
    onSignIn: () -> Unit,
    onRegisterClick: (UserCredentials) -> Unit,
    presetEmail: String? = null,
    presetPassword: String? = null,
    viewModel: LoginViewModel = viewModel { LoginViewModel() }
) {
    LaunchedEffect(Unit) {
        presetEmail?.let { viewModel.setEmail(it) }
        presetPassword?.let { viewModel.setPassword(it) }
    }

    LoginScreen(
        uiState = viewModel.uiState,
        onEmailChange = { viewModel.setEmail(it) },
        onPasswordChange = { viewModel.setPassword(it) },
        onRegisterClick = onRegisterClick,
        onSignInRequest = { viewModel.login() },
        onSignIn = {
            viewModel.resetNavigateToHome()
            onSignIn()
        },
        onDismissErrorRequest = { viewModel.dismissError() }
    )
}

@Composable
private fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: (UserCredentials) -> Unit,
    onSignInRequest: () -> Unit,
    onSignIn: () -> Unit,
    onDismissErrorRequest: () -> Unit
) {
    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            onSignIn()
        }
    }

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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    email = uiState.email,
                    password = uiState.password,
                    onEmailChange = { onEmailChange(it) },
                    onPasswordChange = { onPasswordChange(it) },
                    onSubmit = onSignInRequest,
                    enabled = !uiState.loading
                )

                if (compact) Spacer(Modifier.weight(1f))

                LoginFormButtons(
                    modifier = Modifier.fillMaxWidth(),
                    compact = compact,
                    loading = uiState.loading,
                    onLoginClick = onSignInRequest,
                    onRegisterClick = { onRegisterClick(uiState.typedCredentials) },
                )

                if (compact) Spacer(Modifier.padding(top = 24.dp))
            }
        }
    }

    if (uiState.showError) {
        LoginErrorDialog(onDismissErrorRequest)
    }
}

@Composable
fun LoginErrorDialog(onDismissRequest: () -> Unit) {
    ErrorDialog(
        title = stringResource(Res.string.login_error_title),
        message = stringResource(Res.string.login_error_message),
        onDismissRequest = onDismissRequest
    )
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
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = email,
            onValueChange = onEmailChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.email_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(onDone = { onSubmit() })
        )
    }
}

@Composable
fun LoginFormButtons(
    compact: Boolean,
    loading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier
) {
    if (compact) {
        Column(modifier) {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLoginClick,
                enabled = !loading,
                label = stringResource(Res.string.sign_in_label),
                loading = loading
            )

            Spacer(Modifier.height(8.dp))

            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegisterClick,
                enabled = !loading,
                label = stringResource(Res.string.create_account_label)
            )
        }
    }
    else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(
                modifier = Modifier.weight(1f),
                onClick = onLoginClick,
                enabled = !loading,
                label = stringResource(Res.string.sign_in_label),
                loading = loading
            )

            SecondaryButton(
                modifier = Modifier.weight(1f),
                onClick = onRegisterClick,
                enabled = !loading,
                label = stringResource(Res.string.create_account_label)
            )
        }
    }
}