plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    `java-gradle-plugin`
    `maven-publish`
}

group = "io.github.josephsanjaya.kautoservice"
version = libs.versions.kautoservice.get()

val kotlinVersion = "2.4.0"

// Embed the version in the JAR manifest so KAutoServiceGradlePlugin can read it
// at runtime via javaClass.`package`?.implementationVersion
tasks.withType<Jar>().configureEach {
    manifest {
        attributes(
            "Implementation-Title" to "kautoservice-gradle-plugin",
            "Implementation-Version" to project.version
        )
    }
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

gradlePlugin {
    plugins {
        create("kautoservice") {
            id = "io.github.josephsanjaya.kautoservice"
            version = libs.versions.kautoservice.get()
            implementationClass = "io.github.josephsanjaya.kautoservice.gradle.KAutoServiceGradlePlugin"
            displayName = "KAutoService Compiler Plugin"
            description = "Kotlin K2 compiler plugin successor to AutoService"
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("KAutoService Gradle Plugin")
            description.set("Gradle plugin that wires the KAutoService K2 compiler plugin")
            url.set("https://github.com/josephsanjaya/KAutoService")
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/josephsanjaya/KAutoService")
            credentials {
                username = providers.environmentVariable("GITHUB_ACTOR").orNull
                    ?: System.getenv("GITHUB_ACTOR")
                password = providers.environmentVariable("GITHUB_TOKEN").orNull
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
