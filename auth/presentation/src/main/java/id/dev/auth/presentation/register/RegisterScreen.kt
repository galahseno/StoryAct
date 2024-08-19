@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.auth.presentation.register

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import id.dev.auth.domain.PasswordValidationState
import id.dev.auth.domain.UserDataValidator
import id.dev.auth.presentation.R
import id.dev.core.presentation.design_system.CheckIcon
import id.dev.core.presentation.design_system.CrossIcon
import id.dev.core.presentation.design_system.EmailIcon
import id.dev.core.presentation.design_system.Lora
import id.dev.core.presentation.design_system.NameIcon
import id.dev.core.presentation.design_system.SignUpIcon
import id.dev.core.presentation.design_system.StoryActDarkRed
import id.dev.core.presentation.design_system.StoryActOrange
import id.dev.core.presentation.design_system.StoryActTheme
import id.dev.core.presentation.design_system.components.StoryActActionButton
import id.dev.core.presentation.design_system.components.StoryActLogo
import id.dev.core.presentation.design_system.components.StoryActPasswordTextField
import id.dev.core.presentation.design_system.components.StoryActTextField
import id.dev.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegisterEvent.Error -> {
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }

            RegisterEvent.RegistrationSuccess -> {
                Toast.makeText(
                    context,
                    R.string.registration_successful,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulRegistration()
            }
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is RegisterAction.OnLoginClick -> onSignInClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp)
            .padding(top = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            StoryActLogo(
                logo = SignUpIcon
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.create_account),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = Lora,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                append(stringResource(id = R.string.already_have_an_account) + " ")
                pushStringAnnotation(
                    tag = "clickable_text",
                    annotation = stringResource(id = R.string.sign_in)
                )
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = Lora
                    )
                ) {
                    append(stringResource(id = R.string.sign_in))
                }
            }
        }
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = "clickable_text",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    onAction(RegisterAction.OnLoginClick)
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        StoryActTextField(
            state = state.name,
            startIcon = NameIcon,
            endIcon = if (state.isNameValid) {
                CheckIcon
            } else null,
            hint = stringResource(id = R.string.example_name),
            title = stringResource(id = R.string.name),
            modifier = Modifier.fillMaxWidth(),
            additionalInfo = stringResource(
                id = R.string.at_least_x_characters,
                UserDataValidator.MIN_NAME_LENGTH
            ),
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(16.dp))
        StoryActTextField(
            state = state.email,
            startIcon = EmailIcon,
            endIcon = if (state.isEmailValid) {
                CheckIcon
            } else null,
            hint = stringResource(id = R.string.example_email),
            title = stringResource(id = R.string.email),
            modifier = Modifier.fillMaxWidth(),
            additionalInfo = stringResource(id = R.string.must_be_a_valid_email),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(16.dp))
        StoryActPasswordTextField(
            state = state.password,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = {
                onAction(RegisterAction.OnTogglePasswordVisibilityClick)
            },
            hint = stringResource(id = R.string.example_password),
            title = stringResource(id = R.string.password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        PasswordRequirement(
            text = stringResource(
                id = R.string.at_least_x_characters,
                UserDataValidator.MIN_PASSWORD_LENGTH
            ),
            isValid = state.passwordValidationState.hasMinLength
        )
        Spacer(modifier = Modifier.height(4.dp))
        PasswordRequirement(
            text = stringResource(
                id = R.string.at_least_one_number,
            ),
            isValid = state.passwordValidationState.hasNumber
        )
        Spacer(modifier = Modifier.height(4.dp))
        PasswordRequirement(
            text = stringResource(
                id = R.string.contains_lowercase_char,
            ),
            isValid = state.passwordValidationState.hasLowerCaseCharacter
        )
        Spacer(modifier = Modifier.height(4.dp))
        PasswordRequirement(
            text = stringResource(
                id = R.string.contains_uppercase_char,
            ),
            isValid = state.passwordValidationState.hasUpperCaseCharacter
        )
        Spacer(Modifier.weight(0.5f))
        StoryActActionButton(
            text = stringResource(id = R.string.sign_up),
            isLoading = state.isRegistering,
            enabled = state.canRegister && !state.isRegistering,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(RegisterAction.OnRegisterClick)
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun PasswordRequirement(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isValid) {
                CheckIcon
            } else {
                CrossIcon
            },
            contentDescription = null,
            tint = if (isValid) StoryActOrange else StoryActDarkRed,
            modifier = Modifier
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisterScreenRotScreenPreview() {
    StoryActTheme {
        RegisterScreen(
            state = RegisterState(
                passwordValidationState = PasswordValidationState(
                    hasNumber = true,
                    hasLowerCaseCharacter = true,
                    hasMinLength = true,
                    hasUpperCaseCharacter = true,
                ),
                canRegister = true,
                isEmailValid = true
            ),
            onAction = {}
        )
    }
}