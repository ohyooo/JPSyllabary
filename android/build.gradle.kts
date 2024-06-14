@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.agp)
    alias(libs.plugins.kgp)
    alias(libs.plugins.jc)
    alias(libs.plugins.cc)
}

group = "com.ohyooo"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.activity.compose)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("signkey.jks")
            storePassword = "123456"
            keyPassword = "123456"
            keyAlias = "demo"

            enableV3Signing = true
            enableV4Signing = true
        }
    }
    namespace = "com.ohyooo.jpsyllabary"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        applicationId = libs.versions.application.id.get()
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.target.sdk.get() + rootProject.extra["gitVersion"]
        proguardFile("proguard-rules.pro")
        signingConfig = signingConfigs.getByName("debug")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "consumer-rules.pro")
        }
    }
    buildFeatures {
        compose = true
        // Disable unused AGP features
        // buildConfig = false
        aidl = false
        renderScript = false
        shaders = false
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
//    }
//    compose {
//        kotlinCompilerPlugin.set(libs.compose.compiler.get().toString())
//    }
}
