package id.dev.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }
      //  val baseUrl = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
        val baseUrl = System.getenv("BASE_URL")

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                baseUrl,
                                ExtensionType.APPLICATION
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                baseUrl,
                                ExtensionType.APPLICATION
                            )
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                baseUrl,
                                ExtensionType.LIBRARY
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                baseUrl,
                                ExtensionType.LIBRARY
                            )
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                baseUrl,
                                ExtensionType.DYNAMIC_FEATURE
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                baseUrl,
                                ExtensionType.DYNAMIC_FEATURE
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    baseUrl: String,
    extensionType: ExtensionType
) {
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    if (extensionType != ExtensionType.DYNAMIC_FEATURE) {
        isMinifyEnabled = false
    }
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    baseUrl: String,
    extensionType: ExtensionType
) {
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

    if (extensionType != ExtensionType.DYNAMIC_FEATURE) {
        isMinifyEnabled = true
        proguardFiles(
            commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}