package com.rkzmn.appscatalog.data.repositories

import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.ListPreferenceRepository
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeListPreferenceRepository : ListPreferenceRepository {

    private val listTypePref = MutableStateFlow(AppsListType.ALL)
    private val displayTypePref = MutableStateFlow(AppsDisplayType.GRID)
    private val sortOptionPref = MutableStateFlow(AppSortOption.NAME_ASC)

    override val appsListType: Flow<AppsListType> = listTypePref

    override suspend fun setAppsListType(type: AppsListType) {
        listTypePref.update { type }
    }

    override val displayType: Flow<AppsDisplayType> = displayTypePref

    override suspend fun setAppsDisplayType(type: AppsDisplayType) {
        displayTypePref.update { type }
    }

    override val sortOption: Flow<AppSortOption> = sortOptionPref

    override suspend fun setSortOption(option: AppSortOption) {
        sortOptionPref.update { option }
    }
}
