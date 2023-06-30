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

    fun refreshAppsList() {
        val currentListState = _appsListState.value
        loadApps(
            isRefresh = true,
            sortOption = currentListState.sortBy,
            listType = currentListState.listType
        )
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

        _appsListState.update { it.copy(isLoading = true, isRefresh = isRefresh) }

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
                    isLoading = false,
                    isRefresh = false,
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
        Timber.d("loadAppDetails() called with: packageName = [$packageName]")
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

    fun onAppSelected(packageName: String) {
        Timber.d("onAppSelected() called with: packageName = [$packageName]")
        selectApp(packageName)
    }

    fun clearSelection() {
        selectApp(null)
    }

    private fun selectApp(packageName: String?) {
        Timber.d("selectAppInternal() called with: packageName = [$packageName]")
        viewModelScope.launch {
            var changesCount = 0
            val updatedList = _appsListState.value.apps.map {
                val wasSelected = it.isSelected
                val matches = it.packageName == packageName
                if (wasSelected != matches) {
                    changesCount += 1
                }
                it.copy(isSelected = matches)
            }.toImmutableList()
            if (changesCount > 0) {
                _appsListState.emitUpdate { it.copy(apps = updatedList) }
            }
            Timber.d("selectApp: $changesCount Selections updated.")
        }
    }
}
