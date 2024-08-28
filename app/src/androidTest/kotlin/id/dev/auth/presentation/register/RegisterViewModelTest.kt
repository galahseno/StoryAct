@file:OptIn(ExperimentalFoundationApi::class)

package id.dev.auth.presentation.register

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
import id.dev.auth.data.dto.RegisterRequest
import id.dev.auth.domain.UserDataValidator
import id.dev.core.android_test.TestMockEngine
import id.dev.core.android_test.registerFailResponseStub
import id.dev.core.android_test.registerSuccessResponseStub
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
class RegisterViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var viewModel: RegisterViewModel
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
                if (relativeUrl == "/v1/register") {
                    when (currentTest) {
                        "testRegisterSuccess" -> {
                            respond(
                                content = ByteReadChannel(
                                    text = Json.encodeToString(registerSuccessResponseStub)
                                ),
                                headers = headers {
                                    set("Content-Type", "application/json")
                                }
                            )
                        }

                        "testRegisterFail" -> {
                            respond(
                                content = ByteReadChannel(
                                    text = Json.encodeToString(registerFailResponseStub)
                                ),
                                headers = headers {
                                    set("Content-Type", "application/json")
                                },
                                status = HttpStatusCode.BadRequest
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
        viewModel = RegisterViewModel(
            authRepository = authRepository,
            userDataValidator = UserDataValidator(
                patternValidator = EmailPatternValidator
            )
        )
    }

    @Test
    fun testRegisterFail() = runTest {
        currentTest = "testRegisterFail"

        val name = "integration-test"
        val email = "integration-test@test.com"
        val password = "Asdfgh12345"
        assertThat(viewModel.state.canRegister).isFalse()

        viewModel.state.name.edit { append(name) }
        viewModel.state.email.edit { append(email) }
        viewModel.state.password.edit { append(password) }

        viewModel.onAction(RegisterAction.OnRegisterClick)

        assertThat(viewModel.state.isRegistering).isFalse()
        assertThat(viewModel.state.email.text.toString()).isEqualTo(email)
        assertThat(viewModel.state.password.text.toString()).isEqualTo(password)

        val registerRequest = mockEngine.mockEngine.requestHistory.find {
            it.url.encodedPath == "/v1/register"
        }
        assertThat(registerRequest).isNotNull()

        val registerBody = Json.decodeFromString<RegisterRequest>(
            registerRequest!!.body.toByteArray().decodeToString()
        )
        assertThat(registerBody.name).isEqualTo(name)
        assertThat(registerBody.email).isEqualTo(email)
        assertThat(registerBody.password).isEqualTo(password)

        viewModel.events.test {
            val emission1 = awaitItem()
            assertThat(emission1).isInstanceOf(RegisterEvent.Error::class.java)
            assertThat((emission1 as RegisterEvent.Error).error)
                .isEqualTo(UiText.DynamicString("Register Fail"))
        }
    }

    @Test
    fun testRegisterSuccess() = runTest {
        currentTest = "testRegisterSuccess"

        val name = "integration-test"
        val email = "integration-test@test.com"
        val password = "Asdfgh12345"
        assertThat(viewModel.state.canRegister).isFalse()

        viewModel.state.name.edit { append(name) }
        viewModel.state.email.edit { append(email) }
        viewModel.state.password.edit { append(password) }

        viewModel.onAction(RegisterAction.OnRegisterClick)

        assertThat(viewModel.state.isRegistering).isFalse()
        assertThat(viewModel.state.email.text.toString()).isEqualTo(email)
        assertThat(viewModel.state.password.text.toString()).isEqualTo(password)

        val registerRequest = mockEngine.mockEngine.requestHistory.find {
            it.url.encodedPath == "/v1/register"
        }
        assertThat(registerRequest).isNotNull()

        val registerBody = Json.decodeFromString<RegisterRequest>(
            registerRequest!!.body.toByteArray().decodeToString()
        )
        assertThat(registerBody.name).isEqualTo(name)
        assertThat(registerBody.email).isEqualTo(email)
        assertThat(registerBody.password).isEqualTo(password)

        viewModel.events.test {
            val emission1 = awaitItem()
            assertThat(emission1).isInstanceOf(RegisterEvent.RegistrationSuccess::class.java)
        }
    }
}