package com.rkzmn.appscatalog.domain.mappers

import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appsdataprovider.model.AppComponent

fun AppComponent.getComponentInfo(): AppComponentInfo {
    return AppComponentInfo(
        name = name,
        packageName = packageName,
        fqn = fqn,
        isPrivate = isPrivate
    )
}
