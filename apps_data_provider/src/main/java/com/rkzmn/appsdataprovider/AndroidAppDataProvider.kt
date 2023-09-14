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

internal class AndroidAppDataProvider(private val context: Context) : AppDataProvider {

    override fun getApps(): List<App> {
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

    override fun getAppServices(packageName: String): List<AppComponent> {
        return context.packageManager
            .getPackageInfoCompat(
                packageName = packageName,
                flags = PackageManager.GET_SERVICES
            )?.services?.map(AppComponent.Companion::fromServiceInfo) ?: emptyList()
    }

    override fun getAppActivities(packageName: String): List<AppComponent> {
        return context.packageManager
            .getPackageInfoCompat(
                packageName = packageName,
                flags = PackageManager.GET_ACTIVITIES
            )?.activities?.map { activityInfo ->
                AppComponent.fromActivityInfo(
                    info = activityInfo,
                    context = context
                )
            } ?: emptyList()
    }

    override fun getAppReceivers(packageName: String): List<AppComponent> {
        return context.packageManager
            .getPackageInfoCompat(
                packageName = packageName,
                flags = PackageManager.GET_RECEIVERS
            )?.receivers?.map(AppComponent.Companion::fromReceiverInfo) ?: emptyList()
    }

    override fun getAppPermissions(packageName: String): List<AppPermission> {
        val packageManager = context.packageManager
        val packageInfo = packageManager
            .getPackageInfoCompat(
                packageName = packageName,
                flags = PackageManager.GET_PERMISSIONS
            ) ?: return emptyList()

        return packageInfo.requestedPermissions?.map { permission ->
            val permissionInfo = runCatching {
                packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA)
            }.getOrNull()

            if (permissionInfo != null) {
                AppPermission.from(
                    info = permissionInfo,
                    context = context
                )
            } else {
                AppPermission(
                    permission = permission,
                    group = null,
                    description = null,
                    isDangerous = false
                )
            }
        } ?: emptyList()
    }
}
