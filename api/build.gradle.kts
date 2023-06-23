plugins {
    id("library-convention")
}

version = libs.versions.kyt.get()

dependencies {
    implementation(libs.ktorClient)
    api(projects.types)
}
