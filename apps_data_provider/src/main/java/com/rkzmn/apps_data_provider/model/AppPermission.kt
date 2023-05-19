package com.rkzmn.apps_data_provider.model

data class AppPermission(
    val permission: String,
    val group: String?,
    val description: String?,
    val isDangerous: Boolean
)
