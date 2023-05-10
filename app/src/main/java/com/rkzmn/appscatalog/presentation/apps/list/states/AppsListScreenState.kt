package com.rkzmn.appscatalog.presentation.apps.list.states

import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.list.AppItem

data class AppsListScreenState(
    val apps: List<AppItem> = emptyList(),
    val listDisplayType: AppsDisplayType = AppsDisplayType.LIST,
    val listType: AppsListType = AppsListType.ALL,
    val selectedPackageName: String? = null,
    val isLoading: Boolean = false,
)
