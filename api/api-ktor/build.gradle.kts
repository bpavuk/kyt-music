plugins {
    id("library-convention")
}

version = libs.versions.kyt.get()

dependencies {
    implementation(libs.ktorClient)
    implementation(libs.ktorClient.cio)
    implementation(libs.ktorClient.plugins.contentNegotiation)
    implementation(libs.ktorClient.plugins.contentNegotiation.json)
    implementation(libs.ktorClient.plugins.contentNegotiation.xml)
    implementation(libs.ktorClient.plugins.jSoup)
    implementation(libs.ktorClient.plugins.contentEncoding)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines)

    api(projects.api)
}
