package com.rkzmn.appscatalog.presentation.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.presentation.apps.list.AppItem
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsListScreenState
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import com.rkzmn.appscatalog.utils.kotlin.emitUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val repository: AppDataRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val _appsListState = MutableStateFlow(AppsListScreenState())
    val appsListState: StateFlow<AppsListScreenState> = _appsListState.asStateFlow()

    init {
        loadApps()
    }

    fun loadApps() {
        if (_appsListState.value.isLoading) {
            Timber.d("loadApps: Already loading in progress. No-Op.")
            return
        }
        _appsListState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcherProvider.default) {
            val appItems = repository.getAllApps().map(AppItem.Companion::from)
            _appsListState.emitUpdate { it.copy(apps = appItems, isLoading = false) }
        }
    }

}