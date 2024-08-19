plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.android.room)
    alias(libs.plugins.storyact.jvm.ktor)
}

android {
    namespace = "id.dev.story.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.story.domain)
}