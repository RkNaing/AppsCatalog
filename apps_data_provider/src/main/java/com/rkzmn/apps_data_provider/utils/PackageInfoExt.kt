package com.rkzmn.apps_data_provider.utils

import android.content.pm.PackageInfo

@Suppress("DEPRECATION")
internal val PackageInfo.versionCodeCompat: Long
    get() = if (isSDKIntAtLeast(AndroidVersions.P)) longVersionCode else versionCode.toLong()