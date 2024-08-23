plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.widget.presentation"
}

dependencies {
    implementation(libs.androidx.ui.glance)
    implementation(libs.androidx.ui.glance.material3)
    implementation(libs.bundles.koin)
    implementation(libs.coil.compose)

    implementation(projects.core.domain)
    implementation(projects.widget.domain)
}