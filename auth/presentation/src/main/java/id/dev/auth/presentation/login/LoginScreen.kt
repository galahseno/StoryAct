@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.auth.presentation.login

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.auth.domain.UserDataValidator
import id.dev.auth.presentation.R
import id.dev.core.presentation.design_system.EmailIcon
import id.dev.core.presentation.design_system.Lora
import id.dev.core.presentation.design_system.SignInIcon
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.design_system.components.StoryActActionButton
import id.dev.core.presentation.design_system.components.StoryActLogo
import id.dev.core.presentation.design_system.components.StoryActPasswordTextField
import id.dev.core.presentation.design_system.components.StoryActTextField
import id.dev.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.Error -> {
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }

            LoginEvent.LoginSuccess -> {
                Toast.makeText(
                    context,
                    R.string.you_are_logged_in,
                    Toast.LENGTH_LONG
                ).show()
                onLoginSuccess()
            }
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is LoginAction.OnRegisterClick -> onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(vertical = 32.dp)
            .padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            StoryActLogo(
                logo = SignInIcon
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.hello),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(id = R.string.storyact_welcome_text),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        StoryActTextField(
            state = state.email,
            startIcon = EmailIcon,
            endIcon = null,
            keyboardType = KeyboardType.Email,
            hint = stringResource(id = R.string.example_email),
            title = stringResource(id = R.string.email),
            modifier = Modifier.fillMaxWidth(),
            additionalInfo = stringResource(id = R.string.must_be_a_valid_email),
        )
        Spacer(modifier = Modifier.height(16.dp))
        StoryActPasswordTextField(
            state = state.password,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = {
                onAction(LoginAction.OnTogglePasswordVisibility)
            },
            hint = stringResource(id = R.string.example_password),
            title = stringResource(id = R.string.password),
            modifier = Modifier.fillMaxWidth(),
            additionalInfo = stringResource(
                id = R.string.at_least_x_characters,
                UserDataValidator.MIN_PASSWORD_LENGTH
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        StoryActActionButton(
            text = stringResource(id = R.string.sign_in),
            isLoading = state.isLoggingIn,
            enabled = state.canLogin && !state.isLoggingIn,
            onClick = {
                onAction(LoginAction.OnLoginClick)
                keyboardController?.hide()
            },
        )

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = Lora,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                append(stringResource(id = R.string.dont_have_an_account) + " ")
                pushStringAnnotation(
                    tag = "clickable_text",
                    annotation = stringResource(id = R.string.sign_up)
                )
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = Lora
                    )
                ) {
                    append(stringResource(id = R.string.sign_up))
                }
            }
        }
        Spacer(Modifier.weight(0.5f))
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = "clickable_text",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    onAction(LoginAction.OnRegisterClick)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenPreview() {
    StoryActTheme {
        LoginScreen(
            state = LoginState(
                canLogin = false,
            ),
            onAction = {}
        )
    }
}