package com.rkzmn.appscatalog.domain.mappers

import com.rkzmn.appscatalog.domain.model.AppPermissionInfo
import com.rkzmn.appsdataprovider.model.AppPermission

fun AppPermission.getPermissionInfo(): AppPermissionInfo {
    return AppPermissionInfo(
        permission = permission,
        group = group,
        description = description,
        isDangerous = isDangerous
    )
}
