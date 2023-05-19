package com.rkzmn.appscatalog.domain.mappers

import com.rkzmn.apps_data_provider.model.App
import com.rkzmn.appscatalog.domain.model.AppInfo

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