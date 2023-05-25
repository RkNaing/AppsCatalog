package com.rkzmn.appscatalog.domain.repositories

import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import kotlinx.coroutines.flow.Flow

interface ListPreferenceRepository {

    val appsListType: Flow<AppsListType>

    suspend fun setAppsListType(type: AppsListType)

    val displayType: Flow<AppsDisplayType>

    suspend fun setAppsDisplayType(type: AppsDisplayType)

    val sortOption: Flow<AppSortOption>

    suspend fun setSortOption(option: AppSortOption)
}
