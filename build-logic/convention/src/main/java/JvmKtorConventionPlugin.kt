import id.dev.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JvmKtorConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)

            dependencies {
               "implementation"(libs.findBundle("ktor").get())
            }
        }
    }
}