plugins {
    alias(libs.plugins.kotlinMultiplatform)
    `maven-publish`
}

group = "io.github.josephsanjaya.kautoservice"
version = providers.gradleProperty("kautoservice.version").getOrElse("1.0.0")

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()
    js {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // No external dependencies needed for thin annotations
        }
    }
}

publishing {
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
