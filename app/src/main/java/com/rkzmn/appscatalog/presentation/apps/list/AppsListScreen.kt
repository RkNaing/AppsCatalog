package com.rkzmn.appscatalog.presentation.apps.list

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.detail.AppDetailsContent
import com.rkzmn.appscatalog.presentation.apps.detail.states.AppDetailsScreenState
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsListScreenState
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsSearchState
import com.rkzmn.appscatalog.presentation.apps.options.AppsListOptionsBottomSheet
import com.rkzmn.appscatalog.ui.theme.seed
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.widgets.ProgressView
import com.rkzmn.appscatalog.utils.app.AppStrings
import com.rkzmn.appscatalog.utils.app.createCountLabelAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    state: AppsListScreenState,
    searchState: AppsSearchState,
    windowSize: WindowSizeClass,
    onItemClicked: (String) -> Unit,
    onSelectAppListType: (AppsListType) -> Unit,
    onSelectDisplayType: (AppsDisplayType) -> Unit,
    onSelectSortOption: (AppSortOption) -> Unit,
    onClickedSettings: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchStatusChanged: (Boolean) -> Unit,
    appDetailsProvider: () -> AppDetailsScreenState,
    modifier: Modifier = Modifier,
    onSearchResultItemClicked: (String) -> Unit = onItemClicked,
    onRefresh: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val subtitle = rememberAppsListSubtitle(state = state, searchState = searchState)

    var showFilterDialog by rememberSaveable { mutableStateOf(false) }

    if (showFilterDialog) {
        AppsListOptionsBottomSheet(
            onDismissRequest = { showFilterDialog = false },
            appsType = state.listType,
            onSelectAppListType = onSelectAppListType,
            displayType = state.listDisplayType,
            onSelectDisplayType = onSelectDisplayType,
            selectedSortOption = state.sortBy,
            onSelectSortOption = onSelectSortOption,
            modifier = Modifier.testTag(TEST_TAG_APPS_LIST_OPTIONS),
        )
    }

    Scaffold(
        modifier = modifier
            .testTag(TEST_TAG_APPS_LIST_SCREEN)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppsListTopAppBar(
                subtitle = subtitle,
                onClickedSettings = onClickedSettings,
                onClickedFilters = { showFilterDialog = true },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isRefresh,
            onRefresh = onRefresh,
        )

        Box(
            modifier = Modifier
                .padding(contentPadding)
                .pullRefresh(pullRefreshState)
        ) {
            if (state.isLoading && !state.isRefresh) {
                ProgressView(
                    message = stringResource(id = AppStrings.lbl_loading),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(TEST_TAG_APPS_LIST_LOADING)
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val colorScheme = MaterialTheme.colorScheme
                    val surfaceColorWithElevation by remember(key1 = colorScheme) {
                        mutableStateOf(colorScheme.surfaceColorAtElevation(3.dp))
                    }
                    val searchBarBackground by remember {
                        derivedStateOf {
                            val alpha =
                                scrollBehavior.state.collapsedFraction.coerceIn(range = 0f..1f)
                            surfaceColorWithElevation.copy(alpha = alpha)
                        }
                    }

                    AppsSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind { drawRect(color = searchBarBackground) }
                            .padding(bottom = spacingMedium),
                        state = searchState,
                        onQueryChanged = onSearchQueryChanged,
                        onItemClicked = onSearchResultItemClicked,
                        onSearchStatusChanged = onSearchStatusChanged,
                    )

                    Row(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        val listModifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()

                        when (state.listDisplayType) {
                            AppsDisplayType.LIST -> AppsList(
                                modifier = listModifier.testTag(TEST_TAG_APPS_LIST),
                                apps = state.apps,
                                onItemClicked = onItemClicked,
                            )

                            AppsDisplayType.GRID -> AppsGrid(
                                modifier = listModifier.testTag(TEST_TAG_APPS_GRID),
                                apps = state.apps,
                                gridMinSize = if (windowSize.widthSizeClass == WindowWidthSizeClass.Medium) {
                                    appGridItemMinSizeLarge
                                } else {
                                    appGridItemMinSize
                                },
                                onItemClicked = onItemClicked,
                            )
                        }

                        if (
                            windowSize.widthSizeClass == WindowWidthSizeClass.Expanded &&
                            windowSize.heightSizeClass > WindowHeightSizeClass.Compact
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(spacingMedium)
                                    .weight(2f)
                                    .fillMaxHeight()
                                    .verticalScroll(state = rememberScrollState())
                            ) {
                                val detailState = appDetailsProvider()
                                if (!detailState.isLoading && detailState.details == null) {
                                    Text(
                                        text = stringResource(id = AppStrings.msg_select_app_to_view),
                                        modifier = Modifier.align(alignment = Alignment.Center),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    AppDetailsContent(
                                        state = detailState,
                                        modifier = Modifier
                                            .matchParentSize()
                                            .testTag(TEST_TAG_APPS_LIST_DETAIL_CONTENT)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = state.isRefresh,
                state = pullRefreshState,
                scale = true,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .testTag(TEST_TAG_APPS_LIST_REFRESH_INDICATOR),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppsSearchBar(
    state: AppsSearchState,
    onQueryChanged: (String) -> Unit,
    onSearchStatusChanged: (Boolean) -> Unit,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = onQueryChanged,
) {
    val searchPadding by animateDpAsState(
        targetValue = if (state.isActive) 0.dp else spacingMedium,
        animationSpec = tween(durationMillis = 500),
        label = "AppsListSearchBarPaddingAnimation"
    )

    Box(
        modifier = modifier
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = searchPadding)
                .align(Alignment.Center),
            query = state.query,
            onQueryChange = onQueryChanged,
            onSearch = onSearch,
            active = state.isActive,
            onActiveChange = onSearchStatusChanged,
            placeholder = {
                Text(text = stringResource(id = AppStrings.hint_search_apps))
            },
            trailingIcon = {
                if (!state.isActive) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = AppStrings.hint_search_apps)
                    )
                } else {
                    IconButton(onClick = {
                        if (state.query.isNotBlank()) {
                            onQueryChanged("")
                        } else {
                            onSearchStatusChanged(false)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(id = AppStrings.content_desc_clear)
                        )
                    }
                }
            }
        ) {
            AppsSearchResultsList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacingMedium)
                    .testTag(TEST_TAG_APPS_SEARCH_RESULTS),
                results = state.results,
                onItemClicked = onItemClicked,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppsListTopAppBar(
    subtitle: AnnotatedString,
    onClickedSettings: () -> Unit,
    onClickedFilters: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacingExtraSmall)
            ) {
                Text(text = stringResource(id = AppStrings.app_name))

                AnimatedVisibility(visible = subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onClickedFilters) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = stringResource(id = AppStrings.content_desc_tune_app_list),
                    tint = seed
                )
            }
            IconButton(onClick = onClickedSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(id = AppStrings.lbl_settings),
                    tint = seed
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun rememberAppsListSubtitle(
    state: AppsListScreenState,
    searchState: AppsSearchState,
): AnnotatedString {
    val context = LocalContext.current
    val subtitle by remember(
        state.listType,
        state.apps.size,
        searchState.isActive,
        searchState.results.size,
    ) { mutableStateOf(createSubtitleText(state, searchState, context)) }
    return subtitle
}

private fun createSubtitleText(
    state: AppsListScreenState,
    searchState: AppsSearchState,
    context: Context
): AnnotatedString {
    return if (searchState.isActive) {
        val resultsSize = searchState.results.size
        if (searchState.query.isBlank()) {
            AnnotatedString("")
        } else {
            val countLabel = if (resultsSize > 0) {
                resultsSize.toString()
            } else {
                context.getString(AppStrings.lbl_no)
            }
            createCountLabelAnnotatedString(
                countLabel = countLabel,
                label = context.getString(AppStrings.lbl_apps_found)
            )
        }
    } else {
        val appsCount = state.apps.size
        val countLabel = if (appsCount > 0) {
            appsCount.toString()
        } else {
            context.getString(AppStrings.lbl_no)
        }
        createCountLabelAnnotatedString(
            countLabel = countLabel,
            label = state.listType.label.asString(context)
        )
    }
}

// /////////////////////////////////////////////////////////////////////////
// Test Tags
// /////////////////////////////////////////////////////////////////////////
const val TEST_TAG_APPS_LIST_SCREEN = "apps_list_screen"
const val TEST_TAG_APPS_LIST_LOADING = "apps_list_loading_view"
const val TEST_TAG_APPS_LIST_OPTIONS = "apps_list_options_bottom_sheet"
const val TEST_TAG_APPS_LIST = "apps_list"
const val TEST_TAG_APPS_GRID = "apps_grid"
const val TEST_TAG_APPS_LIST_DETAIL_CONTENT = "app_detail"
const val TEST_TAG_APPS_LIST_REFRESH_INDICATOR = "apps_list_refresh_indicator"
const val TEST_TAG_APPS_SEARCH_RESULTS = "apps_search_results"
