package com.rkzmn.appscatalog.navigation.composables

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rkzmn.appscatalog.navigation.ARG_PACKAGE_NAME
import com.rkzmn.appscatalog.navigation.NAV_TAG
import com.rkzmn.appscatalog.navigation.destination.AppDetailDestination
import com.rkzmn.appscatalog.navigation.stringNavArgument
import com.rkzmn.appscatalog.presentation.apps.detail.AppDetailsScreen
import com.rkzmn.appscatalog.presentation.apps.detail.AppDetailsViewModel
import timber.log.Timber

fun NavGraphBuilder.appDetailsScreenComposable(
    navHostController: NavHostController,
) {
    composable(
        route = AppDetailDestination.route,
        arguments = listOf(
            stringNavArgument(key = ARG_PACKAGE_NAME, isNullable = false, default = "")
        )
    ) { backStackEntry ->
        val packageName = backStackEntry.arguments?.getString(ARG_PACKAGE_NAME)
        Timber.tag(NAV_TAG).d("Navigating to App Details Screen for package $packageName")

        val viewModel: AppDetailsViewModel = hiltViewModel()
        val appDetailsScreenState by viewModel.appDetailsScreenState.collectAsStateWithLifecycle()

        AppDetailsScreen(
            state = appDetailsScreenState,
            onNavIconClicked = { navHostController.popBackStack() }
        )
    }
}
