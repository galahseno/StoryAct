pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "StoryAct"
include(":app")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":story:data")
include(":story:domain")
include(":story:presentation")
include(":post_story:data")
include(":post_story:domain")
include(":post_story:presentation")
include(":profile:data")
include(":profile:domain")
include(":profile:presentation")
include(":core:presentation:design_system")
include(":core:presentation:ui")
include(":core:data")
include(":core:domain")
include(":core:database")
include(":core:network")
include(":maps:data")
include(":maps:domain")
include(":maps:presentation")
include(":widget:data")
include(":widget:domain")
include(":widget:presentation")
include(":favorite:data")
include(":favorite:domain")
include(":favorite:presentation")
include(":favorite:favorite_feature")
