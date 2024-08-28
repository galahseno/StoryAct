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
import id.dev.core.android_test.loginFailResponseStub
import id.dev.core.android_test.loginSuccessResponseStub
import id.dev.core.network.HttpClientFactory
import id.dev.core.presentation.ui.UiText
import id.dev.core.test.MainCoroutineExtension
import id.dev.core.test.SessionStorageFake
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@MediumTest
class LoginViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var sessionStorageFake: SessionStorageFake
    private lateinit var mockEngine: TestMockEngine

    private var currentTest: String? = null

    @BeforeEach
    fun setup() {
        sessionStorageFake = SessionStorageFake()
        val mockEngineConfig = MockEngineConfig().apply {
            requestHandlers.add { request ->
                val relativeUrl = request.url.encodedPath
                if (relativeUrl == "/v1/login") {
                    when (currentTest) {
                        "testLoginSuccess" -> {
                            respond(
                                content = ByteReadChannel(
                                    text = Json.encodeToString(loginSuccessResponseStub)
                                ),
                                headers = headers {
                                    set("Content-Type", "application/json")
                                }
                            )
                        }

                        "testLoginFail" -> {
                            respond(
                                content = ByteReadChannel(
                                    text = Json.encodeToString(loginFailResponseStub)
                                ),
                                headers = headers {
                                    set("Content-Type", "application/json")
                                },
                                status = HttpStatusCode.Unauthorized
                            )
                        }

                        else -> {
                            respond(
                                content = byteArrayOf(),
                                status = HttpStatusCode.InternalServerError
                            )
                        }
                    }
                } else {
                    respond(
                        content = byteArrayOf(),
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }
        mockEngine = TestMockEngine(
            dispatcher = mainCoroutineExtension.testDispatcher,
            mockEngineConfig = mockEngineConfig
        )

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

    @Test
    fun testLoginFail() = runTest {
        currentTest = "testLoginFail"

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
        currentTest = "testLoginSuccess"

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