package com.rkzmn.appscatalog.presentation.apps.list.states

import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.list.AppItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AppsListScreenState(
    val apps: ImmutableList<AppItem> = persistentListOf(),
    val listDisplayType: AppsDisplayType = AppsDisplayType.LIST,
    val sortBy: AppSortOption = AppSortOption.NAME_ASC,
    val listType: AppsListType = AppsListType.ALL,
    val selectedPackageName: String? = null,
    val isLoading: Boolean = false,
)
