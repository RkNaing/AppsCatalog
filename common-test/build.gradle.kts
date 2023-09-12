@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rkzmn.appscatalog.test.common"
    compileSdk = ProjectConfigs.COMPILE_SDK

    defaultConfig {
        minSdk = ProjectConfigs.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(projects.appsDataProvider)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)

    implementation(libs.junit)
    implementation(libs.junit.android)
    implementation(libs.expression.android)
    implementation(libs.kotlinx.coroutines.test)
}
