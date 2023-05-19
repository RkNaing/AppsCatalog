package com.rkzmn.appscatalog.domain.model

data class AppDetails(
    val info: AppInfo,
    val activities: List<AppComponentInfo> = emptyList(),
    val services: List<AppComponentInfo> = emptyList(),
    val broadcastReceivers: List<AppComponentInfo> = emptyList(),
    val permissions: List<AppPermissionInfo> = emptyList(),
)
