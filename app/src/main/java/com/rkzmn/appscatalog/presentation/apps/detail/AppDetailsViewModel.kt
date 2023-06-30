package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.navigation.ARG_PACKAGE_NAME
import com.rkzmn.appscatalog.presentation.apps.detail.states.AppDetailsScreenState
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    repository: AppDataRepository,
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val appDetailsScreenState = savedStateHandle
        .getStateFlow(ARG_PACKAGE_NAME, "")
        .map { packageName ->
            AppDetailsScreenState(details = repository.getAppDetails(packageName))
        }
        .flowOn(dispatcherProvider.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AppDetailsScreenState()
        )
}
