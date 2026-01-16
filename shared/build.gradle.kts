@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import com.android.kotlin.multiplatform.ide.models.serialization.androidSourceSetKey
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
        val commonMain by getting {
            dependencies {
                api(compose.ui)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.material3)
                api(compose.materialIconsExtended)
                implementation(compose.components.resources)
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
                compileOnly(libs.compose.ui.tooling)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                api(compose.preview)
            }
        }
    }
}


task("testClasses")
