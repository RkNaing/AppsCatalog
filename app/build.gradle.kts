@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kover)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.appdistribution)
    id("org.sonarqube") version "4.2.1.3168"
}

sonar {
    properties {
        val sonarProperties = readProperties(file("../config/sonar.properties"))
        sonarProperties.forEach { key, value ->
            property(key as String, value as Any)
        }
        property("sonar.androidLint.reportPaths", "${project.rootDir}/build/reports/lint-results.xml")
        property("sonar.kotlin.detekt.reportPaths", "${project.rootDir}/build/reports/detekt/merge.xml")
    }
}

android {

    val configsDir = "$rootDir${File.separator}config${File.separator}"

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

    lint {
        quiet = true
        abortOnError = false
        xmlReport = true
        xmlOutput = file("../build/reports/lint-results.xml")
    }

    signingConfigs {
        create("release") {
            val signingConfigs = readProperties(file("../config/signing.properties"))
            storeFile = file("$rootDir${File.separator}apps_catalog_keystore")
            storePassword = signingConfigs["storePassword"] as String
            keyPassword = signingConfigs["keyPassword"] as String
            keyAlias = signingConfigs["keyAlias"] as String
        }
    }

    buildTypes {
        val credentialPath = "${configsDir}apps-catalog-e4a0b-140bcb296b4b.json"

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            firebaseAppDistribution {
                artifactType = "AAB"
                releaseNotes = "Sample test release."
                groups = "internaltesters"
                serviceCredentialsFile = credentialPath
            }
        }

        debug {
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotes = "Sample test release."
                groups = "internaltesters"
                serviceCredentialsFile = credentialPath
            }
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    testOptions {
        managedDevices { // https://developer.android.com/studio/test/gradle-managed-devices#create_a_gradle_managed_device
            devices {
                maybeCreate<ManagedVirtualDevice>("pixel2api30").apply {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 2"
                    // Use only API levels 27 and higher.
                    apiLevel = 30
                    // To include Google services, use "google".
                    systemImageSource = "google-atd" // "aosp"
                }
            }
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
        }
    }
}

tasks.register("runUITests") {
    val hasTestFiles = File(rootProject.projectDir.toString() + "/app/src/androidTest")
        .walkTopDown()
        .any { it.name.endsWith(".kt") }

    if (hasTestFiles) {
        println("UI tests found")
        dependsOn("pixel2api30DebugAndroidTest")
    } else {
        println("No UI tests found")
    }
}

dependencies {

    coreLibraryDesugaring(libs.core.desugar)

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
    implementation(libs.kotlinx.datetime)

    implementation(libs.timber)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

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

    testImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.android)

    testImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk.agent)
}

koverReport {
    filters {
        filters {
            excludes {
                packages(
                    "androidx.compose.material3",
                    "dagger.hilt.internal",
                    "hilt_aggregated_deps",
                    "com.rkzmn.appsdataprovider",
                    "com.rkzmn.appscatalog.di",
                    "com.rkzmn.appscatalog.test.common"
                )
                classes("*_Hilt*", "*Hilt_*")
                annotatedBy("*Generated*")
            }
        }
    }
    defaults {
        mergeWith("debug")

        html {
            title = "AppsCatalog"
            onCheck = true
        }

        xml {
            onCheck = true
        }
    }
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
