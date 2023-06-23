plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("publication")
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}
