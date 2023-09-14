package com.rkzmn.appscatalog.presentation.apps.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.ui.theme.spacingExtraLarge
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import com.rkzmn.appscatalog.utils.app.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsListOptionsBottomSheet(
    onDismissRequest: () -> Unit,
    appsType: AppsListType,
    onSelectAppListType: (AppsListType) -> Unit,
    displayType: AppsDisplayType,
    onSelectDisplayType: (AppsDisplayType) -> Unit,
    selectedSortOption: AppSortOption,
    onSelectSortOption: (AppSortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        AppsListOptions(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState()),
            appsType = appsType,
            onSelectAppListType = onSelectAppListType,
            displayType = displayType,
            onSelectDisplayType = onSelectDisplayType,
            selectedSortOption = selectedSortOption,
            onSelectSortOption = onSelectSortOption
        )

        Spacer(modifier = Modifier.height(spacingExtraLarge))
    }
}

@Composable
fun AppsListOptions(
    appsType: AppsListType,
    onSelectAppListType: (AppsListType) -> Unit,
    displayType: AppsDisplayType,
    onSelectDisplayType: (AppsDisplayType) -> Unit,
    selectedSortOption: AppSortOption,
    onSelectSortOption: (AppSortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sortFilters = AppSortFilter.filters
    val displayTypes = AppsDisplayType.values()
    val listTypes = AppsListType.values()

    Column(
        modifier = modifier.padding(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingSmall)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = AppStrings.lbl_options_filter),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Divider(modifier = Modifier.padding(vertical = spacingSmall))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacingSmall),
            text = stringResource(id = AppStrings.lbl_app_type),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )

        AppListTypeOptions(
            listTypes = listTypes,
            appsType = appsType,
            onSelectAppListType = onSelectAppListType
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacingSmall),
            text = stringResource(id = AppStrings.lbl_app_display_type),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )

        AppDisplayTypeOptions(
            displayTypes = displayTypes,
            displayType = displayType,
            onSelectDisplayType = onSelectDisplayType
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacingSmall),
            text = stringResource(id = AppStrings.lbl_sort_by),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )

        AppSortOptions(
            sortFilters = sortFilters,
            selectedSortOption = selectedSortOption,
            onSelectSortOption = onSelectSortOption
        )
    }
}

@Composable
private fun AppSortOptions(
    sortFilters: Array<AppSortFilter>,
    selectedSortOption: AppSortOption,
    onSelectSortOption: (AppSortOption) -> Unit
) {
    sortFilters.forEach { filter ->
        SortFilterRow(
            modifier = Modifier.fillMaxWidth(),
            filter = filter,
            selectedOption = selectedSortOption,
            onSelected = onSelectSortOption
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
private fun AppDisplayTypeOptions(
    displayTypes: Array<AppsDisplayType>,
    displayType: AppsDisplayType,
    onSelectDisplayType: (AppsDisplayType) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        displayTypes.forEach { type ->

            val label = type.label.asString()

            FilterChip(
                modifier = Modifier.testTag(label),
                selected = displayType == type,
                label = {
                    Text(text = label)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = type.icon),
                        contentDescription = label
                    )
                },
                onClick = { onSelectDisplayType(type) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
private fun AppListTypeOptions(
    listTypes: Array<AppsListType>,
    appsType: AppsListType,
    onSelectAppListType: (AppsListType) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        listTypes.forEach { type ->
            FilterChip(
                selected = appsType == type,
                label = {
                    Text(text = type.label.asString())
                },
                onClick = { onSelectAppListType(type) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SortFilterRow(
    filter: AppSortFilter,
    selectedOption: AppSortOption?,
    onSelected: (AppSortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        val label = filter.label.asString()

        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
                .padding(end = spacingExtraSmall),
            painter = painterResource(id = filter.icon),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier.align(Alignment.CenterVertically).padding(end = spacingMedium),
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )

        filter.options.forEach { option ->

            FilterChip(
                selected = selectedOption == option,
                label = { Text(text = option.label.asString()) },
                onClick = { onSelected(option) }
            )
        }
    }
}

// /////////////////////////////////////////////////////////////////////////
// Previews
// /////////////////////////////////////////////////////////////////////////

@UiModePreviews
@Composable
private fun AppsListOptionsPreview() {
    ThemedPreview {
        AppsListOptions(
            modifier = Modifier.fillMaxWidth(),
            appsType = AppsListType.ALL,
            onSelectAppListType = {},
            displayType = AppsDisplayType.LIST,
            onSelectDisplayType = {},
            selectedSortOption = AppSortOption.NAME_ASC,
            onSelectSortOption = {},
        )
    }
}
