package com.rkzmn.appsdataprovider

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import com.rkzmn.appsdataprovider.model.App
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission
import com.rkzmn.appsdataprovider.utils.AndroidVersions
import com.rkzmn.appsdataprovider.utils.appIcon
import com.rkzmn.appsdataprovider.utils.appSize
import com.rkzmn.appsdataprovider.utils.getInstallerPackage
import com.rkzmn.appsdataprovider.utils.getLastTimeUsed
import com.rkzmn.appsdataprovider.utils.getPackageInfoCompat
import com.rkzmn.appsdataprovider.utils.getPackages
import com.rkzmn.appsdataprovider.utils.isPrivate
import com.rkzmn.appsdataprovider.utils.isSDKIntAtLeast
import com.rkzmn.appsdataprovider.utils.usageStatsManager
import com.rkzmn.appsdataprovider.utils.versionCodeCompat

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
        val minSdkInt = if (isSDKIntAtLeast(AndroidVersions.N)) {
            applicationInfo.minSdkVersion
        } else {
            applicationInfo.targetSdkVersion
        }
        val targetSdkInt = applicationInfo.targetSdkVersion
        val compileSdkInt = if (isSDKIntAtLeast(AndroidVersions.S)) {
            applicationInfo.minSdkVersion
        } else {
            applicationInfo.targetSdkVersion
        }

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
            appSize = appSize,
            minSdk = minSdkInt,
            targetSdk = targetSdkInt,
            compileSdk = compileSdkInt
        )
    }

    return apps.toList()
}

fun getAppServices(context: Context, packageName: String): List<AppComponent> {
    val packageManager = context.packageManager
    val serviceInfoList =
        packageManager.getPackageInfoCompat(packageName, PackageManager.GET_SERVICES)?.services
            ?: emptyArray()

    return serviceInfoList.map { info ->
        val name = info.name
        AppComponent(
            name = extractShortClassName(name),
            packageName = info.packageName,
            fqn = name,
            isPrivate = !info.exported,
            type = AppComponent.Type.SERVICE
        )
    }
}

fun getAppActivities(context: Context, packageName: String): List<AppComponent> {
    val packageManager = context.packageManager
    val activityInfoList =
        packageManager.getPackageInfoCompat(packageName, PackageManager.GET_ACTIVITIES)?.activities
            ?: emptyArray()

    return activityInfoList.map { info ->
        val name = info.name
        val activityPackageName = info.packageName
        AppComponent(
            name = extractShortClassName(name),
            packageName = activityPackageName,
            fqn = name,
            isPrivate = info.isPrivate(packageManager),
            type = AppComponent.Type.ACTIVITY
        )
    }
}

fun getAppReceivers(context: Context, packageName: String): List<AppComponent> {
    val packageManager = context.packageManager
    val receiverInfoList =
        packageManager.getPackageInfoCompat(packageName, PackageManager.GET_RECEIVERS)?.receivers
            ?: emptyArray()

    return receiverInfoList.map { info ->
        val name = info.name
        val receiverPackageName = info.packageName
        AppComponent(
            name = extractShortClassName(name),
            packageName = receiverPackageName,
            fqn = name,
            isPrivate = !info.exported,
            type = AppComponent.Type.BROADCAST
        )
    }
}

fun getAppPermissions(context: Context, packageName: String): List<AppPermission> {
    val packageManager = context.packageManager
    val permissionInfoList = packageManager.getPackageInfoCompat(
        packageName,
        PackageManager.GET_PERMISSIONS
    )?.permissions ?: emptyArray()

    return permissionInfoList.map { info ->
        val name = info.name
        val permissionGroup = info.group?.let {
            runCatching {
                packageManager.getPermissionGroupInfo(it, 0).loadLabel(packageManager)
            }
        }?.getOrNull()?.toString()
        val description =
            runCatching { info.loadDescription(packageManager) }.getOrNull()?.toString()

        @Suppress("DEPRECATION")
        val protectionFlags =
            if (isSDKIntAtLeast(AndroidVersions.P)) info.protectionFlags else info.protectionLevel
        val isDangerous = protectionFlags and PermissionInfo.PROTECTION_DANGEROUS != 0
        AppPermission(
            permission = name,
            group = permissionGroup,
            description = description,
            isDangerous = isDangerous
        )
    }
}

private fun extractShortClassName(className: String): String {
    if (className.isBlank() || !className.contains(".")) {
        return className
    }
    return with(className.split(".")) {
        getOrElse(lastIndex, defaultValue = { className })
    }
}
