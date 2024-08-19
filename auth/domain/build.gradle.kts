plugins {
    alias(libs.plugins.storyact.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}