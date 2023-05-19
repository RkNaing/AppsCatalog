package com.rkzmn.appscatalog.domain.mappers

import com.rkzmn.apps_data_provider.model.AppPermission
import com.rkzmn.appscatalog.domain.model.AppPermissionInfo

fun AppPermission.getPermissionInfo(): AppPermissionInfo {
    return AppPermissionInfo(
        permission = permission,
        group = group,
        description = description,
        isDangerous = isDangerous
    )
}