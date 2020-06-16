package com.rk.appscatalog

import android.graphics.drawable.Drawable

data class App(
    val name: String,
    val icon: Drawable?,
    val category: String,
    val packageName: String,
    val installerPackageName: String?,
    val installedTimestamp: Long?,
    val lastUpdatedTimestamp: Long?,
    val isSystemApp: Boolean,
    val processName: String?,
    val minimumSDKDescription: String?,
    val targetSDKDescription: String?,
    val appId: Int?,
    val activities: List<String>,
    val services: List<String>,
    val receivers: List<String>,
    val permissions: List<Pair<String, String?>>,
    val providers: List<String>,
    val appCertificate: AppCertificate?
) {


    val desc: String
        get() = """
            Name            :   $name
            Category        :   $category
            Package Name    :   $packageName
            Installed By    :   $installerPackageName
            Installed At    :   ${installedTimestamp.toReadableTimestamp().defaultOnNullOrEmpty()}
            Updated At      :   ${lastUpdatedTimestamp.toReadableTimestamp().defaultOnNullOrEmpty()}
            Is System App   :   $isSystemApp
            Process Name    :   ${processName.defaultOnNullOrEmpty()}
            Minimum SDK     :   ${minimumSDKDescription.defaultOnNullOrEmpty()}
            Target SDK      :   ${targetSDKDescription.defaultOnNullOrEmpty()}
            App Kernel ID   :   ${appId?.toString().defaultOnNullOrEmpty()}
            Activities      :   ${activities.joinToString(separator = "\n${" ".repeat(20)}")}
            Services        :   ${services.joinToString(separator = "\n${" ".repeat(20)}")}
            Permission      :   ${permissions.joinToString(separator = "\n${" ".repeat(20)}")}
            Providers       :   ${providers.joinToString(separator = "\n${" ".repeat(20)}")}
            Certificate     :   ${appCertificate?.desc?.defaultOnNullOrEmpty()}
        """.trimIndent()
}