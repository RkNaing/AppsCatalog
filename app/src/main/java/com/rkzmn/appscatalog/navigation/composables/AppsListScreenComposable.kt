package com.rkzmn.appscatalog.navigation.composables

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.rkzmn.appscatalog.navigation.NAV_TAG
import com.rkzmn.appscatalog.navigation.destination.AppDetailDestination
import com.rkzmn.appscatalog.navigation.destination.AppSettingsDestination
import com.rkzmn.appscatalog.navigation.destination.AppsListDestination
import com.rkzmn.appscatalog.presentation.apps.AppsViewModel
import com.rkzmn.appscatalog.presentation.apps.list.AppListScreen
import com.rkzmn.appscatalog.utils.android.compose.LocalWindowSize
import timber.log.Timber

fun NavGraphBuilder.appsListScreenComposable(
    navHostController: NavHostController,
    viewModel: AppsViewModel,
) {
    composable(AppsListDestination.route) {
        Timber.tag(NAV_TAG).d("Navigating to AppsList Screen")

        val windowSize = LocalWindowSize.current

        val state by viewModel.appsListState.collectAsStateWithLifecycle()
        val searchState by viewModel.appsSearchState.collectAsStateWithLifecycle()
        val appDetailsState by viewModel.appDetailsScreenState.collectAsStateWithLifecycle()
        val onItemClicked = rememberAppItemClickHandler(
            navHostController = navHostController,
            windowSize = windowSize,
            viewModel = viewModel
        )

        LaunchedEffect(key1 = windowSize) {
            if (windowSize.widthSizeClass != WindowWidthSizeClass.Expanded) {
                viewModel.clearSelection()
            }
        }

        AppListScreen(
            state = state,
            searchState = searchState,
            windowSize = windowSize,
            onItemClicked = onItemClicked,
            onSelectAppListType = { viewModel.onSelectListType(it) },
            onSelectDisplayType = { viewModel.onSelectDisplayType(it) },
            onSelectSortOption = { viewModel.onSelectSortOption(it) },
            onClickedSettings = { navHostController.navigate(AppSettingsDestination.route) },
            onSearchQueryChanged = { viewModel.onSearchQueryChange(it) },
            onSearchStatusChanged = { viewModel.onSearchStatusChange(it) },
            appDetailsProvider = { appDetailsState },
            onSearchResultItemClicked = { packageName ->
                navHostController.navigate(
                    route = AppDetailDestination.getAddress(packageName),
                    navOptions = navOptions { launchSingleTop = true }
                )
            },
            onRefresh = { viewModel.refreshAppsList() }
        )
    }
}

@Composable
private fun rememberAppItemClickHandler(
    navHostController: NavHostController,
    windowSize: WindowSizeClass,
    viewModel: AppsViewModel
): (String) -> Unit = remember(windowSize, viewModel) {
    if (
        windowSize.widthSizeClass == WindowWidthSizeClass.Expanded &&
        windowSize.heightSizeClass > WindowHeightSizeClass.Compact
    ) {
        { packageName ->
            with(viewModel) {
                onAppSelected(packageName)
                loadAppDetails(packageName)
            }
        }
    } else {
        { packageName ->
            navHostController.navigate(
                route = AppDetailDestination.getAddress(packageName),
                navOptions = navOptions { launchSingleTop = true }
            )
        }
    }
}
