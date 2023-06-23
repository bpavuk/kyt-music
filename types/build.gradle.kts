plugins {
    id("library-convention")
    libs.plugins.kotlinx.serialization.plugin
}

version = libs.versions.kyt.get()

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}
