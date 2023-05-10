package com.rkzmn.appscatalog.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rkzmn.appscatalog.navigation.composables.appsListScreenComposable
import com.rkzmn.appscatalog.navigation.destination.AppsListDestination
import com.rkzmn.appscatalog.presentation.apps.AppsViewModel

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
) {
    val viewModel: AppsViewModel = hiltViewModel()

    NavHost(
        navController = navHostController,
        startDestination = AppsListDestination.route,
        route = ROOT_NAV_GRAPH
    ) {
        appsListScreenComposable(
            navHostController = navHostController,
            viewModel = viewModel
        )
    }
}

const val ROOT_NAV_GRAPH = "RootNavGraph"