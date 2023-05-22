package com.rkzmn.appscatalog.domain.repositories

import com.rkzmn.appscatalog.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface AppPreferenceRepository {
    val appTheme: Flow<AppTheme>

    suspend fun setAppTheme(theme: AppTheme)

    val isUsingDynamicColors: Flow<Boolean>

    suspend fun setUseDynamicColors(use: Boolean)
}