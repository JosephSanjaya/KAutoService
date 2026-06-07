plugins {
    alias(libs.plugins.kotlinMultiplatform)
    `maven-publish`
}

group = "io.github.josephsanjaya.kautoservice"
version = "1.0.0"

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
