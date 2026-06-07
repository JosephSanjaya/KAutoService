plugins {
    alias(libs.plugins.kotlinJvm)
    `java-gradle-plugin`
    `maven-publish`
}

group = "io.github.josephsanjaya.kautoservice"
version = "1.0.0"

val kotlinVersion = "2.4.0"

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

gradlePlugin {
    plugins {
        create("kautoservice") {
            id = "io.github.josephsanjaya.kautoservice"
            implementationClass = "io.github.josephsanjaya.kautoservice.gradle.KAutoServiceGradlePlugin"
            displayName = "KAutoService Compiler Plugin"
            description = "Kotlin K2 compiler plugin successor to AutoService"
        }
    }
}
