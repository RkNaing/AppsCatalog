package com.rkzmn.appscatalog.utils.android

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

typealias AndroidVersions = Build.VERSION_CODES

@ChecksSdkIntAtLeast(parameter = 0)
internal fun isSDKIntAtLeast(sdkInt: Int): Boolean = Build.VERSION.SDK_INT >= sdkInt