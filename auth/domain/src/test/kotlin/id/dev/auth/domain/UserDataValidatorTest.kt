package id.dev.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UserDataValidatorTest {
    private lateinit var userDataValidator: UserDataValidator

    @BeforeEach
    fun setup() {
        userDataValidator = UserDataValidator(
            patternValidator = object : PatternValidator {
                override fun matches(value: String): Boolean {
                    return true
                }
            }
        )
    }

    @ParameterizedTest
    @CsvSource(
        "123Asdfgh, true",
        "123asdfgh, false",
        "12345, false",
        "123-Asdfgh, true",
        "123ASDFGH, false",
    )
    fun testValidatePassword(password: String, expected: Boolean) {
        val state = userDataValidator.validatePassword(password)

        assertThat(state.isValidPassword).isEqualTo(expected)
    }
}