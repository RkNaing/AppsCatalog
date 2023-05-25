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
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppPreferenceRepositoryImpl @Inject constructor(
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val dataStore: DataStore<Preferences>,
) : AppPreferenceRepository {

    override val appTheme: Flow<AppTheme>
        get() = keyAppTheme.watchValueWithNonNullMapper(dataStore) { rawValue ->
            if (rawValue.isNullOrBlank()) {
                AppTheme.FOLLOW_SYSTEM
            } else {
                AppTheme.valueOf(rawValue)
            }
        }.flowOn(dispatcherProvider.io)

    override suspend fun setAppTheme(theme: AppTheme) {
        withContext(dispatcherProvider.io) {
            keyAppTheme.setValue(dataStore, theme.name)
        }
    }

    override val isUsingDynamicColors: Flow<Boolean>
        get() = keyDynamicColors
            .watchValueWithDefault(dataStore, isSDKIntAtLeast(AndroidVersions.S))
            .flowOn(dispatcherProvider.io)

    override suspend fun setUseDynamicColors(use: Boolean) {
        withContext(dispatcherProvider.io) {
            keyDynamicColors.setValue(dataStore, use)
        }
    }

    companion object {
        private val keyAppTheme = stringPreferencesKey("PREF_APP_THEME")
        private val keyDynamicColors = booleanPreferencesKey("PREF_APP_DYNAMIC_COLORS")
    }
}
