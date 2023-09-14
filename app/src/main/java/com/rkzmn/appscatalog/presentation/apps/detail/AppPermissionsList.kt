package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.rkzmn.appscatalog.domain.model.AppPermissionInfo
import com.rkzmn.appscatalog.ui.theme.cardColorAndElevation
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AppPermissionsList(
    permissions: ImmutableList<AppPermissionInfo>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        contentPadding = PaddingValues(spacingMedium),
    ) {
        items(
            items = permissions,
            key = { it.permission }
        ) { permission ->
            ItemPermission(
                permission = permission,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ItemPermission(
    permission: AppPermissionInfo,
    modifier: Modifier = Modifier,
) {
    val cardColorsElevation = cardColorAndElevation

    Card(
        modifier = modifier,
        colors = cardColorsElevation.first,
        elevation = cardColorsElevation.second,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
            verticalArrangement = Arrangement.spacedBy(spacingExtraSmall),
        ) {
            Text(
                text = permission.permission,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (permission.isDangerous) MaterialTheme.colorScheme.error else Color.Unspecified
            )

            if (!permission.group.isNullOrBlank()) {
                Text(
                    text = permission.group,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }

            if (!permission.description.isNullOrBlank()) {
                Text(
                    modifier = Modifier.padding(vertical = spacingSmall),
                    text = permission.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@UiModePreviews
@Composable
private fun AppPermissionsListPreview() {
    val permissions = persistentListOf(
        AppPermissionInfo(
            permission = "android.permission.ACCESS_COARSE_LOCATION",
            group = "android.permission-group.UNDEFINED",
            description = "This app can get you approximate location from " +
                "locations services while the app is in use. " +
                "Location services for your device must be turned on " +
                "for the app to get location.",
            isDangerous = true
        ),
        AppPermissionInfo(
            permission = "android.permission.LOCATION_BYPASS",
            group = null,
            description = null,
            isDangerous = false
        ),
    )

    ThemedPreview {
        AppPermissionsList(permissions = permissions, modifier = Modifier.fillMaxSize())
    }
}
