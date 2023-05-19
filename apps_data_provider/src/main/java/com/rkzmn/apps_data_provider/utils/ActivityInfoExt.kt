package com.rkzmn.apps_data_provider.utils

import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager

fun ActivityInfo.isPrivate(packageManager: PackageManager): Boolean {
    if (!exported) {
        return false
    }

    val activityName = name
    val activityPackageName = packageName
    val componentName = ComponentName(activityPackageName, activityName)
    componentName.shortClassName
    return when (packageManager.getComponentEnabledSetting(componentName)) {
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED -> true
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> false
        else -> !isEnabled
    }
}