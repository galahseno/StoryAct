plugins {
    alias(libs.plugins.storyact.android.application.compose)
    alias(libs.plugins.storyact.jvm.ktor)
}

android {
    namespace = "id.dev.storyact"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dynamicFeatures += setOf(":favorite:favorite_feature")
}

dependencies {
    // Coil
    implementation(libs.coil.compose)

    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    // Koin
    implementation(libs.bundles.koin.compose)

    // Location
    implementation(libs.google.android.gms.play.services.location)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Dynamic Feature
    api(libs.feature.delivery.ktx)

    // Timber
    implementation(libs.timber)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(projects.auth.data)
    implementation(projects.auth.domain)
    implementation(projects.auth.presentation)

    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)

    implementation(projects.maps.data)
    implementation(projects.maps.domain)
    implementation(projects.maps.presentation)

    implementation(projects.postStory.data)
    implementation(projects.postStory.domain)
    implementation(projects.postStory.presentation)

    implementation(projects.profile.data)
    implementation(projects.profile.domain)
    implementation(projects.profile.presentation)

    implementation(projects.story.data)
    implementation(projects.story.domain)
    implementation(projects.story.presentation)

    implementation(projects.widget.data)
    implementation(projects.widget.domain)
    implementation(projects.widget.presentation)
}