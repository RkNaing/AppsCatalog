package com.rkzmn.appsdataprovider.utils

import android.content.pm.ApplicationInfo
import java.io.File

internal val ApplicationInfo.appIcon: String?
    get() = icon.takeIf { it != 0 }?.let { "android.resource://$packageName/drawable/$it" }

internal val ApplicationInfo.apkFile: File?
    get() = runCatching { File(publicSourceDir).takeIf { it.exists() } }.getOrNull()

internal val ApplicationInfo.appSize: Long
    get() = runCatching { apkFile?.length() }.getOrNull() ?: 0
