package com.rkzmn.appscatalog.navigation.composables

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rkzmn.appscatalog.navigation.NAV_TAG
import com.rkzmn.appscatalog.navigation.destination.AppsListDestination
import com.rkzmn.appscatalog.presentation.apps.AppsViewModel
import com.rkzmn.appscatalog.presentation.apps.list.AppListScreen
import timber.log.Timber

fun NavGraphBuilder.appsListScreenComposable(
    navHostController: NavHostController,
    viewModel: AppsViewModel,
) {
    composable(AppsListDestination.route) {
        Timber.tag(NAV_TAG).d("Navigating to AppsList Screen")

        val state by viewModel.appsListState.collectAsStateWithLifecycle()
        AppListScreen(
            state = state,
            onItemClicked = {}
        )

    }
}