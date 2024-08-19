import com.android.build.api.dsl.LibraryExtension
import id.dev.convention.configureAndroidCompose
import id.dev.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("storyact.android.library").get().get().pluginId)
                apply(libs.findPlugin("compose.compiler").get().get().pluginId)
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}