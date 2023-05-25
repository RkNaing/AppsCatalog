package com.rkzmn.appscatalog.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkzmn.appscatalog.domain.model.AppTheme
import com.rkzmn.appscatalog.domain.repositories.AppPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPrefRepo: AppPreferenceRepository,
) : ViewModel() {

    val appTheme = appPrefRepo
        .appTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.FOLLOW_SYSTEM
        )

    val useDynamicColors = appPrefRepo.isUsingDynamicColors.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    fun updateAppTheme(theme: AppTheme) {
        viewModelScope.launch { appPrefRepo.setAppTheme(theme) }
    }

    fun updateUseDynamicColors(use: Boolean) {
        viewModelScope.launch {
            appPrefRepo.setUseDynamicColors(use)
        }
    }
}
