import com.android.build.api.dsl.DynamicFeatureExtension
import id.dev.convention.ExtensionType
import id.dev.convention.addUiLayerDependency
import id.dev.convention.configureAndroidCompose
import id.dev.convention.configureBuildTypes
import id.dev.convention.configureKotlinAndroid
import id.dev.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidDynamicFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("android.dynamicFeature").get().get().pluginId)
                apply(libs.findPlugin("jetbrains.kotlin.android").get().get().pluginId)
                apply(libs.findPlugin("compose.compiler").get().get().pluginId)
            }

            extensions.configure<DynamicFeatureExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.DYNAMIC_FEATURE
                )
            }

            dependencies {
                addUiLayerDependency(target)
                "testImplementation"(kotlin("test"))
            }
        }
    }
}