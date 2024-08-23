plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.favorite.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.androidx.material.icons.extended)

    implementation(projects.favorite.domain)
    implementation(projects.core.domain)
}