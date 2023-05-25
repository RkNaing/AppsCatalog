package com.rkzmn.appscatalog.navigation.composables

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rkzmn.appscatalog.navigation.ARG_PACKAGE_NAME
import com.rkzmn.appscatalog.navigation.NAV_TAG
import com.rkzmn.appscatalog.navigation.destination.AppDetailDestination
import com.rkzmn.appscatalog.navigation.stringNavArgument
import com.rkzmn.appscatalog.presentation.apps.AppsViewModel
import com.rkzmn.appscatalog.presentation.apps.detail.AppDetailsScreen
import timber.log.Timber

fun NavGraphBuilder.appDetailsScreenComposable(
    navHostController: NavHostController,
    viewModel: AppsViewModel,
) {
    composable(
        route = AppDetailDestination.route,
        arguments = listOf(
            stringNavArgument(key = ARG_PACKAGE_NAME, isNullable = false, default = "")
        )
    ) { backStackEntry ->
        val packageName = backStackEntry.arguments?.getString(ARG_PACKAGE_NAME)
        Timber.tag(NAV_TAG).d("Navigating to App Details Screen for package $packageName")

        val appDetailsScreenState by viewModel.appDetailsScreenState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = packageName) {
            if (!packageName.isNullOrBlank()) {
                viewModel.loadAppDetails(packageName)
            }
        }

        AppDetailsScreen(
            state = appDetailsScreenState,
            onNavIconClicked = { navHostController.popBackStack() }
        )
    }
}
