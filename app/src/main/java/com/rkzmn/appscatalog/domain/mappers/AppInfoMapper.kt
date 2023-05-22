package com.rkzmn.appscatalog.domain.mappers

import com.rkzmn.apps_data_provider.model.App
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import com.rkzmn.appscatalog.utils.kotlin.byteCountToDisplaySize

fun App.getAppInfo(): AppInfo {
    return AppInfo(
        appName = appName,
        appIcon = appIcon,
        versionCode = versionCode,
        versionName = versionName,
        packageName = packageName,
        isSystemApp = isSystemApp,
        isDebuggable = isDebuggable,
        installationSource = installationSource,
        installedTimestamp = installedTimestamp,
        lastUpdatedTimestamp = lastUpdatedTimestamp,
        lastUsedTimestamp = lastUsedTimestamp,
        appSize = appSize,
        minSdk = minSdk,
        minAndroidVersion = minAndroidVersion,
        targetSdk = targetSdk,
        targetAndroidVersion = targetAndroidVersion,
        compileSdk = compileSdk,
        compileSdkAndroidVersion = compileSdkAndroidVersion,
    )
}

val AppInfo.indicators: List<AppTypeIndicator>
    get() {
        val indicators = mutableListOf<AppTypeIndicator>()

        if (isSystemApp) {
            indicators.add(AppTypeIndicator.system)
        } else {
            indicators.add(AppTypeIndicator.installed)
        }

        if (isDebuggable) {
            indicators.add(AppTypeIndicator.debuggable)
        }
        return indicators.toList()
    }

val AppInfo.readableSize: String?
    get() = appSize.takeIf { it > 0 }?.let { byteCountToDisplaySize(it) }

val AppInfo.displayName: String?
    get() = appName.takeIf { it.isNotBlank() && it != packageName }