package com.rkzmn.appsdataprovider.utils

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.content.pm.ServiceInfo
import com.rkzmn.appsdataprovider.model.App
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission

internal fun App.Companion.from(
    packageInfo: PackageInfo,
    context: Context,
    usageStatsManager: UsageStatsManager? = context.usageStatsManager,
): App {
    val applicationInfo = packageInfo.applicationInfo
    val packageManager = context.packageManager
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

    return App(
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

internal fun AppComponent.Companion.fromServiceInfo(info: ServiceInfo): AppComponent {
    val name = info.name
    return AppComponent(
        name = extractShortClassName(name),
        packageName = info.packageName,
        fqn = name,
        isPrivate = !info.exported,
        type = AppComponent.Type.SERVICE
    )
}

internal fun AppComponent.Companion.fromReceiverInfo(info: ActivityInfo): AppComponent {
    val name = info.name
    val receiverPackageName = info.packageName
    return AppComponent(
        name = extractShortClassName(name),
        packageName = receiverPackageName,
        fqn = name,
        isPrivate = !info.exported,
        type = AppComponent.Type.BROADCAST
    )
}

internal fun AppComponent.Companion.fromActivityInfo(
    info: ActivityInfo,
    context: Context
): AppComponent {
    val name = info.name
    val activityPackageName = info.packageName
    return AppComponent(
        name = extractShortClassName(name),
        packageName = activityPackageName,
        fqn = name,
        isPrivate = info.isPrivate(context.packageManager),
        type = AppComponent.Type.ACTIVITY
    )
}

internal fun AppPermission.Companion.from(
    info: PermissionInfo,
    context: Context,
): AppPermission {
    val packageManager = context.packageManager
    val name = info.name
    val permissionGroup = info.group?.let {
        runCatching {
            packageManager.getPermissionGroupInfo(it, PackageManager.GET_META_DATA).loadLabel(packageManager)
        }
    }?.getOrNull()?.toString()
    val description =
        runCatching { info.loadDescription(packageManager) }.getOrNull()?.toString()

    val protectionFlags = if (isSDKIntAtLeast(AndroidVersions.P)) {
        info.protection
    } else {
        @Suppress("DEPRECATION")
        info.protectionLevel
    }
    val isDangerous = protectionFlags and PermissionInfo.PROTECTION_DANGEROUS != 0

    return AppPermission(
        permission = name,
        group = permissionGroup,
        description = description,
        isDangerous = isDangerous
    )
}

private fun extractShortClassName(className: String): String {
    if (className.isBlank() || !className.contains(".")) {
        return className
    }
    return with(className.split(".")) {
        getOrElse(lastIndex, defaultValue = { className })
    }
}
