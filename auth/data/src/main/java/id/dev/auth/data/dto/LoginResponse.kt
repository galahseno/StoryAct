package id.dev.auth.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult? = null,
    val message: String
)