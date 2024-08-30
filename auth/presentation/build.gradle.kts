plugins {
    alias(libs.plugins.storyact.android.feature.ui)
    alias(libs.plugins.storyact.jvm.junit5)
}

android {
    namespace = "id.dev.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)

    testImplementation(projects.core.domain)
}