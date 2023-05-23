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
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.ui.theme.cardColorAndElevation
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews

@Composable
fun AppBroadcastReceiversList(
    modifier: Modifier = Modifier,
    receivers: List<AppComponentInfo>,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        contentPadding = PaddingValues(spacingMedium),
    ) {
        items(
            items = receivers,
            key = { it.fqn },
        ) { receiver ->
            ItemBroadcastReceiver(
                modifier = Modifier.fillMaxWidth(),
                receiver = receiver,
            )
        }
    }
}

@Composable
private fun ItemBroadcastReceiver(
    modifier: Modifier = Modifier,
    receiver: AppComponentInfo,
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
                    text = receiver.name,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = receiver.fqn,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (receiver.isPrivate) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = receiver.name
                )
            }

        }


    }
}

///////////////////////////////////////////////////////////////////////////
// Previews
///////////////////////////////////////////////////////////////////////////
@UiModePreviews
@Composable
private fun ItemBroadcastReceiverPreview() {
    ThemedPreview {
        ItemBroadcastReceiver(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
            receiver = AppComponentInfo(
                name = "SampleBroadcastReceiver",
                packageName = "com.rkzmn.appscatalog",
                fqn = "com.rkzmn.appscatalog.SampleBroadcastReceiver",
                isPrivate = false
            ),
        )
    }
}