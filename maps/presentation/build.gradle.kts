plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.maps.presentation"
}

dependencies {
    implementation(libs.google.maps.android.compose)
    implementation(libs.coil.compose)

    implementation(projects.core.domain)
    implementation(projects.maps.domain)
}