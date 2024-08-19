plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.jvm.ktor)
}

android {
    namespace = "id.dev.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.network)
}