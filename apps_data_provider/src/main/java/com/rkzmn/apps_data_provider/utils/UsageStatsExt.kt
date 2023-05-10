package com.rkzmn.apps_data_provider.utils

import android.app.usage.UsageStatsManager
import timber.log.Timber

fun UsageStatsManager.getLastTimeUsed(packageName: String): Long? {
    val endTime = System.currentTimeMillis()
    val startTime = endTime - 86400000 // 24 hours ago
    val usageStats = queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    val appUsageStats = usageStats.firstOrNull { it.packageName == packageName }
    return appUsageStats?.lastTimeUsed.also {
        Timber.tag(TAG).d("getLastTimeUsed($packageName) returned: $it")
    }
}