@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.rkzmn.apps_data_provider"
    compileSdk = ProjectConfigs.COMPILE_SDK

    defaultConfig {
        minSdk = ProjectConfigs.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        val javaVersion = JavaVersion.toVersion(ProjectConfigs.javaSourceCodeCompatibilityVersion)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = ProjectConfigs.javaSourceCodeCompatibilityVersion.toString()
    }

    kotlin {
        jvmToolchain(ProjectConfigs.javaToolchainVersion)
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.android)
    androidTestImplementation(libs.expression.android)
}
