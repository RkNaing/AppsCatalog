package com.rkzmn.appscatalog.navigation.composables

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rkzmn.appscatalog.navigation.NAV_TAG
import com.rkzmn.appscatalog.navigation.destination.AppSettingsDestination
import com.rkzmn.appscatalog.presentation.settings.SettingsScreen
import com.rkzmn.appscatalog.presentation.settings.SettingsViewModel
import timber.log.Timber

fun NavGraphBuilder.appSettingsScreenComposable(
    navHostController: NavHostController,
) {
    composable(
        route = AppSettingsDestination.route,
    ) {
        Timber.tag(NAV_TAG).d("Navigating to App Settings Screen.")
        val viewModel = hiltViewModel<SettingsViewModel>()
        val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
        val useDynamicColor by viewModel.useDynamicColors.collectAsStateWithLifecycle()

        SettingsScreen(
            appTheme = appTheme,
            useDynamicColors = useDynamicColor,
            onNavIconClicked = { navHostController.popBackStack() },
            onThemeUpdated = { theme -> viewModel.updateAppTheme(theme) },
            updateUseDynamicColor = { viewModel.updateUseDynamicColors(it) }
        )
    }
}
