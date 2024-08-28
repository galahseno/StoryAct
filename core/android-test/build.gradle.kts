plugins {
    alias(libs.plugins.storyact.android.library.compose)
    alias(libs.plugins.storyact.jvm.junit5)
}

android {
    namespace = "id.dev.core.android_test"
}

dependencies {
    implementation(libs.ktor.client.mock)
    implementation(libs.bundles.ktor)
    implementation(libs.coroutines.test)

    implementation(projects.auth.data)
    api(projects.core.test)

}