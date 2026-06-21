plugins {
    alias(libs.plugins.kotlinJvm)
    `maven-publish`
}

group = "io.github.josephsanjaya.kautoservice"
version = libs.versions.kautoservice.get()

val kotlinVersion = "2.4.0"

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
    implementation(project(":kautoservice-annotations"))
}

kotlin {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_4)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_4)
        freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("KAutoService Compiler Plugin")
                description.set("Kotlin K2 compiler plugin for generating META-INF/services files")
                url.set("https://github.com/josephsanjaya/KAutoService")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
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
