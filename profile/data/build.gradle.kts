plugins {
    alias(libs.plugins.storyact.android.library)
}

android {
    namespace = "id.dev.profile.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.profile.domain)
}