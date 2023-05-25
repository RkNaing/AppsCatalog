package com.rkzmn.appsdataprovider.utils

import android.content.pm.PackageInfo

@Suppress("DEPRECATION")
internal val PackageInfo.versionCodeCompat: Long
    get() = if (isSDKIntAtLeast(AndroidVersions.P)) longVersionCode else versionCode.toLong()
