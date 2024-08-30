@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.auth.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import id.dev.auth.domain.AuthRepository
import id.dev.auth.domain.PasswordValidationState
import id.dev.auth.domain.UserDataValidator
import id.dev.auth.presentation.login.LoginAction
import id.dev.auth.presentation.login.LoginEvent
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.core.presentation.ui.UiText
import id.dev.core.test.MainCoroutineExtension
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class RegisterViewModelUnitTest {
    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension()

    @MockK
    lateinit var userDataValidator: UserDataValidator

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var viewModel: RegisterViewModel

    @BeforeEach
    fun beforeEach() {
        MockKAnnotations.init(this)
        every { userDataValidator.isValidEmail(any()) } returns true
        every { userDataValidator.isValidPasswordLength(any()) } returns true
        every { userDataValidator.validateName(any()) } returns true
        every { userDataValidator.validatePassword(any()) } returns PasswordValidationState(
            hasMinLength = true,
            hasNumber = true,
            hasLowerCaseCharacter = true,
            hasUpperCaseCharacter = true
        )

        viewModel = RegisterViewModel(
            userDataValidator = userDataValidator,
            authRepository = authRepository
        )
    }

    @Test
    fun testViewModelStateAndEventCorrect() = runTest {
        assertThat(viewModel.state.isRegistering).isFalse()
        assertThat(viewModel.state.canRegister).isTrue()
        assertThat(viewModel.state.passwordValidationState.isValidPassword).isTrue()

        viewModel.onAction(RegisterAction.OnTogglePasswordVisibility)
        assertThat(viewModel.state.isPasswordVisible).isTrue()
        viewModel.onAction(RegisterAction.OnTogglePasswordVisibility)
        assertThat(viewModel.state.isPasswordVisible).isFalse()

        viewModel.events.test {
            coEvery { authRepository.register(any(), any(), any()) } returns Result.Success("")
            viewModel.onAction(RegisterAction.OnRegisterClick)

            val emission1 = awaitItem()
            assertThat(emission1).isInstanceOf(RegisterEvent.RegistrationSuccess::class.java)

            coEvery {
                authRepository.register(any(), any(), any())
            } returns Result.Error(DataError.Network.CONFLICT)
            viewModel.onAction(RegisterAction.OnRegisterClick)

            val emission2 = awaitItem()
            assertThat(emission2).isInstanceOf(RegisterEvent.Error::class)
            assertThat((emission2 as RegisterEvent.Error).error).isInstanceOf(UiText.StringResource::class.java)

            coEvery {
                authRepository.register(any(), any(), any())
            } returns Result.Error(
                DataError.Network.BAD_REQUEST,
                message = "Bad Request"
            )
            viewModel.onAction(RegisterAction.OnRegisterClick)

            val emission3 = awaitItem()
            assertThat(emission3).isInstanceOf(RegisterEvent.Error::class)
            assertThat((emission3 as RegisterEvent.Error).error).isInstanceOf(UiText.DynamicString::class.java)
            assertThat(emission3.error).isEqualTo(UiText.DynamicString("Bad Request"))

            coEvery {
                authRepository.register(any(), any(), any())
            } returns Result.Error(
                DataError.Network.SERVER_ERROR,
                message = "Opps, something went wrong, please try again."
            )
            viewModel.onAction(RegisterAction.OnRegisterClick)

            val emission4 = awaitItem()
            assertThat(emission4).isInstanceOf(RegisterEvent.Error::class)
            assertThat((emission4 as RegisterEvent.Error).error).isInstanceOf(UiText.StringResource::class.java)
        }
    }
}