plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.android.room)
}

android {
    namespace = "id.dev.favorite.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.favorite.domain)
}