package com.rkzmn.appsdataprovider.utils

import android.app.usage.UsageStatsManager
import timber.log.Timber
import kotlin.time.Duration.Companion.hours

fun UsageStatsManager.getLastTimeUsed(packageName: String): Long? {
    val endTime = System.currentTimeMillis()
    val startTime = endTime - 24.hours.inWholeMilliseconds
    val usageStats = queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    val appUsageStats = usageStats.firstOrNull { it.packageName == packageName }
    return appUsageStats?.lastTimeUsed.also {
        Timber.tag(TAG).d("getLastTimeUsed($packageName) returned: $it")
    }
}

