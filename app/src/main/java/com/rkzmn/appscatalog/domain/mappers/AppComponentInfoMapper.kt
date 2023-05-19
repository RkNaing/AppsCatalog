package com.rkzmn.appscatalog.domain.mappers

import com.rkzmn.apps_data_provider.model.AppComponent
import com.rkzmn.appscatalog.domain.model.AppComponentInfo

fun AppComponent.getComponentInfo(): AppComponentInfo {
    return AppComponentInfo(
        name = name,
        packageName = packageName,
        fqn = fqn,
        isPrivate = isPrivate
    )
}