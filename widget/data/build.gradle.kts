plugins {
    alias(libs.plugins.storyact.android.library)
}

android {
    namespace = "id.dev.widget.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.widget.domain)
}