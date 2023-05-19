package com.rkzmn.appscatalog.domain.model

data class AppPermissionInfo(
    val permission: String,
    val group: String?,
    val description: String?,
    val isDangerous: Boolean
)
