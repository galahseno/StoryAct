package id.dev.core.android_test.util

object TestContext {
    var currentTest: TestScenario? = null
}

enum class TestScenario {
    TEST_LOGIN_SUCCESS,
    TEST_LOGIN_FAIL,
    TEST_REGISTER_SUCCESS,
    TEST_REGISTER_FAIL,
}