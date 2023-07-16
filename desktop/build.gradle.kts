import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.ohyooo"
version = "1.0.0"

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose {
    kotlinCompilerPlugin.set(Libs.Compose.compiler)
    desktop {
        application {
            mainClass = "MainKt"
            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "JPSyllabary"
                packageVersion = "1.0.0"

                windows {
                    menu = true
                    // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                    upgradeUuid = "7a05f8ea-dbec-4d37-8058-6432d5a7dc5f"
                }

                macOS {
                    bundleID = "com.ohyooo.jpsyllabary.widgets"
                }
            }
            buildTypes.release.proguard {
                configurationFiles.from("proguard.pro")
            }
        }
    }
}

