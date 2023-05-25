package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rkzmn.appscatalog.domain.model.AppDetails
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import com.rkzmn.appscatalog.ui.theme.spacingLarge
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import com.rkzmn.appscatalog.utils.app.AppStrings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun AppInfoContainer(
    details: AppDetails,
    modifier: Modifier = Modifier,
) {
    val appInfoData = remember(key1 = details) {
        mutableStateMapOf<Int, String>().apply { putAll(details.infoDataMap) }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingLarge)
    ) {
        val indicators = details.appTypeIndicators
        if (indicators.isNotEmpty()) {
            AppIndicators(
                indicators = indicators,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        appInfoData.forEach { (labelRes, value) ->
            InfoTile(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = labelRes),
                message = value
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun AppIndicators(
    indicators: ImmutableList<AppTypeIndicator>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        indicators.forEach { indicator ->
            val label = indicator.contentDescription.asString()
            SuggestionChip(
                onClick = { },
                interactionSource = remember { MutableInteractionSource() },
                label = { Text(text = label) },
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = indicator.icon),
                        contentDescription = label
                    )
                }
            )
        }
    }
}

private val AppDetails.infoDataMap: ImmutableMap<Int, String>
    get() {
        val data = mapOf(
            AppStrings.lbl_package_name to packageName,
            AppStrings.lbl_version_name to versionName,
            AppStrings.lbl_version_code to versionCode.takeIf { it > 0 }?.toString(),
            AppStrings.lbl_app_size to appSize,
            AppStrings.lbl_min_android_version to minAndroidVersion,
            AppStrings.lbl_target_android_version to targetAndroidVersion,
            AppStrings.lbl_compile_android_version to compileSdkAndroidVersion,
            AppStrings.lbl_install_source to installationSource,
            AppStrings.lbl_installed_at to installedTimestamp,
            AppStrings.lbl_last_updated_at to lastUpdatedTimestamp,
            AppStrings.lbl_last_used_at to lastUsedTimestamp,
        )
        val filteredData = mutableMapOf<Int, String>()
        data.forEach { (k, v) ->
            if (!v.isNullOrBlank()) {
                filteredData[k] = v
            }
        }
        return filteredData.toImmutableMap()
    }

@Composable
private fun InfoTile(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingSmall)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// /////////////////////////////////////////////////////////////////////////
// Previews
// /////////////////////////////////////////////////////////////////////////

@UiModePreviews
@Composable
private fun AppInfoContainerPreview() {
    val dummyAppDetails = AppDetails(
        appName = "App Catalog",
        appIcon = null,
        versionCode = 7425,
        versionName = "1.0.2",
        packageName = "com.rkzmn.appscatalog",
        appTypeIndicators = persistentListOf(
            AppTypeIndicator.installed,
            AppTypeIndicator.debuggable
        ),
        installationSource = "Android System",
        installedTimestamp = "03 March 2022, 10:15:28 AM",
        lastUpdatedTimestamp = "07 May 2023, 11:19:31 PM",
        lastUsedTimestamp = "07 May 2023, 11:43:36 PM",
        appSize = "15.6 MB",
        minAndroidVersion = "Lollipop (22)",
        targetAndroidVersion = "Android 13",
        compileSdkAndroidVersion = "Android 13",
        activities = persistentListOf(),
        services = persistentListOf(),
        broadcastReceivers = persistentListOf(),
        permissions = persistentListOf()
    )
    ThemedPreview {
        AppInfoContainer(modifier = Modifier.fillMaxSize(), details = dummyAppDetails)
    }
}
