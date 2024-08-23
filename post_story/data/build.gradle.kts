plugins {
    alias(libs.plugins.storyact.android.library)
    alias(libs.plugins.storyact.jvm.ktor)
}

android {
    namespace = "id.dev.post_story.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.postStory.domain)
}