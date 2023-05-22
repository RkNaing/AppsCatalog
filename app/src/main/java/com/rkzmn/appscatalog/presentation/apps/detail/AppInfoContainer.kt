package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rkzmn.appscatalog.domain.model.AppDetails
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import com.rkzmn.appscatalog.ui.theme.appIconSize
import com.rkzmn.appscatalog.ui.theme.spacingLarge
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.AppIcon
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import com.rkzmn.appscatalog.utils.app.AppStrings


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppInfoContainer(
    modifier: Modifier,
    details: AppDetails,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingLarge)
    ) {

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            AppIcon(
//                iconPath = details.appIcon,
//                contentDescription = details.appName,
//                modifier = Modifier
//                    .padding(spacingMedium)
//                    .size(appIconSize)
//            )
//            if (!details.appName.isNullOrBlank()) {
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(end = spacingMedium),
//                    text = details.appName,
//                    maxLines = 2,
//                    style = MaterialTheme.typography.headlineSmall
//                )
//            } else {
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(end = spacingMedium),
//                    text = details.packageName,
//                    maxLines = 2,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//
////            Column(
////                modifier = Modifier
////                    .weight(1f)
////                    .padding(vertical = spacingMedium),
////                verticalArrangement = Arrangement.spacedBy(spacingSmall)
////            ) {
////
////
//
////
//////                Text(
//////                    modifier = Modifier
//////                        .fillMaxWidth()
//////                        .padding(end = spacingMedium),
//////                    text = details.version,
//////                    style = MaterialTheme.typography.bodySmall,
//////                    color = MaterialTheme.colorScheme.onSurfaceVariant
//////                )
//////
//////                if (!appItem.readableSize.isNullOrBlank()) {
//////                    Text(
//////                        modifier = Modifier
//////                            .fillMaxWidth()
//////                            .padding(end = spacingMedium),
//////                        text = appItem.readableSize,
//////                        style = MaterialTheme.typography.bodySmall,
//////                        fontWeight = FontWeight.SemiBold,
//////                        color = MaterialTheme.colorScheme.onSurfaceVariant
//////                    )
//////                }
////            }
//
//        }

        val indicators = details.appTypeIndicators
        if (indicators.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                indicators.forEach { indicator ->
                    val label = indicator.contentDescription.asString()
                    SuggestionChip(
                        onClick = { /*TODO*/ },
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

        InfoTile(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(id = AppStrings.lbl_package_name),
            message = details.packageName
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            InfoTile(
                modifier = Modifier.weight(1f),
                title = stringResource(id = AppStrings.lbl_version_name),
                message = details.versionName ?: "-"
            )

            InfoTile(
                modifier = Modifier.weight(1f),
                title = stringResource(id = AppStrings.lbl_version_code),
                message = details.versionCode.toString()
            )

        }

        val appSize = details.appSize
        if (!appSize.isNullOrBlank()) {
            InfoTile(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = AppStrings.lbl_app_size),
                message = appSize
            )
        }

        InfoTile(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(id = AppStrings.lbl_min_android_version),
            message = details.minAndroidVersion
        )

        InfoTile(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(id = AppStrings.lbl_target_android_version),
            message = details.targetAndroidVersion
        )

        InfoTile(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(id = AppStrings.lbl_compile_android_version),
            message = details.compileSdkAndroidVersion
        )

        if (!details.installationSource.isNullOrBlank()) {
            InfoTile(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = AppStrings.lbl_install_source),
                message = details.installationSource
            )
        }

        if (!details.installedTimestamp.isNullOrBlank()) {
            InfoTile(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = AppStrings.lbl_installed_at),
                message = details.installedTimestamp
            )
        }

        if (!details.lastUpdatedTimestamp.isNullOrBlank()) {
            InfoTile(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = AppStrings.lbl_last_updated_at),
                message = details.lastUpdatedTimestamp
            )
        }

        if (!details.lastUsedTimestamp.isNullOrBlank()) {
            InfoTile(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = AppStrings.lbl_last_used_at),
                message = details.lastUsedTimestamp
            )
        }

    }
}

@Composable
private fun InfoTile(
    modifier: Modifier,
    title: String,
    message: String,
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


///////////////////////////////////////////////////////////////////////////
// Previews
///////////////////////////////////////////////////////////////////////////
//@UiModePreviews
//@Composable
//private fun InfoTilePreview() {
//    ThemedPreview {
//        InfoTile(
//            modifier = Modifier.fillMaxWidth(),
//            title = "Sample Title",
//            message = "Sample Message/Description"
//        )
//    }
//}

@UiModePreviews
@Composable
private fun AppInfoContainerPreview() {

    val dummyAppDetails = AppDetails(
        appName = "App Catalog",
        appIcon = null,
        versionCode = 7425,
        versionName = "1.0.2",
        packageName = "com.rkzmn.appscatalog",
        appTypeIndicators = listOf(AppTypeIndicator.installed, AppTypeIndicator.debuggable),
        installationSource = "Android System",
        installedTimestamp = "03 March 2022, 10:15:28 AM",
        lastUpdatedTimestamp = "07 May 2023, 11:19:31 PM",
        lastUsedTimestamp = "07 May 2023, 11:43:36 PM",
        appSize = "15.6 MB",
        minAndroidVersion = "Lollipop (22)",
        targetAndroidVersion = "Android 13",
        compileSdkAndroidVersion = "Android 13",
        activities = listOf(),
        services = listOf(),
        broadcastReceivers = listOf(),
        permissions = listOf()
    )
    ThemedPreview {

        AppInfoContainer(modifier = Modifier.fillMaxSize(), details = dummyAppDetails)

    }
}