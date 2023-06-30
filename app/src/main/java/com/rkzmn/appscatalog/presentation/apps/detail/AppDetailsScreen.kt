package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.rkzmn.appscatalog.presentation.apps.detail.states.AppDetailsScreenState
import com.rkzmn.appscatalog.ui.widgets.ProgressView
import com.rkzmn.appscatalog.utils.android.compose.LocalWindowSize
import com.rkzmn.appscatalog.utils.app.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(
    state: AppDetailsScreenState,
    modifier: Modifier = Modifier,
    onNavIconClicked: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val windowSize = LocalWindowSize.current
    Scaffold(
        modifier = modifier,
        topBar = {
            val title: @Composable () -> Unit =
                { Text(text = stringResource(id = AppStrings.lbl_app_details)) }

            val navigationIcon: @Composable () -> Unit = {
                IconButton(onClick = onNavIconClicked) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = AppStrings.content_desc_back)
                    )
                }
            }

            if (windowSize.heightSizeClass > WindowHeightSizeClass.Compact) {
                LargeTopAppBar(
                    title = title,
                    navigationIcon = navigationIcon,
                    scrollBehavior = scrollBehavior,
                )
            } else {
                TopAppBar(
                    title = title,
                    navigationIcon = navigationIcon,
                    scrollBehavior = scrollBehavior,
                )
            }
        }
    ) { contentPadding ->
        AppDetailsContent(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@Composable
fun AppDetailsContent(
    state: AppDetailsScreenState,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            ProgressView(
                message = stringResource(id = AppStrings.lbl_loading),
                modifier = modifier
            )
        }

        state.details != null -> {
            AppDetailsUI(
                modifier = modifier,
                details = state.details,
            )
        }
    }
}
