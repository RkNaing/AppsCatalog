package com.rkzmn.apps_data_provider

import android.content.Context
import android.content.pm.ApplicationInfo
import com.rkzmn.apps_data_provider.model.App
import com.rkzmn.apps_data_provider.utils.appIcon
import com.rkzmn.apps_data_provider.utils.appSize
import com.rkzmn.apps_data_provider.utils.getInstallerPackage
import com.rkzmn.apps_data_provider.utils.getLastTimeUsed
import com.rkzmn.apps_data_provider.utils.getPackages
import com.rkzmn.apps_data_provider.utils.usageStatsManager
import com.rkzmn.apps_data_provider.utils.versionCodeCompat

fun getApps(context: Context): List<App> {
    val apps = mutableListOf<App>()

//    if (!context.hasQueryAllPackagePermission) {
//        return apps
//    }

    val packageManager = context.packageManager
    val packages = packageManager.getPackages()
    val usageStatsManager = context.usageStatsManager

    for (packageInfo in packages) {

        val applicationInfo = packageInfo.applicationInfo

        val appName = applicationInfo.loadLabel(packageManager).toString()
        val appIcon = applicationInfo.appIcon
        val versionCode = packageInfo.versionCodeCompat
        val versionName = packageInfo.versionName
        val packageName = packageInfo.packageName
        val isSystemApp = applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val installationSource = packageManager.getInstallerPackage(context, packageName)
        val installedTime = packageInfo.firstInstallTime
        val lastUpdatedTime = packageInfo.lastUpdateTime
        val lastUsedTimestamp = usageStatsManager?.getLastTimeUsed(packageName)
        val appSize = applicationInfo.appSize

        apps += App(
            appName = appName,
            appIcon = appIcon,
            versionCode = versionCode,
            versionName = versionName,
            packageName = packageName,
            isSystemApp = isSystemApp,
            isDebuggable = isDebuggable,
            installationSource = installationSource,
            installedTimestamp = installedTime,
            lastUpdatedTimestamp = lastUpdatedTime,
            lastUsedTimestamp = lastUsedTimestamp,
            appSize = appSize
        )

    }

    return apps
}

//fun getAppServices(context: Context,packageName: String): List<AppComponent>{
//    val services = mutableListOf<AppComponent>()
//
//
//    return services
//}