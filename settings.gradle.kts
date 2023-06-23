enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

includeBuild("build-logic")

include(":api")
include(":api:api-ktor")
include("types")

rootProject.name = "kyt"
