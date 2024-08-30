package id.dev.core.android_test.di

import id.dev.core.android_test.TestMockEngine
import id.dev.core.android_test.loginFailResponseStub
import id.dev.core.android_test.loginSuccessResponseStub
import id.dev.core.android_test.registerFailResponseStub
import id.dev.core.android_test.registerSuccessResponseStub
import id.dev.core.android_test.util.TestContext
import id.dev.core.android_test.util.TestScenario
import id.dev.core.test.SessionStorageFake
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val integrationTestModule = module {
    single {
        createMockEngineConfig()
    }
    singleOf(::SessionStorageFake)

    factory { (dispatcher: CoroutineDispatcher) ->
        TestMockEngine(
            dispatcher = dispatcher,
            mockEngineConfig = get()
        )
    }

//    factory { (sessionStorage: SessionStorageFake) ->
//        HttpClientFactory(
//            sessionStorage = sessionStorage
//        ).build(get())
//    }
}

fun createMockEngineConfig(): MockEngineConfig {
    return MockEngineConfig().apply {
        requestHandlers.add { request ->
            val relativeUrl = request.url.encodedPath
            if (relativeUrl == "/v1/register") {
                when (TestContext.currentTest) {
                    TestScenario.TEST_REGISTER_SUCCESS -> {
                        respond(
                            content = ByteReadChannel(
                                text = Json.encodeToString(registerSuccessResponseStub)
                            ),
                            headers = headers {
                                set("Content-Type", "application/json")
                            }
                        )
                    }

                    TestScenario.TEST_REGISTER_FAIL -> {
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
            } else if (relativeUrl == "/v1/login") {
                when (TestContext.currentTest) {
                    TestScenario.TEST_LOGIN_SUCCESS -> {
                        respond(
                            content = ByteReadChannel(
                                text = Json.encodeToString(loginSuccessResponseStub)
                            ),
                            headers = headers {
                                set("Content-Type", "application/json")
                            }
                        )
                    }

                    TestScenario.TEST_LOGIN_FAIL -> {
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
}
