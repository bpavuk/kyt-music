
plugins {
    `kotlin-dsl`
}

version = libs.versions.kyt.get()

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api(libs.kotlinPlugin)
    api(libs.kotlinx.serialization.plugin)
}
