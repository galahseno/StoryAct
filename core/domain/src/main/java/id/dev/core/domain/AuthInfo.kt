package id.dev.core.domain

data class AuthInfo(
    val token: String,
    val email: String,
    val userId: String,
    val name: String
)
