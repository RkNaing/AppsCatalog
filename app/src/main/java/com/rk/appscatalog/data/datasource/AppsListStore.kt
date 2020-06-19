package com.rk.appscatalog.data.datasource

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Log
import com.google.gson.GsonBuilder
import com.rk.appscatalog.data.models.Android
import com.rk.appscatalog.data.models.App
import com.rk.appscatalog.data.models.AppCertificate
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileWriter
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

object AppsListStore {

    private const val TAG = "AppsListFetcher"

    private val apps: MutableList<App> = mutableListOf()

    private var isLoaded: Boolean = false

    fun getApps(context: Context): List<App> {
        if (!isLoaded) {
            loadApps(context)
        }
        return apps
    }

    private fun loadApps(context: Context) {
        apps.clear()
        apps.addAll(
            context.packageManager.getInstalledPackages(0)
                .mapNotNull { it?.applicationInfo?.packageName }
                .mapNotNull { packageName ->
                    runCatching { collectAppInformation(context, packageName) }
                        .getOrNull()
                }
        )
        isLoaded = true
        debugLogFile(context)
    }

    private fun collectAppInformation(context: Context, packageName: String): App {
        val packageManager = context.packageManager

        //region MetaData Information
        val metadataPackageInfo =
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)

        val applicationInfo = metadataPackageInfo.applicationInfo
        val name = applicationInfo.loadLabel(packageManager).toString()
        val icon = applicationInfo.loadIcon(packageManager)
        val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAppCategory(applicationInfo.category)
        } else {
            "Undefined"
        }
        val isSystemApp = applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        val appId = applicationInfo.uid
        val processName = applicationInfo.processName
        val targetSDKDescription =
            Android.getDescriptionLabelFor(
                applicationInfo.targetSdkVersion
            )
        val minimumSDKDescription = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Android.getDescriptionLabelFor(
                applicationInfo.minSdkVersion
            )
        } else null
        val appPackageName = metadataPackageInfo.packageName
        val versionName = metadataPackageInfo.versionName
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            metadataPackageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            metadataPackageInfo.versionCode.toLong()
        }
        val installedTimestamp = metadataPackageInfo.firstInstallTime
        val lastUpdatedTimestamp = metadataPackageInfo.lastUpdateTime
        val installedBy = packageManager.getInstallerPackageName(packageName)
        //endregion

        //region Activities
        val activityInfoList = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_ACTIVITIES
        ).activities
        val activities = activityInfoList?.mapNotNull { it.name }?.toList() ?: listOf()
        //endregion

        //region Services
        val serviceInfoList =
            packageManager.getPackageInfo(packageName, PackageManager.GET_SERVICES).services
        val services = serviceInfoList?.mapNotNull { it.name }?.toList() ?: listOf()
        //endregion

        //region Receivers
        val receiverInfoList = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_RECEIVERS
        ).receivers
        val receivers = receiverInfoList?.mapNotNull { it.name }?.toList() ?: listOf()
        //endregion

        //region Permissions
        val permissionInfoList = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        ).requestedPermissions
        val permissions = permissionInfoList?.mapNotNull {
            Pair(
                it,
                getPermissionDescription(
                    context,
                    it
                )
            )
        } ?: listOf()
        //endregion

        //region Providers
        val providerInfoList = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PROVIDERS
        ).providers
        val providers = providerInfoList?.mapNotNull { it.name }?.toList() ?: listOf()
        //endregion

        //region Signature Information
        val applicationSignatures: List<Signature> =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val signature = context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                ).signingInfo
                if (signature.hasMultipleSigners()) {
                    signature.apkContentsSigners?.toList()
                } else {
                    signature.signingCertificateHistory?.toList()
                }
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures?.toList()
            } ?: listOf()

        val appCertificate =
            if (applicationSignatures.isNotEmpty()) extractSignatureInformation(
                applicationSignatures[0]
            ) else null
        //endregion

        return App(
            name = name,
            icon = icon,
            category = category,
            packageName = appPackageName,
            versionName = versionName,
            versionCode = versionCode,
            installerPackageName = installedBy,
            installedTimestamp = installedTimestamp,
            lastUpdatedTimestamp = lastUpdatedTimestamp,
            isSystemApp = isSystemApp,
            processName = processName,
            minimumSDKDescription = minimumSDKDescription,
            targetSDKDescription = targetSDKDescription,
            appId = appId,
            activities = activities,
            services = services,
            receivers = receivers,
            permissions = permissions,
            providers = providers,
            appCertificate = appCertificate
        )

    }

    private fun getPermissionDescription(context: Context, permission: String): String? {
        try {
            val packageManager = context.packageManager
            val permissionInfo =
                packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA)
            return permissionInfo.loadDescription(packageManager)?.toString()
        } catch (e: Exception) {
            //Log.e(TAG, "getPermissionDescription: ", e)
        }
        return null
    }

    private fun extractSignatureInformation(signature: Signature): AppCertificate? {
        return try {
            val rawCertificate = signature.toByteArray()
            val certificateStream = ByteArrayInputStream(rawCertificate)
            val certificateFactory = CertificateFactory.getInstance("X509")
            val x509Cert: X509Certificate =
                certificateFactory.generateCertificate(certificateStream) as X509Certificate
            val issuerDNName = x509Cert.issuerDN.name
            //CN=Android,OU=Android,O=Google Inc.,L=Mountain View,ST=California,C=US
            Log.d(TAG, "extractSignatureInformation: $issuerDNName")
            AppCertificate(
                commonName = getDNValue(
                    "CN",
                    issuerDNName
                ),
                organization = getDNValue(
                    "O",
                    issuerDNName
                ),
                organizationUnit = getDNValue(
                    "OU",
                    issuerDNName
                ),
                location = getDNValue(
                    "L",
                    issuerDNName
                ),
                state = getDNValue(
                    "ST",
                    issuerDNName
                ),
                country = getDNValue(
                    "C",
                    issuerDNName
                ),
                algorithm = x509Cert.sigAlgName,
                serialNumber = x509Cert.serialNumber.toString(),
                issuedDate = x509Cert.notBefore,
                expiryDate = x509Cert.notAfter
            )
        } catch (e: Exception) {
            return null
        }

    }

    private fun getDNValue(key: String, input: String?): String? {
        var value: String? = null
        try {
            if (input != null) {
                val regEx = "(?:^|,\\s?)(?:$key=(?<val>\"(?:[^\"]|\"\")+\"|[^,]+))".toRegex(
                    RegexOption.IGNORE_CASE
                )
                val matchValue = regEx.find(input)?.value
                if (matchValue != null && matchValue.contains("=")) {
                    val matchValuesSplit = matchValue.split("=")
                    if (matchValuesSplit.size > 1) {
                        value = matchValuesSplit[1]
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getDNValue: ", e)
        }
        return value
    }

    private fun getAppCategory(category: Int): String {
        return when (category) {
            ApplicationInfo.CATEGORY_AUDIO -> "Audio"
            ApplicationInfo.CATEGORY_GAME -> "Game"
            ApplicationInfo.CATEGORY_IMAGE -> "Photo"
            ApplicationInfo.CATEGORY_MAPS -> "Map"
            ApplicationInfo.CATEGORY_NEWS -> "News"
            ApplicationInfo.CATEGORY_PRODUCTIVITY -> "Productivity"
            ApplicationInfo.CATEGORY_SOCIAL -> "Social"
            else -> "Undefined"
        }
    }

    private fun debugLogFile(context: Context) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonLogFile = File(context.cacheDir, "Apps.json")
        gson.toJson(apps, FileWriter(jsonLogFile))
    }

}