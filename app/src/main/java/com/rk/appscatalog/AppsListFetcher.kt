package com.rk.appscatalog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.pm.Signature
import android.os.Build
import android.util.Log
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

object AppsListFetcher {

    private const val TAG = "AppsListFetcher"

    fun getAppsList(context: Context) {
        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolvedAppsList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0)
        Log.d(TAG, "getAppsList: Found ${resolvedAppsList.size} applications.")
        resolvedAppsList.forEach { resolvedInfo ->

            val appName = resolvedInfo.loadLabel(packageManager).toString()
            val packageName = resolvedInfo.activityInfo.packageName
            val installedBy = packageManager.getInstallerPackageName(packageName)
            var installedTimestamp: Long? = null
            var lastUpdatedTimestamp: Long? = null
            var activities: List<String> = listOf()
            var services: List<String> = listOf()
            var receivers: List<String> = listOf()
            var permissions: List<Pair<String, String?>> = listOf()
            var providers: List<String> = listOf()
            var appCertificate: AppCertificate? = null

            try {

                val metadataPackageInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
                installedTimestamp = metadataPackageInfo.firstInstallTime
                lastUpdatedTimestamp = metadataPackageInfo.lastUpdateTime

                val activityInfoList = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_ACTIVITIES
                ).activities
                activities = activityInfoList?.mapNotNull { it.name }?.toList() ?: listOf()

                val serviceInfoList =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_SERVICES).services
                services = serviceInfoList?.mapNotNull { it.name }?.toList() ?: listOf()

                val receiverInfoList = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_RECEIVERS
                ).receivers
                receivers = receiverInfoList?.mapNotNull { it.name }?.toList() ?: listOf()

                val permissionInfoList = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_PERMISSIONS
                ).requestedPermissions
                permissions = permissionInfoList?.mapNotNull {
                    Pair(
                        it,
                        getPermissionDescription(context, it)
                    )
                } ?: listOf()

                val providerInfoList = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_PROVIDERS
                ).providers
                providers = providerInfoList?.mapNotNull { it.name }?.toList() ?: listOf()

                val applicationSignatures = getApplicationSignatures(context, packageName)
                Log.d(TAG, "getAppsList: ${applicationSignatures.size}")
                appCertificate =
                    if (applicationSignatures.isNotEmpty()) extractSignatureInformation(
                        applicationSignatures[0]
                    ) else null

            } catch (e: Exception) {
                Log.e(TAG, "getAppsList: ", e)
            }

            var appId: Int? = null
            var isSystemApp = false
            var processName: String? = null
            var minimumSDKDescription: String? = null
            var targetSDKDescription: String? = null
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                isSystemApp = appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                appId = appInfo.uid
                processName = appInfo.processName
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    minimumSDKDescription = Android.getDescriptionLabelFor(appInfo.minSdkVersion)
                }
                targetSDKDescription = Android.getDescriptionLabelFor(appInfo.targetSdkVersion)


            } catch (e: Exception) {
                Log.e(TAG, "getAppsList: ", e)
            }

            val app = App(
                name = appName,
                packageName = packageName,
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

            Log.d(TAG, "getAppsList: ${app.desc}")

        }
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

    private fun getApplicationSignatures(context: Context, packageName: String): List<Signature> {

        var signatures: List<Signature>? = null

        try {
            @SuppressLint("PackageManagerGetSignatures")
            signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
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
            }
        } catch (e: Exception) {
            //Log.e(TAG, "getApplicationSignatures: ", e)
        }

        return signatures ?: emptyList()

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
                commonName = getDNValue("CN", issuerDNName),
                organization = getDNValue("O", issuerDNName),
                organizationUnit = getDNValue("OU", issuerDNName),
                location = getDNValue("L", issuerDNName),
                state = getDNValue("ST", issuerDNName),
                country = getDNValue("C", issuerDNName),
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

}