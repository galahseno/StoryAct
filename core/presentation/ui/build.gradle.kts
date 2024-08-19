plugins {
    alias(libs.plugins.storyact.android.library.compose)
}

android {
    namespace = "id.dev.core.presentation.ui"
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.date)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)
}