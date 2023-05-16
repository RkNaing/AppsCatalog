@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdk = 33
    defaultConfig {
        namespace = "com.rkzmn.appscatalog"
        applicationId = "com.rkzmn.appscatalog"
        minSdk = 22
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    val javaVersion = JavaVersion.VERSION_17
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(project(":apps_data_provider"))

    setupJetpackCompose()

    /* Androidx */
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    /* Hilt https://tinyurl.com/hilt-android */
    val hiltVersion = "2.45"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    /* Preference Datastore https://tinyurl.com/preference-datastore*/
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    /* Kotlin Coroutines */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    /* Timber Logging - https://github.com/JakeWharton/timber */
    implementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

fun DependencyHandlerScope.setupJetpackCompose(){
    val versionComposeBom = "2023.05.00"

    val composeBom = platform("androidx.compose:compose-bom:$versionComposeBom")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.ui:ui")

    // Material 3
    implementation("androidx.compose.material3:material3:1.1.0-rc01")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")

    implementation("androidx.activity:activity-compose:1.6.1") // Integration with activities
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1") // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    // https://developer.android.com/jetpack/compose/libraries#hilt-navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // https://developer.android.com/jetpack/compose/navigation#setup
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // https://coil-kt.github.io/coil/compose/#jetpack-compose
    implementation("io.coil-kt:coil-compose:2.3.0")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
