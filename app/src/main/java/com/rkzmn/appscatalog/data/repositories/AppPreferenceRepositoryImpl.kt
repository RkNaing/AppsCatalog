package com.rkzmn.appscatalog.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rkzmn.appscatalog.domain.model.AppTheme
import com.rkzmn.appscatalog.domain.repositories.AppPreferenceRepository
import com.rkzmn.appscatalog.utils.android.AndroidVersions
import com.rkzmn.appscatalog.utils.android.isSDKIntAtLeast
import com.rkzmn.appscatalog.utils.android.setValue
import com.rkzmn.appscatalog.utils.android.watchValueWithDefault
import com.rkzmn.appscatalog.utils.android.watchValueWithNonNullMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppPreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AppPreferenceRepository {

    override val appTheme: Flow<AppTheme>
        get() = keyAppTheme.watchValueWithNonNullMapper(dataStore) { rawValue ->
            if (rawValue.isNullOrBlank()) {
                AppTheme.FOLLOW_SYSTEM
            } else {
                AppTheme.valueOf(rawValue)
            }
        }

    override suspend fun setAppTheme(theme: AppTheme) {
        keyAppTheme.setValue(dataStore, theme.name)
    }

    override val isUsingDynamicColors: Flow<Boolean>
        get() = keyDynamicColors
            .watchValueWithDefault(dataStore, isSDKIntAtLeast(AndroidVersions.S))

    override suspend fun setUseDynamicColors(use: Boolean) {
        keyDynamicColors.setValue(dataStore, use)
    }

    companion object {
        private val keyAppTheme = stringPreferencesKey("PREF_APP_THEME")
        private val keyDynamicColors = booleanPreferencesKey("PREF_APP_DYNAMIC_COLORS")
    }
}
