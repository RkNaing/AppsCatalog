package com.rkzmn.appscatalog.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.ListPreferenceRepository
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.utils.android.setValue
import com.rkzmn.appscatalog.utils.android.watchValueWithNonNullMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListPreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ListPreferenceRepository {

    override val appsListType: Flow<AppsListType> =
        keyAppsListType.watchValueWithNonNullMapper(dataStore) { rawValue ->
            if (rawValue.isNullOrBlank()) {
                AppsListType.ALL
            } else {
                AppsListType.valueOf(rawValue)
            }
        }

    override suspend fun setAppsListType(type: AppsListType) {
        keyAppsListType.setValue(dataStore, type.name)
    }

    override val displayType: Flow<AppsDisplayType> =
        keyAppsDisplayType.watchValueWithNonNullMapper(dataStore) { rawValue ->
            if (rawValue.isNullOrBlank()) {
                AppsDisplayType.GRID
            } else {
                AppsDisplayType.valueOf(rawValue)
            }
        }

    override suspend fun setAppsDisplayType(type: AppsDisplayType) {
        keyAppsDisplayType.setValue(dataStore, type.name)
    }

    override val sortOption: Flow<AppSortOption> =
        keySortOption.watchValueWithNonNullMapper(dataStore) { rawValue ->
            if (rawValue.isNullOrBlank()) {
                AppSortOption.NAME_ASC
            } else {
                AppSortOption.valueOf(rawValue)
            }
        }

    override suspend fun setSortOption(option: AppSortOption) {
        keySortOption.setValue(dataStore, option.name)
    }

    companion object {
        private val keyAppsListType = stringPreferencesKey("PREF_APPS_LIST_TYPE")
        private val keyAppsDisplayType = stringPreferencesKey("PREF_APPS_DISPLAY_TYPE")
        private val keySortOption = stringPreferencesKey("PREF_APPS_SORT_OPTION")
    }
}
