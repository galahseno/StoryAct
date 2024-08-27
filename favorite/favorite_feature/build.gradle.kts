plugins {
    alias(libs.plugins.storyact.android.dynamic.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
}
android {
    namespace = "id.dev.favorite.favorite_feature"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.compose)

    implementation(projects.app)
    implementation(projects.core.domain)
    implementation(projects.favorite.presentation)
    implementation(projects.favorite.domain)
    implementation(projects.favorite.data)
}