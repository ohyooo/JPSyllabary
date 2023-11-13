group = "com.ohyooo"
version = "1.0.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

buildscript {
    repositories {
        mavenLocal()
    }
}

plugins {
    kotlin("multiplatform") version libs.versions.kotlin.get() apply false
    alias(libs.plugins.agp) apply false
    alias(libs.plugins.kgp) apply false
    alias(libs.plugins.jc) apply false
}


