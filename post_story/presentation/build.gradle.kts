plugins {
    alias(libs.plugins.storyact.android.feature.ui)
}

android {
    namespace = "id.dev.post_story.presentation"
}

dependencies {
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)

    implementation(projects.postStory.domain)
    implementation(projects.core.domain)
}