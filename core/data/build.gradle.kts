plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.jvm.ktor)
}

android {
    namespace = "id.dev.core.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    api(projects.core.database)
    api(projects.core.network)
}