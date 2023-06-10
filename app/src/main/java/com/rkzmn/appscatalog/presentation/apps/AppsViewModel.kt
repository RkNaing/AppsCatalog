package com.rkzmn.appscatalog.presentation.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.domain.repositories.ListPreferenceRepository
import com.rkzmn.appscatalog.presentation.apps.detail.states.AppDetailsScreenState
import com.rkzmn.appscatalog.presentation.apps.list.AppItem
import com.rkzmn.appscatalog.presentation.apps.list.AppSearchResult
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsListScreenState
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsSearchState
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import com.rkzmn.appscatalog.utils.kotlin.emitUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class AppsViewModel @Inject constructor(
    private val repository: AppDataRepository,
    private val listPrefRepo: ListPreferenceRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val defaultAppsListState = AppsListScreenState(
        listDisplayType = AppsDisplayType.GRID,
        sortBy = AppSortOption.NAME_ASC,
        listType = AppsListType.ALL,
    )

    private val _appsListState = MutableStateFlow(defaultAppsListState)
    val appsListState: StateFlow<AppsListScreenState> = _appsListState.asStateFlow()

    private val _appsSearchState = MutableStateFlow(AppsSearchState())
    val appsSearchState: StateFlow<AppsSearchState> = _appsSearchState.asStateFlow()

    private val _appDetailsState = MutableStateFlow(AppDetailsScreenState())
    val appDetailsScreenState: StateFlow<AppDetailsScreenState> = _appDetailsState.asStateFlow()

//    private val listTypeStateFlow = listPrefRepo
//        .appsListType
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
//            initialValue = defaultAppsListType
//        )

//    private val sortOptionStateFlow = listPrefRepo
//        .sortOption
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
//            initialValue = defaultSortOption
//        )

    init {

        viewModelScope.launch {
            combine(
                listPrefRepo.appsListType.distinctUntilChanged(),
                listPrefRepo.sortOption.distinctUntilChanged(),
            ) { listType, sortOption ->
                loadApps(
                    isRefresh = false,
                    sortOption = sortOption,
                    listType = listType
                )
            }.collect()
        }

        viewModelScope.launch {
            listPrefRepo.displayType.distinctUntilChanged().collectLatest { displayType ->
                _appsListState.emitUpdate { it.copy(listDisplayType = displayType) }
            }
        }

        viewModelScope.launch {
            _appsSearchState
                .mapLatest { it.query }
                .debounce(300)
                .distinctUntilChanged()
                .mapLatest { query ->
                    if (query.isNotBlank()) {
                        repository.searchByNameAndPackageName(query).map {
                            AppSearchResult.from(it, query)
                        }
                    } else {
                        emptyList()
                    }
                }
                .flowOn(dispatcherProvider.default)
                .collectLatest { searchResult ->
                    _appsSearchState.update { it.copy(results = searchResult.toImmutableList()) }
                }
        }
    }

    private fun loadApps(
        isRefresh: Boolean,
        sortOption: AppSortOption,
        listType: AppsListType
    ) {
        if (_appsListState.value.isLoading) {
            Timber.d("loadApps: Already loading in progress. No-Op.")
            return
        }

        _appsListState.update { it.copy(isLoading = true) }

        viewModelScope.launch(dispatcherProvider.default) {
            val appItems = repository.getAllApps(
                isRefresh = isRefresh,
                sortOption = sortOption,
                listType = listType
            ).map(AppItem.Companion::from)
            _appsListState.emitUpdate {
                it.copy(
                    apps = appItems.toImmutableList(),
                    sortBy = sortOption,
                    listType = listType,
                    isLoading = false
                )
            }
        }
    }

    fun onSelectListType(type: AppsListType) {
        viewModelScope.launch {
            listPrefRepo.setAppsListType(type)
        }
    }

    fun onSelectDisplayType(type: AppsDisplayType) {
        viewModelScope.launch {
            listPrefRepo.setAppsDisplayType(type)
        }
    }

    fun onSelectSortOption(option: AppSortOption) {
        viewModelScope.launch {
            listPrefRepo.setSortOption(option)
        }
    }

    fun loadAppDetails(packageName: String) {
        if (_appDetailsState.value.isLoading) {
            Timber.d("loadAppDetails: Already in progress. No-Op!")
            return
        }
        _appDetailsState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val details = repository.getAppDetails(packageName)
            _appDetailsState.emitUpdate { it.copy(isLoading = false, details = details) }
        }
    }

    fun onSearchQueryChange(query: String) {
        _appsSearchState.update { it.copy(query = query.trim()) }
    }

    fun onSearchStatusChange(isActive: Boolean) {
        _appsSearchState.update {
            AppsSearchState(isActive = isActive)
        }
    }
}
