plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.story.presentation"
}

dependencies {
    implementation(libs.androidx.paging.compose)
    implementation(libs.bundles.koin)
    implementation(libs.timber)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)

    implementation(projects.core.domain)
    implementation(projects.story.domain)
}