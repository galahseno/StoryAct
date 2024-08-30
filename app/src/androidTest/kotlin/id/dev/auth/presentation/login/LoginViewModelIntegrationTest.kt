@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.auth.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.test.filters.MediumTest
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import id.dev.auth.data.AuthRepositoryImpl
import id.dev.auth.data.EmailPatternValidator
import id.dev.auth.data.dto.LoginRequest
import id.dev.auth.domain.UserDataValidator
import id.dev.core.android_test.TestMockEngine
import id.dev.core.android_test.di.integrationTestModule
import id.dev.core.android_test.loginSuccessResponseStub
import id.dev.core.android_test.util.TestContext
import id.dev.core.android_test.util.TestScenario
import id.dev.core.network.HttpClientFactory
import id.dev.core.presentation.ui.UiText
import id.dev.core.test.MainCoroutineExtension
import id.dev.core.test.SessionStorageFake
import io.ktor.client.engine.mock.toByteArray
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject

@MediumTest
class LoginViewModelIntegrationTest : KoinTest {

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension()

    private val sessionStorageFake: SessionStorageFake by inject()
    private val mockEngine: TestMockEngine by inject { parametersOf(mainCoroutineExtension.testDispatcher) }

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setup() {
        loadKoinModules(integrationTestModule)

        val httpClient = HttpClientFactory(
            sessionStorage = sessionStorageFake
        ).build(mockEngine)

        authRepository = AuthRepositoryImpl(
            httpClient = httpClient,
            sessionStorage = sessionStorageFake
        )
        viewModel = LoginViewModel(
            authRepository = authRepository,
            userDataValidator = UserDataValidator(
                patternValidator = EmailPatternValidator
            )
        )
    }

    @AfterEach
    fun tearDown() {
        unloadKoinModules(integrationTestModule)
    }

    @Test
    fun testLoginFail() = runTest {
        TestContext.currentTest = TestScenario.TEST_LOGIN_FAIL

        val email = "integration-test@test.com"
        val password = "Asdfgh12345"
        assertThat(viewModel.state.canLogin).isFalse()

        viewModel.state.email.edit { append(email) }
        viewModel.state.password.edit { append(password) }

        viewModel.onAction(LoginAction.OnLoginClick)

        assertThat(viewModel.state.isLoggingIn).isFalse()
        assertThat(viewModel.state.email.text.toString()).isEqualTo(email)
        assertThat(viewModel.state.password.text.toString()).isEqualTo(password)

        val loginRequest = mockEngine.mockEngine.requestHistory.find {
            it.url.encodedPath == "/v1/login"
        }
        assertThat(loginRequest).isNotNull()

        val loginBody = Json.decodeFromString<LoginRequest>(
            loginRequest!!.body.toByteArray().decodeToString()
        )
        assertThat(loginBody.email).isEqualTo(email)
        assertThat(loginBody.password).isEqualTo(password)

        viewModel.events.test {
            val emission1 = awaitItem()
            assertThat(emission1).isInstanceOf(LoginEvent.Error::class)
            assertThat((emission1 as LoginEvent.Error).error)
                .isEqualTo(UiText.DynamicString("Email or Password is wrong"))
        }
    }

    @Test
    fun testLoginSuccess() = runTest {
        TestContext.currentTest = TestScenario.TEST_LOGIN_SUCCESS

        val email = "integration-test@test.com"
        val password = "Asdfgh12345"
        assertThat(viewModel.state.canLogin).isFalse()

        viewModel.state.email.edit { append(email) }
        viewModel.state.password.edit { append(password) }

        viewModel.onAction(LoginAction.OnLoginClick)

        assertThat(viewModel.state.isLoggingIn).isFalse()
        assertThat(viewModel.state.email.text.toString()).isEqualTo(email)
        assertThat(viewModel.state.password.text.toString()).isEqualTo(password)

        val loginRequest = mockEngine.mockEngine.requestHistory.find {
            it.url.encodedPath == "/v1/login"
        }
        assertThat(loginRequest).isNotNull()

        val loginBody = Json.decodeFromString<LoginRequest>(
            loginRequest!!.body.toByteArray().decodeToString()
        )
        assertThat(loginBody.email).isEqualTo(email)
        assertThat(loginBody.password).isEqualTo(password)

        viewModel.events.test {
            val emission1 = awaitItem()
            assertThat(emission1).isInstanceOf(LoginEvent.LoginSuccess::class)
        }

        val session = sessionStorageFake.getAuth()
        assertThat(session?.userId).isEqualTo(loginSuccessResponseStub.loginResult?.userId)
        assertThat(session?.token).isEqualTo(loginSuccessResponseStub.loginResult?.token)
        assertThat(session?.name).isEqualTo(loginSuccessResponseStub.loginResult?.name)
        assertThat(session?.email).isEqualTo(email)
    }
}