plugins {
    `kotlin-dsl`
}

group = "id.dev.storyact.build-logic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "storyact.android.application"
            implementationClass = "AndroidAppConventionPlugin"
        }
        register("androidAppCompose") {
            id = "storyact.android.application.compose"
            implementationClass = "AndroidAppComposeConventionPlugin"
        }
        register("androidLib") {
            id = "storyact.android.library"
            implementationClass = "AndroidLibConventionPlugin"
        }
        register("androidLibCompose") {
            id = "storyact.android.library.compose"
            implementationClass = "AndroidLibComposeConventionPlugin"
        }
        register("androidFeatureUi") {
            id = "storyact.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom") {
            id = "storyact.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidDynamicFeature") {
            id = "storyact.android.dynamic.feature"
            implementationClass = "AndroidDynamicFeatureConventionPlugin"
        }
        register("jvmLibrary") {
            id = "storyact.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("jvmKtor") {
            id = "storyact.jvm.ktor"
            implementationClass = "JvmKtorConventionPlugin"
        }
    }
}