package com.rkzmn.appscatalog.presentation.apps.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

@Composable
fun AppsGrid(
    apps: ImmutableList<AppItem>,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    onItemClicked: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = appGridItemMinSize),
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        horizontalArrangement = Arrangement.spacedBy(spacingMedium, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(spacingMedium),
        state = state,
    ) {
        items(
            items = apps,
            key = { it.packageName },
            contentType = { ITEM_TYPE_APP }
        ) { appItem ->
            AppGridItem(
                appItem = appItem,
                onClicked = onItemClicked,
            )
        }
    }
}

@Composable
fun AppsList(
    apps: ImmutableList<AppItem>,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        contentPadding = PaddingValues(spacingMedium),
        state = state,
    ) {
        items(
            items = apps,
            key = { it.packageName },
            contentType = { ITEM_TYPE_APP }
        ) { appItem ->
            AppListItem(
                appItem = appItem,
                onClicked = onItemClicked,
            )
        }
    }
}

@Composable
fun AppsSearchResultsList(
    results: ImmutableList<AppSearchResult>,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        contentPadding = PaddingValues(spacingMedium),
        state = state,
    ) {
        itemsIndexed(
            items = results,
            key = { _, item -> item.packageName.text },
            contentType = { _, _ -> ITEM_TYPE_APP }
        ) { index, appItem ->

            AppSearchResultItem(
                appItem = appItem,
                onClicked = onItemClicked,
            )

            if (index < results.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

// /////////////////////////////////////////////////////////////////////////
// Previews
// /////////////////////////////////////////////////////////////////////////
@UiModePreviews
@Composable
fun AppsGridPreview() {
    ThemedPreview {
        var selectedApp by remember {
            mutableStateOf("")
        }
        val apps = remember(sampleAppItems::toMutableStateList)

        AppsGrid(
            modifier = Modifier.fillMaxSize(),
            apps = apps.toImmutableList(),
            onItemClicked = { selectedApp = it },
        )
    }
}

@UiModePreviews
@Composable
fun AppsListPreview() {
    ThemedPreview {
        var selectedApp by remember { mutableStateOf("") }

        val apps = remember(sampleAppItems::toMutableStateList)

        AppsList(
            modifier = Modifier.fillMaxSize(),
            apps = apps.toImmutableList(),
            onItemClicked = { selectedApp = it },
        )
    }
}

private val sampleAppItems: List<AppItem>
    get() {
        val sampleApps = mutableListOf<AppItem>()
        val random = Random.Default

        for (i in 1..15) {

            val appTypeIndicators = mutableListOf<AppTypeIndicator>()

            if (random.nextBoolean()) {
                appTypeIndicators += AppTypeIndicator.system
            }

            if (random.nextBoolean()) {
                appTypeIndicators += AppTypeIndicator.debuggable
            }

            sampleApps += AppItem(
                appName = "Sample App $i",
                version = "Version 1.$i (${i + 1})",
                packageName = "com.sample.app.$i",
                appTypeIndicators = appTypeIndicators.toImmutableList(),
            )
        }
        return sampleApps.toList()
    }
