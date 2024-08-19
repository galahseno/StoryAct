plugins {
    alias(libs.plugins.storyact.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)

    implementation(projects.core.domain)
}