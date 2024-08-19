package id.dev.auth.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class LoginResult(
    val name: String,
    val token: String,
    val userId: String
)