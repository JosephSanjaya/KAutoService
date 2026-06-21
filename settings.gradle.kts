rootProject.name = "KAutoService"
gradle.startParameter.isConfigureOnDemand = false
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("kautoservice-gradle-plugin")

pluginManagement {
    repositories {
        mavenLocal()
        // GitHub Packages — resolves the kautoservice Gradle plugin after first publish
        maven {
            mavenContent {
                includeGroupAndSubgroups("io.github.josephsanjaya")
            }
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/josephsanjaya/KAutoService")
            credentials {
                username = providers.environmentVariable("GITHUB_ACTOR")
                    .orElse(providers.gradleProperty("gpr.user"))
                    .orNull
                password = providers.environmentVariable("GITHUB_TOKEN")
                    .orElse(providers.gradleProperty("gpr.key"))
                    .orNull
            }
        }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":app:androidApp")
include(":app:desktopApp")
include(":app:sharedLogic")
include(":app:sharedUI")
include(":core")
include(":server")

include(":kautoservice-annotations")
include(":kautoservice-compiler-plugin")