package id.dev.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}