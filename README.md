# KAutoService

[![Version](https://img.shields.io/github/v/release/josephsanjaya/KAutoService)](https://github.com/josephsanjaya/KAutoService/releases)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)

A Kotlin K2 compiler plugin that automatically registers service providers, serving as the native Kotlin successor to Java's AutoService.

## Table of Contents
- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Configuration](#configuration)
- [Usage](#usage)
- [Running the Demo App](#running-the-demo-app)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## About

`KAutoService` simplifies the creation of service providers in Kotlin projects by automating the generation of `META-INF/services` registry files. In standard Java development, registering services requires manual creation and maintenance of resource files, which is error-prone. By bringing native support for Kotlin K2 compiler plugin extensions (FIR and IR), `KAutoService` eliminates this boilerplate at compile time while verifying implementations.

## Features

- **Automatic Service Registration** — Detects `@AutoService` annotations and creates the corresponding service loader provider-configuration files in `META-INF/services/`.
- **Kotlin K2 Compiler Native** — Integrates directly with the new Kotlin K2 compiler frontend (FIR) and backend (IR) IR-generation phases. No KAPT or KSP configurations are required.
- **Compile-Time Verification** — Asserts that classes annotated with `@AutoService` implement the declared interface, failing the build early with clear error feedback if they do not.
- **Multiplatform Compatibility** — Wires into Kotlin Multiplatform targets, supporting JVM, Android, JS, iOS, and Native compilation environments.

## Tech Stack

- **Language:** Kotlin 2.4.0
- **Build System:** Gradle 9.2.1
- **Platform:** Kotlin Multiplatform (targeting Android compileSdk 37, JVM 11, iOS, JS, and Ktor server)

## Project Structure

This repository contains both the core library source modules and a complete Multiplatform demonstration workspace:

### Library Modules
- [kautoservice-annotations](./kautoservice-annotations) — Defines the `@AutoService` annotation.
- [kautoservice-compiler-plugin](./kautoservice-compiler-plugin) — The Kotlin K2 compiler plugin performing verification (FIR) and file generation (IR).
- [kautoservice-gradle-plugin](./kautoservice-gradle-plugin) — Wires the compiler plugin dependencies and provides the configuration DSL extension.

### Demo Application Modules
- [core](./core) — The KMP library module utilizing `KAutoService` to define services.
- [app](./app) — Contains platform-specific entry points:
  - [app/androidApp](./app/androidApp) — Android application.
  - [app/desktopApp](./app/desktopApp) — Compose Multiplatform desktop application.
  - [app/iosApp](./app/iosApp) — Xcode project wrapping SwiftUI/Compose Multiplatform sharing.
  - [app/webApp](./app/webApp) — React web application targeting Kotlin/JS.
  - [app/sharedLogic](./app/sharedLogic) — Shared KMP business logic.
  - [app/sharedUI](./app/sharedUI) — Shared Compose UI components.
- [server](./server) — Ktor JVM server demonstrating service loader resolution on a backend.

## Prerequisites

| Requirement | Minimum Version | Notes |
|-------------|----------------|-------|
| JDK | 17 | Required for Kotlin compiler plugin execution |
| Gradle | 8.0 | Builds the plugin and project targets |
| Kotlin | 2.0.0 | Required for K2 compiler plugin compatibility |

## Setup

Apply the plugin to your target module's `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.josephsanjaya.kautoservice") version "0.0.1-alpha01"
}
```

> [!NOTE]
> The Gradle plugin automatically configures the annotation processor classpaths and adds the `kautoservice-annotations` dependency to your target module's source sets. No additional manual dependency configuration is required.

## Configuration

Configure the plugin behavior inside the `kautoservice` block in your module's `build.gradle.kts`:

```kotlin
kautoservice {
    enabled.set(true)   // Toggles plugin compilation extensions
    verify.set(true)    // Validates service implementation types
    verbose.set(false)  // Enables detailed compiler log output
}
```

| Property | Type | Default | Description |
|---|---|---|---|
| `enabled` | `Property<Boolean>` | `true` | Enables or disables the compiler plugin. |
| `verify` | `Property<Boolean>` | `true` | Validates that annotated classes implement their specified interfaces. |
| `verbose` | `Property<Boolean>` | `false` | Enables verbose compiler-level logging output. |

## Usage

### 1. Declare the Service and Implementation
Annotate implementation classes with `@AutoService` and provide the target service class reference:

```kotlin
package io.github.josephsanjaya.kautoservice

import io.github.josephsanjaya.kautoservice.annotations.AutoService

interface MyService {
    fun hello(): String
}

@AutoService(MyService::class)
class MyServiceImpl : MyService {
    override fun hello(): String = "Hello from MyServiceImpl"
}
```

### 2. Load the Service
Load the registered implementations using the JDK `ServiceLoader`:

```kotlin
import java.util.ServiceLoader

val service = ServiceLoader.load(MyService::class.java).firstOrNull()
println(service?.hello()) // Outputs: Hello from MyServiceImpl
```

## Running the Demo App

Run commands using the Gradle wrapper from the root of this repository:

- **Android App:**
  ```bash
  ./gradlew :app:androidApp:assembleDebug
  ```
- **Desktop App:**
  - Hot reload:
    ```bash
    ./gradlew :app:desktopApp:hotRun --auto
    ```
  - Standard run:
    ```bash
    ./gradlew :app:desktopApp:run
    ```
- **Server:**
  ```bash
  ./gradlew :server:run
  ```
- **Web App (Kotlin/JS):**
  ```bash
  npm install
  npm run start
  ```
- **iOS App:**
  Open [app/iosApp](./app/iosApp) in Xcode and run the project configuration.

## Contributing

1. Fork the repository.
2. Create a new branch: `git checkout -b feat/your-feature`.
3. Commit your changes using [Conventional Commits](https://www.conventionalcommits.org).
4. Push the branch and open a Pull Request.

## License

This project is licensed under the Apache 2.0 License. See `LICENSE` for details.