package id.dev.auth.presentation.login

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import id.dev.auth.domain.AuthRepository
import id.dev.auth.domain.UserDataValidator
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

class LoginViewModelUnitTest {

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension()

    @MockK
    lateinit var userDataValidator: UserDataValidator

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun beforeEach() {
        MockKAnnotations.init(this)
        every { userDataValidator.isValidEmail(any()) } returns true
        every { userDataValidator.isValidPasswordLength(any()) } returns true

        viewModel = LoginViewModel(
            userDataValidator = userDataValidator,
            authRepository = authRepository
        )
    }

    @Test
    fun testViewModelStateAndEventCorrect() = runTest {
        assertThat(viewModel.state.isLoggingIn).isFalse()
        assertThat(viewModel.state.canLogin).isTrue()

        viewModel.onAction(LoginAction.OnTogglePasswordVisibility)
        assertThat(viewModel.state.isPasswordVisible).isTrue()
        viewModel.onAction(LoginAction.OnTogglePasswordVisibility)
        assertThat(viewModel.state.isPasswordVisible).isFalse()

        viewModel.events.test {
            coEvery { authRepository.login(any(), any()) } returns Result.Success("")
            viewModel.onAction(LoginAction.OnLoginClick)

            val emission1 = awaitItem()
            assertThat(emission1).isInstanceOf(LoginEvent.LoginSuccess::class)

            coEvery {
                authRepository.login(any(), any())
            } returns Result.Error(DataError.Network.UNAUTHORIZED, message = "Unauthorized")
            viewModel.onAction(LoginAction.OnLoginClick)

            val emission2 = awaitItem()
            assertThat(emission2).isInstanceOf(LoginEvent.Error::class)
            assertThat((emission2 as LoginEvent.Error).error).isInstanceOf(UiText.DynamicString::class.java)
            assertThat(emission2.error).isEqualTo(UiText.DynamicString("Unauthorized"))

            coEvery {
                authRepository.login(any(), any())
            } returns Result.Error(DataError.Network.BAD_REQUEST, message = "Bad Request")
            viewModel.onAction(LoginAction.OnLoginClick)

            val emission3 = awaitItem()
            assertThat(emission3).isInstanceOf(LoginEvent.Error::class)
            assertThat((emission3 as LoginEvent.Error).error).isInstanceOf(UiText.DynamicString::class.java)
            assertThat(emission3.error).isEqualTo(UiText.DynamicString("Bad Request"))

            coEvery {
                authRepository.login(any(), any())
            } returns Result.Error(
                DataError.Network.SERVER_ERROR,
                message = "Opps, something went wrong, please try again."
            )
            viewModel.onAction(LoginAction.OnLoginClick)

            val emission4 = awaitItem()
            assertThat(emission4).isInstanceOf(LoginEvent.Error::class)
            assertThat((emission4 as LoginEvent.Error).error).isInstanceOf(UiText.StringResource::class.java)
        }
    }
}