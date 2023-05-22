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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.rkzmn.appscatalog.presentation.apps.detail.states.AppDetailsScreenState
import com.rkzmn.appscatalog.ui.widgets.ProgressView
import com.rkzmn.appscatalog.utils.app.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(
    state: AppDetailsScreenState,
    onNavIconClicked: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = AppStrings.lbl_app_details))
                },
                navigationIcon = {
                    IconButton(onClick = onNavIconClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = AppStrings.content_desc_back
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { contentPadding ->
        when {
            state.isLoading -> {
                ProgressView(
                    message = stringResource(id = AppStrings.lbl_loading),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                )
            }

            state.details != null -> {
                AppDetailsUI(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    details = state.details,
                )
            }
        }
    }
}