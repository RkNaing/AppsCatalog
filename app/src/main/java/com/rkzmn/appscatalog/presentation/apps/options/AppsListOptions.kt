package com.rkzmn.appscatalog.presentation.apps.options

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        AppsListOptions(
            modifier = Modifier.fillMaxWidth(),
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppsListOptions(
    modifier: Modifier = Modifier,
    appsType: AppsListType,
    onSelectAppListType: (AppsListType) -> Unit,
    displayType: AppsDisplayType,
    onSelectDisplayType: (AppsDisplayType) -> Unit,
    selectedSortOption: AppSortOption,
    onSelectSortOption: (AppSortOption) -> Unit,
) {

    val sortFilters = AppSortFilter.filters
    val displayTypes = AppsDisplayType.values()
    val listTypes = AppsListType.values()

    val appTypeInteractionSource = remember { MutableInteractionSource() }
    val displayTypeInteractionSource = remember { MutableInteractionSource() }
    val sortInteractionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.padding(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacingMedium),
            text = stringResource(id = AppStrings.lbl_options_filter),
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacingMedium),
            text = stringResource(id = AppStrings.lbl_app_type),
            style = MaterialTheme.typography.titleSmall,
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {

            listTypes.forEach { type ->
                FilterChip(
                    interactionSource = appTypeInteractionSource,
                    selected = appsType == type,
                    label = {
                        Text(text = type.label.asString())
                    },
                    onClick = { onSelectAppListType(type) }
                )
            }

        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacingMedium),
            text = stringResource(id = AppStrings.lbl_app_display_type),
            style = MaterialTheme.typography.titleSmall,
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {

            displayTypes.forEach { type ->

                val label = type.label.asString()

                FilterChip(
                    interactionSource = displayTypeInteractionSource,
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

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacingMedium),
            text = stringResource(id = AppStrings.lbl_sort_by),
            style = MaterialTheme.typography.titleSmall,
        )

        sortFilters.forEach { filter ->
            SortFilterRow(
                modifier = Modifier.fillMaxWidth(),
                interactionSource = sortInteractionSource,
                filter = filter,
                selectedOption = selectedSortOption,
                onSelected = onSelectSortOption
            )
        }

    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SortFilterRow(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    filter: AppSortFilter,
    selectedOption: AppSortOption?,
    onSelected: (AppSortOption) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {

        val label = filter.label.asString()

        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(end = spacingExtraSmall),
            painter = painterResource(id = filter.icon),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier.padding(end = spacingMedium),
            text = label,
            style = MaterialTheme.typography.titleSmall,
        )

        filter.options.forEach { option ->

            FilterChip(
                interactionSource = interactionSource,
                selected = selectedOption == option,
                label = { Text(text = option.label.asString()) },
                onClick = { onSelected(option) }
            )

        }

    }
}

///////////////////////////////////////////////////////////////////////////
// Previews
///////////////////////////////////////////////////////////////////////////

@UiModePreviews
@Composable
private fun AppsListOptionsPreview() {
    ThemedPreview {
        AppsListOptionsBottomSheet(
//            modifier = Modifier.fillMaxWidth(),
            appsType = AppsListType.ALL,
            onSelectAppListType = {},
            displayType = AppsDisplayType.LIST,
            onSelectDisplayType = {},
            selectedSortOption = AppSortOption.NAME_ASC,
            onSelectSortOption = {},
            onDismissRequest = {}
        )
    }
}