plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.widget.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.widget.domain)
}