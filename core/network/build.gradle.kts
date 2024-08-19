plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.jvm.ktor)
}

android {
    namespace = "id.dev.core.network"
}

dependencies {
    api(libs.timber)
    implementation(projects.core.domain)
}