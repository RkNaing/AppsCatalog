@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    alias(libs.plugins.hilt.android)
}

android {
    compileSdk = ProjectConfigs.COMPILE_SDK
    defaultConfig {
        namespace = ProjectConfigs.APP_ID
        applicationId = ProjectConfigs.APP_ID
        minSdk = ProjectConfigs.MIN_SDK
        targetSdk = ProjectConfigs.TARGET_SDK
        versionCode = ProjectConfigs.APP_VERSION_CODE
        versionName = ProjectConfigs.APP_VERSION_NAME

        testInstrumentationRunner = "com.rkzmn.appscatalog.HiltAndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
        }
    }
}

dependencies {

    implementation(projects.appsDataProvider)

    setupJetpackCompose()

    /* Androidx */
    implementation(libs.core.ktx)
    implementation(libs.core.splashscreen)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    /* Preference Datastore https://tinyurl.com/preference-datastore*/
    implementation(libs.datastore.preference)

    /* Kotlin Coroutines */
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.android)

    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.truth.android)

    testImplementation(libs.turbine)
    androidTestImplementation(libs.turbine)

    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.expression.android)

    testImplementation(projects.commonTest)
    androidTestImplementation(projects.commonTest)

    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
}

fun DependencyHandlerScope.setupJetpackCompose() {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.compose.ui)

    // Material 3
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material.window.size)

    implementation(libs.activity.compose) // Integration with activities
    implementation(libs.viewmodel.compose) // Integration with ViewModels
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.hilt.navigation.compose)

    implementation(libs.navigation.compose)
    androidTestImplementation(libs.navigation.compose.test)

    implementation(libs.coil.compose)

    // Android Studio Preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.compose.junit4)
    debugImplementation(libs.compose.test.manifest)

    /* Accompanist Libraries */
    implementation(libs.accompanist.material.placeholder)
    implementation(libs.accompanist.systemuicontroller)
}
