package com.rkzmn.appscatalog.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.rkzmn.appscatalog.domain.model.AppTheme
import com.rkzmn.appscatalog.ui.theme.cardColorAndElevation
import com.rkzmn.appscatalog.ui.theme.spacingLarge
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.AndroidVersions
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import com.rkzmn.appscatalog.utils.android.isSDKIntAtLeast
import com.rkzmn.appscatalog.utils.app.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    appTheme: AppTheme,
    useDynamicColors: Boolean,
    onNavIconClicked: () -> Unit,
    onThemeUpdated: (AppTheme) -> Unit,
    updateUseDynamicColor: (Boolean) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = AppStrings.lbl_settings))
                },
                navigationIcon = {
                    IconButton(onClick = onNavIconClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = AppStrings.content_desc_back
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(horizontal = spacingLarge)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(spacingMedium),
        ) {

            AppThemeChooserCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacingMedium),
                appTheme = appTheme,
                useDynamicColors = useDynamicColors,
                onThemeUpdated = onThemeUpdated,
                updateUseDynamicColor = updateUseDynamicColor
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun AppThemeChooserCard(
    modifier: Modifier,
    appTheme: AppTheme,
    useDynamicColors: Boolean,
    onThemeUpdated: (AppTheme) -> Unit,
    updateUseDynamicColor: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val cardColorsAndElevation = cardColorAndElevation

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { },
        colors = cardColorsAndElevation.first,
        elevation = cardColorsAndElevation.second,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
            verticalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {

            Text(
                text = stringResource(id = AppStrings.lbl_app_theme),
                style = MaterialTheme.typography.labelLarge
            )

            FlowRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
            ) {
                AppTheme.values().forEach { theme ->
                    FilterChip(
                        interactionSource = interactionSource,
                        selected = theme == appTheme,
                        label = {
                            Text(text = theme.label.asString())
                        },
                        onClick = { onThemeUpdated(theme) }
                    )
                }
            }

            if (isSDKIntAtLeast(AndroidVersions.S)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(spacingSmall))
                        .clickable { updateUseDynamicColor(!useDynamicColors) }
                        .padding(spacingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = AppStrings.lbl_use_dynamic_color),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Switch(
                        modifier = Modifier.padding(horizontal = spacingSmall),
                        checked = useDynamicColors,
                        onCheckedChange = null,
                    )

                }

            }

        }

    }
}


@UiModePreviews
@Composable
fun SettingsScreenPreview() {
    ThemedPreview {
        SettingsScreen(
            appTheme = AppTheme.DARK,
            useDynamicColors = true,
            onNavIconClicked = { },
            onThemeUpdated = {},
            updateUseDynamicColor = {}
        )
    }
}