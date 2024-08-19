plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}