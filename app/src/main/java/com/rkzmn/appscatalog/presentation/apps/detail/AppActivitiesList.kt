package com.rkzmn.appscatalog.presentation.apps.detail

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.ui.theme.cardColorAndElevation
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import com.rkzmn.appscatalog.utils.app.launch
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AppActivitiesList(
    activities: ImmutableList<AppComponentInfo>,
    modifier: Modifier = Modifier,
) {
    val activity = LocalView.current.context as? Activity

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        contentPadding = PaddingValues(spacingMedium),
    ) {
        items(
            items = activities,
            key = { it.fqn },
        ) { appComponentInfo ->
            ItemActivity(
                modifier = Modifier.fillMaxWidth(),
                activity = appComponentInfo,
                onItemClicked = { componentInfo ->
                    activity?.let { componentInfo.launch(it) }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemActivity(
    activity: AppComponentInfo,
    modifier: Modifier = Modifier,
    onItemClicked: (AppComponentInfo) -> Unit,
) {
    val cardColorsElevation = cardColorAndElevation

    Card(
        modifier = modifier,
        onClick = { onItemClicked(activity) },
        colors = cardColorsElevation.first,
        elevation = cardColorsElevation.second,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacingExtraSmall),
            ) {
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = activity.fqn,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (!activity.isPrivate) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = activity.name
                )
            }
        }
    }
}

// /////////////////////////////////////////////////////////////////////////
// Previews
// /////////////////////////////////////////////////////////////////////////
@UiModePreviews
@Composable
private fun ItemActivityPreview() {
    ThemedPreview {
        ItemActivity(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
            activity = AppComponentInfo(
                name = "MainActivity",
                packageName = "com.rkzmn.appscatalog",
                fqn = "com.rkzmn.appscatalog.MainActivity",
                isPrivate = false
            ),
            onItemClicked = {}
        )
    }
}
