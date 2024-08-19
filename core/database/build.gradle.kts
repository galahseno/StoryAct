plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.android.room)
}

android {
    namespace = "id.dev.core.database"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
}