plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.android.room)
}

android {
    namespace = "id.dev.core.database"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite.ktx)

    implementation(projects.core.domain)
}