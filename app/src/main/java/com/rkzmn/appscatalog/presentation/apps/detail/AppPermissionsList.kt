package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rkzmn.appscatalog.domain.model.AppPermissionInfo
import com.rkzmn.appscatalog.ui.theme.cardColorAndElevation
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium

@Composable
fun AppPermissionsList(
    modifier: Modifier = Modifier,
    permissions: List<AppPermissionInfo>,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        contentPadding = PaddingValues(spacingMedium),
    ) {
        items(
            items = permissions,
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
    modifier: Modifier = Modifier,
    permission: AppPermissionInfo,
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
                    text = permission.permission,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = permission.group.orEmpty(),
                    style = MaterialTheme.typography.bodySmall
                )

                if (!permission.description.isNullOrBlank()) {
                    Text(
                        text = permission.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

//            if (service.isPrivate) {
//                Icon(
//                    imageVector = Icons.Filled.Lock,
//                    contentDescription = service.name
//                )
//            }

        }


    }
}

///////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////