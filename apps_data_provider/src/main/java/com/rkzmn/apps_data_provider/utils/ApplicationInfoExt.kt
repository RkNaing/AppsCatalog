package com.rkzmn.apps_data_provider.utils

import android.content.pm.ApplicationInfo
import android.net.Uri
import timber.log.Timber
import java.io.File

internal val ApplicationInfo.appIcon: String?
    get() = icon.takeIf { it != 0 }?.let { "android.resource://$packageName/drawable/$it" }

internal val ApplicationInfo.apkFile: File?
    get() = try {
        File(publicSourceDir).takeIf { it.exists() }
    } catch (e: Exception) {
        Timber.e(e, "Failed to get APK File for $packageName")
        null
    }

internal val ApplicationInfo.appSize: Long
    get() {
        return try {
            apkFile?.length() ?: 0
        } catch (e: Exception) {
            Timber.e(e, "Failed to get App Size for $packageName")
            0
        }
    }