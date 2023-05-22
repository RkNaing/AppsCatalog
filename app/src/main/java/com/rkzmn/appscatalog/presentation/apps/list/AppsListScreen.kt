package com.rkzmn.appscatalog.presentation.apps.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType.*
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsListScreenState
import com.rkzmn.appscatalog.presentation.apps.options.AppsListOptionsBottomSheet
import com.rkzmn.appscatalog.ui.theme.seed
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.ProgressView
import com.rkzmn.appscatalog.utils.app.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    state: AppsListScreenState,
    onItemClicked: (AppItem) -> Unit,
    onSelectAppListType: (AppsListType) -> Unit,
    onSelectDisplayType: (AppsDisplayType) -> Unit,
    onSelectSortOption: (AppSortOption) -> Unit,
    onClickedSettings: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val subtitle by remember(key1 = state.listType, key2 = state.apps.size) {
        val appsCountsLabel = "${state.apps.size}"
        val appsTypeLabel = state.listType.label.asString(context)
        val annotatedString = buildAnnotatedString {
            append(appsCountsLabel)
            append(" ")
            append(appsTypeLabel)
            addStyle(
                SpanStyle(
                    color = seed,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                ),
                start = 0,
                end = appsCountsLabel.length
            )
        }
        mutableStateOf(annotatedString)
    }
    var showFilterDialog by remember { mutableStateOf(false) }

    if (showFilterDialog) {
        AppsListOptionsBottomSheet(
            onDismissRequest = { showFilterDialog = false },
            appsType = state.listType,
            onSelectAppListType = onSelectAppListType,
            displayType = state.listDisplayType,
            onSelectDisplayType = onSelectDisplayType,
            selectedSortOption = state.sortBy,
            onSelectSortOption = onSelectSortOption,
        )
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(spacingSmall)
                    ) {
                        Text(text = stringResource(id = AppStrings.app_name))

                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = stringResource(id = AppStrings.content_desc_tune_app_list),
                            tint = seed
                        )
                    }
                    IconButton(onClick = { onClickedSettings() }) {
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
    ) { contentPadding ->
        if (state.isLoading) {
            ProgressView(
                message = stringResource(id = AppStrings.lbl_loading),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        } else {
            when (state.listDisplayType) {
                LIST -> AppsList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    apps = state.apps,
                    onItemClicked = onItemClicked,
                )

                GRID -> AppsGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    apps = state.apps,
                    onItemClicked = onItemClicked,
                )
            }
        }

    }
}