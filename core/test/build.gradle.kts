plugins {
    alias(libs.plugins.storyact.jvm.library)
    alias(libs.plugins.storyact.jvm.junit5)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.coroutines.test)
    implementation(libs.junit5.api)

    implementation(projects.core.domain)
    implementation(projects.story.domain)
}