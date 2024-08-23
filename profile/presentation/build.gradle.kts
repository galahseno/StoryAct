plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.profile.presentation"
}

dependencies {
    implementation(libs.androidx.material.icons.extended)

    implementation(projects.core.domain)
    implementation(projects.profile.domain)
}