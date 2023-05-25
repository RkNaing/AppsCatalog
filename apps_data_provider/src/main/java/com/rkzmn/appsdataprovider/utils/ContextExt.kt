package com.rkzmn.appsdataprovider.utils

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import timber.log.Timber

val Context.hasUsageStatsPermission: Boolean
    get() {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val op = AppOpsManager.OPSTR_GET_USAGE_STATS
        val uid = android.os.Process.myUid()
        val mode = runCatching {
            if (isSDKIntAtLeast(AndroidVersions.Q)) {
                appOps.unsafeCheckOpNoThrow(op, uid, packageName)
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(op, uid, packageName)
            }
        }.getOrElse { e ->
            Timber.tag(TAG).e(e)
            return false
        }
        val hasPermission = mode == AppOpsManager.MODE_ALLOWED
        Timber.tag(TAG).d("hasUsageStatsPermission: $hasPermission")
        return hasPermission
    }

// internal val Context.hasQueryAllPackagePermission: Boolean
//    get() = !isSDKIntAtLeast(AndroidVersions.R) || ContextCompat.checkSelfPermission(
//        this,
//        Permissions.QUERY_ALL_PACKAGES
//    ) != PackageManager.PERMISSION_GRANTED

internal val Context.usageStatsManager: UsageStatsManager?
    get() = if (hasUsageStatsPermission) {
        getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
    } else {
        Timber.w("Failed to get instance of UsageStatsManager")
        null
    }
