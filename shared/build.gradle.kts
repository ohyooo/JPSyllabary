@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kmm)
    alias(libs.plugins.jc)
    alias(libs.plugins.alp)
    alias(libs.plugins.ks)
    alias(libs.plugins.cc)
}

group = "com.ohyooo"
version = "1.0.0"

kotlin {
    androidLibrary {
        namespace = "com.ohyooo.jpsyllabary.shared"
        compileSdk = libs.versions.compile.sdk.get().toInt()
        minSdk = libs.versions.min.sdk.get().toInt()

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        withJava()
        compilerOptions {
            jvmTarget.set(
                org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
            )
        }
        androidResources{
            enable = true
        }
    }

    jvm("desktop")
    wasmJs {
        outputModuleName = "shared"
        browser {
            commonWebpackConfig {
                outputFileName = "shared.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static(project.projectDir.path, true)
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val osName = System.getProperty("os.name").lowercase()
        val osArch = System.getProperty("os.arch").lowercase()
        val isArm64 = osArch == "aarch64" || osArch == "arm64"
        val composeDesktopCurrentOs = when {
            "mac" in osName && isArm64 -> libs.compose.desktop.jvm.macos.arm64
            "mac" in osName -> libs.compose.desktop.jvm.macos.x64
            "linux" in osName && isArm64 -> libs.compose.desktop.jvm.linux.arm64
            "linux" in osName -> libs.compose.desktop.jvm.linux.x64
            "win" in osName -> libs.compose.desktop.jvm.windows.x64
            else -> error("Unsupported desktop target: $osName/$osArch")
        }

        val commonMain by getting {
            dependencies {
                api(libs.compose.ui)
                api(libs.compose.runtime)
                api(libs.compose.foundation)
                api(libs.compose.material)
                api(libs.compose.material3)
                api(libs.compose.material.icons.extended)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.compose.components.resources)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.core.ktx)
                api(libs.androidx.startup.runtime)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.compose.ui.tooling)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(composeDesktopCurrentOs)
            }
        }
    }
}

tasks.register("testClasses")
