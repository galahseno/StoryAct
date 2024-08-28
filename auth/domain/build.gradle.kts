plugins {
    alias(libs.plugins.storyact.jvm.library)
    alias(libs.plugins.storyact.jvm.junit5)
}

dependencies {
    implementation(projects.core.domain)
}