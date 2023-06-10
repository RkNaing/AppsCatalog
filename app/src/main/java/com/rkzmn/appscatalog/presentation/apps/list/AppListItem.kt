package com.rkzmn.appscatalog.presentation.apps.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rkzmn.appscatalog.ui.theme.appIconSize
import com.rkzmn.appscatalog.ui.theme.isAppInDarkTheme
import com.rkzmn.appscatalog.ui.theme.seed
import com.rkzmn.appscatalog.ui.theme.spacingExtraSmall
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.AppIcon
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import com.rkzmn.appscatalog.utils.app.getHighlightedMatchingText

@Composable
fun AppGridItem(
    appItem: AppItem,
    modifier: Modifier = Modifier,
    onClicked: (String) -> Unit,
) {
    AppItemContainer(
        modifier = modifier,
        onClick = { onClicked(appItem.packageName) },
        isSelected = appItem.isSelected,
    ) {
        AppIcon(
            iconPath = appItem.appIcon,
            contentDescription = appItem.appName,
            modifier = Modifier
                .padding(spacingMedium)
                .size(appIconSize)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = appItem.appName ?: appItem.packageName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(
                    start = spacingMedium,
                    end = spacingMedium,
                    bottom = spacingMedium
                )
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AppListItem(
    appItem: AppItem,
    modifier: Modifier = Modifier,
    onClicked: (String) -> Unit,
) {
    AppItemContainer(
        modifier = modifier,
        onClick = { onClicked(appItem.packageName) },
        isSelected = appItem.isSelected,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AppIcon(
                iconPath = appItem.appIcon,
                contentDescription = appItem.appName,
                modifier = Modifier
                    .padding(spacingMedium)
                    .size(appIconSize)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = spacingMedium),
                verticalArrangement = Arrangement.spacedBy(spacingSmall)
            ) {
                if (!appItem.appName.isNullOrBlank()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = spacingMedium),
                        text = appItem.appName,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = spacingMedium),
                    text = appItem.packageName,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = spacingMedium),
                    text = appItem.version,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!appItem.readableSize.isNullOrBlank()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = spacingMedium),
                        text = appItem.readableSize,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Indicators
            if (appItem.appTypeIndicators.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(spacingSmall)
                        .align(Alignment.Bottom),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    appItem.appTypeIndicators.forEach { indicator ->
                        Icon(
                            modifier = Modifier
                                .padding(spacingExtraSmall)
                                .size(24.dp),
                            painter = painterResource(id = indicator.icon),
                            contentDescription = indicator.contentDescription.asString(),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppSearchResultItem(
    appItem: AppSearchResult,
    modifier: Modifier = Modifier,
    onClicked: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClicked(appItem.packageName.text) }
            .padding(
                horizontal = spacingMedium,
                vertical = spacingSmall
            ),
    ) {
        AppIcon(
            iconPath = appItem.appIcon,
            contentDescription = appItem.appName?.text,
            modifier = Modifier
                .padding(spacingMedium)
                .size(appIconSize)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = spacingMedium),
            verticalArrangement = Arrangement.spacedBy(spacingSmall)
        ) {
            if (!appItem.appName.isNullOrBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = spacingMedium),
                    text = appItem.appName,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = spacingMedium),
                text = appItem.packageName,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppItemContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardColors: CardColors
    val cardElevation: CardElevation

    if (isAppInDarkTheme) {
        cardColors = CardDefaults.cardColors()
        cardElevation = CardDefaults.cardElevation()
    } else {
        cardColors = CardDefaults.elevatedCardColors()
        cardElevation = CardDefaults.elevatedCardElevation()
    }

    Card(
        modifier = modifier,
        onClick = onClick,
        border = if (isSelected) BorderStroke(1.dp, color = seed) else null,
        colors = cardColors,
        elevation = cardElevation,
        content = content
    )
}

val appGridItemMinSize = 85.dp

const val ITEM_TYPE_APP = "AppItem"

// /////////////////////////////////////////////////////////////////////////
// Previews
// /////////////////////////////////////////////////////////////////////////
@UiModePreviews
@Composable
private fun AppGridItemPreview() {
    ThemedPreview {
        AppGridItem(
            appItem = AppItem(
                appName = "Sample App",
                packageName = "com.rkzmn.appscatalog",
                isSelected = true,
            ),
            onClicked = {}
        )
    }
}

@UiModePreviews
@Composable
private fun AppListItemPreview() {
    ThemedPreview {
        AppListItem(
            appItem = AppItem(
                appName = "Sample App",
                packageName = "com.rkzmn.appscatalog",
                version = "1.0.0 (23)",
                isSelected = true,
            ),
            onClicked = {}
        )
    }
}

@UiModePreviews
@Composable
private fun AppSearchResultItemPreview() {
    ThemedPreview {
        AppSearchResultItem(
            appItem = AppSearchResult(
                appName = getHighlightedMatchingText("Apps Catalog", "Catalog"),
                packageName = getHighlightedMatchingText("com.rkzmn.appscatalog", "rkzmn")
            ),
            onClicked = {}
        )
    }
}
