package com.rkzmn.apps_data_provider

import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import com.rkzmn.apps_data_provider.model.App
import com.rkzmn.apps_data_provider.model.AppComponent
import com.rkzmn.apps_data_provider.model.AppPermission
import com.rkzmn.apps_data_provider.utils.AndroidVersions
import com.rkzmn.apps_data_provider.utils.appIcon
import com.rkzmn.apps_data_provider.utils.appSize
import com.rkzmn.apps_data_provider.utils.getInstallerPackage
import com.rkzmn.apps_data_provider.utils.getLastTimeUsed
import com.rkzmn.apps_data_provider.utils.getPackageInfoCompat
import com.rkzmn.apps_data_provider.utils.getPackages
import com.rkzmn.apps_data_provider.utils.isPrivate
import com.rkzmn.apps_data_provider.utils.isSDKIntAtLeast
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
    val serviceInfoList = packageManager
        .getPackageInfoCompat(packageName, PackageManager.GET_SERVICES)?.services ?: emptyArray()

    return serviceInfoList.map { info ->
        val name = info.name
        val servicePackageName = info.packageName
        val componentName = ComponentName(servicePackageName, name)
        AppComponent(
            name = componentName.shortClassName,
            packageName = info.packageName,
            fqn = name,
            isPrivate = !info.exported,
            type = AppComponent.Type.SERVICE
        )
    }
}

fun getAppActivities(context: Context, packageName: String): List<AppComponent> {
    val packageManager = context.packageManager
    val activityInfoList = packageManager
        .getPackageInfoCompat(packageName, PackageManager.GET_ACTIVITIES)?.activities
        ?: emptyArray()

    return activityInfoList.map { info ->
        val name = info.name
        val activityPackageName = info.packageName
        val componentName = ComponentName(activityPackageName, name)
        AppComponent(
            name = componentName.shortClassName,
            packageName = activityPackageName,
            fqn = name,
            isPrivate = info.isPrivate(packageManager),
            type = AppComponent.Type.ACTIVITY
        )
    }
}

fun getAppReceivers(context: Context, packageName: String): List<AppComponent> {
    val packageManager = context.packageManager
    val receiverInfoList = packageManager
        .getPackageInfoCompat(packageName, PackageManager.GET_ACTIVITIES)?.receivers
        ?: emptyArray()

    return receiverInfoList.map { info ->
        val name = info.name
        val receiverPackageName = info.packageName
        val componentName = ComponentName(receiverPackageName, name)
        AppComponent(
            name = componentName.shortClassName,
            packageName = receiverPackageName,
            fqn = name,
            isPrivate = !info.exported,
            type = AppComponent.Type.BROADCAST
        )
    }
}

fun getAppPermissions(context: Context, packageName: String): List<AppPermission> {
    val packageManager = context.packageManager
    val permissionInfoList = packageManager
        .getPackageInfoCompat(packageName, PackageManager.GET_ACTIVITIES)?.permissions
        ?: emptyArray()

    return permissionInfoList.map { info ->
        val name = info.name
        val permissionGroup = info.group?.let {
            runCatching {
                packageManager.getPermissionGroupInfo(it, 0)
                    .loadLabel(packageManager)
            }
        }?.getOrNull()?.toString()
        val description = runCatching { info.loadDescription(packageManager) }
            .getOrNull()?.toString()
        @Suppress("DEPRECATION") val protectionFlags =
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