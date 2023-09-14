package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.ui.theme.cardColorAndElevation
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AppServicesList(
    services: ImmutableList<AppComponentInfo>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        contentPadding = PaddingValues(spacingMedium),
    ) {
        items(
            items = services,
            key = { it.fqn },
        ) { service ->
            ItemService(
                modifier = Modifier.fillMaxWidth(),
                service = service,
            )
        }
    }
}

@Composable
private fun ItemService(
    service: AppComponentInfo,
    modifier: Modifier = Modifier,
) {
    val cardColorsElevation = cardColorAndElevation

    Card(
        modifier = modifier,
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
                    text = service.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = service.fqn,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (service.isPrivate) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = service.name
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
        ItemService(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
            service = AppComponentInfo(
                name = "SampleService",
                packageName = "com.rkzmn.appscatalog",
                fqn = "com.rkzmn.appscatalog.SampleService",
                isPrivate = false
            ),
        )
    }
}
