package com.rkzmn.apps_data_provider.utils

import android.Manifest
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

internal typealias AndroidVersions = Build.VERSION_CODES
internal typealias Permissions = Manifest.permission

@ChecksSdkIntAtLeast(parameter = 0)
internal fun isSDKIntAtLeast(sdkInt: Int): Boolean = Build.VERSION.SDK_INT >= sdkInt
