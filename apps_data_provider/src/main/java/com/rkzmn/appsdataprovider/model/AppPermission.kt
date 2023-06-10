package com.rkzmn.appsdataprovider.model

data class AppPermission(
    val permission: String,
    val group: String?,
    val description: String?,
    val isDangerous: Boolean
) {
    companion object
}
