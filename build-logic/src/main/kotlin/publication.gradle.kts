
import gradle.kotlin.dsl.accessors._d38c6427db3fcfdef7772860d14e84df.java
import gradle.kotlin.dsl.accessors._d38c6427db3fcfdef7772860d14e84df.publishing

plugins {
    `maven-publish`
}

group = "com.bpavuk.kyt"

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven {
            name = "KYT"
            url = uri("https://maven.pkg.github.com/bpavuk/kyt")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        mavenLocal()
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
