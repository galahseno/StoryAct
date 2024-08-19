plugins {
    alias(libs.plugins.storyact.android.library)
}

android {
    namespace = "id.dev.maps.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(projects.core.domain)
    implementation(projects.maps.domain)
}