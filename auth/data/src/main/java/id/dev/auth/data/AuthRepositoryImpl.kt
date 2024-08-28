package id.dev.auth.data

import id.dev.auth.data.dto.LoginRequest
import id.dev.auth.data.dto.LoginResponse
import id.dev.auth.data.dto.RegisterRequest
import id.dev.auth.data.dto.RegisterResponse
import id.dev.auth.domain.AuthRepository
import id.dev.core.domain.AuthInfo
import id.dev.core.domain.SessionStorage
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import id.dev.core.network.invalidateBearerTokens
import id.dev.core.network.post
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String, DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.BAD_REQUEST -> {
                        val response: LoginResponse? = result.message?.let {
                            Json.decodeFromString(it)
                        }
                        return Result.Error(DataError.Network.BAD_REQUEST, response?.message)
                    }

                    DataError.Network.UNAUTHORIZED -> {
                        val response: LoginResponse? = result.message?.let {
                            Json.decodeFromString(it)
                        }
                        return Result.Error(DataError.Network.UNAUTHORIZED, response?.message)
                    }

                    else -> return result
                }
            }

            is Result.Success -> {
                sessionStorage.setAuth(
                    AuthInfo(
                        token = result.data.loginResult?.token ?: "",
                        email = email,
                        userId = result.data.loginResult?.userId ?: "",
                        name = result.data.loginResult?.name ?: ""
                    )
                )
                httpClient.invalidateBearerTokens()
                return Result.Success(result.data.message)
            }
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<String, DataError.Network> {
        val result = httpClient.post<RegisterRequest, RegisterResponse>(
            route = "/register",
            body = RegisterRequest(
                name = name,
                email = email,
                password = password
            )
        )
        when (result) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.BAD_REQUEST -> {
                        val response: RegisterResponse? = result.message?.let {
                            Json.decodeFromString(it)
                        }
                        return Result.Error(DataError.Network.BAD_REQUEST, response?.message)
                    }

                    else -> return result
                }
            }

            is Result.Success -> return Result.Success(result.data.message)
        }
    }
}