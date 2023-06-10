package com.rkzmn.appsdataprovider

import android.content.Context
import android.content.pm.PackageManager
import com.rkzmn.appsdataprovider.model.App
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission
import com.rkzmn.appsdataprovider.utils.from
import com.rkzmn.appsdataprovider.utils.fromActivityInfo
import com.rkzmn.appsdataprovider.utils.fromReceiverInfo
import com.rkzmn.appsdataprovider.utils.fromServiceInfo
import com.rkzmn.appsdataprovider.utils.getPackageInfoCompat
import com.rkzmn.appsdataprovider.utils.getPackages
import com.rkzmn.appsdataprovider.utils.usageStatsManager

fun getApps(context: Context): List<App> {
    val usageStatsManager = context.usageStatsManager
    return context.packageManager
        .getPackages()
        .map { packageInfo ->
            App.from(
                packageInfo,
                context,
                usageStatsManager
            )
        }
}

fun getAppServices(
    context: Context,
    packageName: String
): List<AppComponent> = context.packageManager
    .getPackageInfoCompat(
        packageName = packageName,
        flags = PackageManager.GET_SERVICES
    )?.services?.map(AppComponent.Companion::fromServiceInfo) ?: emptyList()

fun getAppActivities(
    context: Context,
    packageName: String
): List<AppComponent> = context.packageManager
    .getPackageInfoCompat(
        packageName = packageName,
        flags = PackageManager.GET_ACTIVITIES
    )?.activities?.map { activityInfo ->
        AppComponent.fromActivityInfo(
            info = activityInfo,
            context = context
        )
    } ?: emptyList()

fun getAppReceivers(
    context: Context,
    packageName: String
): List<AppComponent> = context.packageManager
    .getPackageInfoCompat(
        packageName = packageName,
        flags = PackageManager.GET_RECEIVERS
    )?.receivers?.map(AppComponent.Companion::fromReceiverInfo) ?: emptyList()

fun getAppPermissions(
    context: Context,
    packageName: String
): List<AppPermission> = context.packageManager
    .getPackageInfoCompat(
        packageName = packageName,
        flags = PackageManager.GET_PERMISSIONS
    )?.permissions?.map { info ->
        AppPermission.from(
            info = info,
            context = context
        )
    } ?: emptyList()
