package com.rkzmn.appsdataprovider.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import timber.log.Timber

/**
 * Get a list of all installed applications on the device
 *
 * @param flag Additional option flags to modify the data returned.
 */
internal fun PackageManager.getPackages(flag: Int = PackageManager.GET_META_DATA): List<PackageInfo> {
    Timber.tag(TAG).d("getPackages() called with: flag = [$flag]")
    return when {
        isSDKIntAtLeast(AndroidVersions.TIRAMISU) -> {
            getInstalledPackages(PackageManager.PackageInfoFlags.of(flag.toLong()))
        }

        else -> {
            @Suppress("DEPRECATION")
            getInstalledPackages(flag)
        }
    }.filterNotNull().also {
        Timber.tag(TAG).d("getPackages() returned: ${it.size} packages.")
    }
}

internal fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int): PackageInfo? {
    Timber.tag(TAG)
        .d("getPackageInfoCompat() called with: packageName = [$packageName], flags = [$flags]")
    return runCatching {
        when {
            isSDKIntAtLeast(AndroidVersions.TIRAMISU) -> {
                getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
            }

            else -> {
                @Suppress("DEPRECATION")
                getPackageInfo(packageName, flags)
            }
        }
    }.getOrElse { e ->
        Timber.tag(TAG).e(e, "getPackageInfoCompat: Failed to get PackageInfo for $packageName")
        return null
    }
}

internal fun PackageManager.getInstallerPackage(context: Context, packageName: String): String? {
    Timber.tag(TAG).d("getInstallerPackage() called with: packageName = [$packageName]")
    return runCatching {
        if (isSDKIntAtLeast(AndroidVersions.R)) {
            if (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.INSTALL_PACKAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getInstallSourceInfo(packageName).installingPackageName
            } else {
                null
            }
        } else {
            @Suppress("DEPRECATION")
            getInstallerPackageName(packageName)
        }
    }.getOrElse { e ->
        Timber.tag(TAG)
            .e(e, "getInstallerPackage: Failed to get Installer Package for $packageName")
        null
    }.also { Timber.tag(TAG).d("getInstallerPackage($packageName) returned: $it") }
}
