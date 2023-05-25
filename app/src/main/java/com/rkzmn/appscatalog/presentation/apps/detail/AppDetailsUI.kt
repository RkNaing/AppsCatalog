package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.domain.model.AppDetails
import com.rkzmn.appscatalog.domain.model.AppPermissionInfo
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import com.rkzmn.appscatalog.ui.theme.appIconSize
import com.rkzmn.appscatalog.ui.theme.spacingExtraLarge
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.ui.theme.spacingSmall
import com.rkzmn.appscatalog.ui.widgets.AppIcon
import com.rkzmn.appscatalog.ui.widgets.ThemedPreview
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews
import com.rkzmn.appscatalog.utils.app.AppStrings
import com.rkzmn.appscatalog.utils.app.createCountLabelAnnotatedString
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppDetailsUI(
    modifier: Modifier = Modifier,
    details: AppDetails,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val tabs = remember(details) { details.tabTitles.toMutableStateList() }
    val pagerState = rememberPagerState(initialPage = 0)
    val selectedTabIndex = pagerState.currentPage
    val subtitleText by remember(key1 = selectedTabIndex, key2 = details) {
        val annotatedString = when (val selectedTab = tabs.getOrNull(selectedTabIndex)) {
            AppDetailTab.PERMISSIONS -> createCountLabelAnnotatedString(
                count = details.permissions.size,
                label = context.getString(selectedTab.label)
            )

            AppDetailTab.ACTIVITIES -> createCountLabelAnnotatedString(
                count = details.activities.size,
                label = context.getString(selectedTab.label)
            )

            AppDetailTab.SERVICES -> createCountLabelAnnotatedString(
                count = details.services.size,
                label = context.getString(selectedTab.label)
            )

            AppDetailTab.RECEIVERS -> createCountLabelAnnotatedString(
                count = details.broadcastReceivers.size,
                label = context.getString(selectedTab.label)
            )

            else -> AnnotatedString("")
        }
        mutableStateOf(annotatedString)
    }

    Column(
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIcon(
                iconPath = details.appIcon,
                contentDescription = details.appName,
                modifier = Modifier
                    .padding(spacingMedium)
                    .size(appIconSize)
            )
            if (!details.appName.isNullOrBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = spacingMedium),
                    text = details.appName,
                    maxLines = 2,
                    style = MaterialTheme.typography.headlineSmall
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = spacingMedium),
                    text = details.packageName,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = spacingExtraLarge),
            contentAlignment = Alignment.CenterStart
        ) {
            // Needs method FQN: https://stackoverflow.com/a/69669445
            androidx.compose.animation.AnimatedVisibility(
                visible = subtitleText.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (subtitleText.isNotBlank()) {
                    Text(
                        modifier = Modifier.animateContentSize(
                            animationSpec = tween(durationMillis = 200)
                        ),
                        text = subtitleText,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            edgePadding = spacingMedium,
            divider = { }
        ) {
            tabs.forEachIndexed { index, tab ->
                val isTabSelected = index == selectedTabIndex
                Tab(
                    selected = isTabSelected,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    content = {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = spacingSmall,
                                vertical = spacingMedium
                            ),
                            text = stringResource(id = tab.label),
                            fontWeight = if (isTabSelected) FontWeight.Bold else null
                        )
                    }
                )
            }
        }

        Divider(modifier = Modifier.fillMaxWidth())

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            pageCount = tabs.size,
            state = pagerState,
        ) { position ->
            val pageContentModifier = Modifier
                .fillMaxSize()
                .padding(spacingMedium)

            when (tabs.getOrNull(position)) {
                AppDetailTab.OVERVIEW -> {
                    AppInfoContainer(
                        modifier = pageContentModifier.verticalScroll(state = rememberScrollState()),
                        details = details
                    )
                }

                AppDetailTab.PERMISSIONS -> {
//                    subtitleText = "${details.permissions.size} ${stringResource(id = tab.label)}"
                    AppPermissionsList(
                        modifier = pageContentModifier,
                        permissions = details.permissions,
                    )
                }

                AppDetailTab.ACTIVITIES -> {
//                    subtitleText = "${details.activities.size} ${stringResource(id = tab.label)}"
                    AppActivitiesList(
                        modifier = pageContentModifier,
                        activities = details.activities,
                    )
                }

                AppDetailTab.SERVICES -> {
//                    subtitleText = "${details.services.size} ${stringResource(id = tab.label)}"
                    AppServicesList(
                        modifier = pageContentModifier,
                        services = details.services,
                    )
                }

                AppDetailTab.RECEIVERS -> {
//                    subtitleText =
//                        "${details.broadcastReceivers.size} ${stringResource(id = tab.label)}"
                    AppBroadcastReceiversList(
                        modifier = pageContentModifier,
                        receivers = details.broadcastReceivers
                    )
                }

                null -> Unit
            }
        }

    }
}

private val AppDetails.tabTitles: List<AppDetailTab>
    get() {
        val titles = mutableListOf<AppDetailTab>()
        titles.add(AppDetailTab.OVERVIEW)
        if (permissions.isNotEmpty()) {
            titles.add(AppDetailTab.PERMISSIONS)
        }
        if (activities.isNotEmpty()) {
            titles.add(AppDetailTab.ACTIVITIES)
        }
        if (services.isNotEmpty()) {
            titles.add(AppDetailTab.SERVICES)
        }
        if (broadcastReceivers.isNotEmpty()) {
            titles.add(AppDetailTab.RECEIVERS)
        }
        return titles
    }

private enum class AppDetailTab(@StringRes val label: Int) {
    OVERVIEW(AppStrings.lbl_overview),
    PERMISSIONS(AppStrings.lbl_permissions),
    ACTIVITIES(AppStrings.lbl_activities),
    SERVICES(AppStrings.lbl_services),
    RECEIVERS(AppStrings.lbl_receivers),
}

///////////////////////////////////////////////////////////////////////////
// Previews
///////////////////////////////////////////////////////////////////////////

@UiModePreviews
@Composable
private fun AppDetailsPreview() {
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
        activities = persistentListOf(
            AppComponentInfo(
                name = "Dorothea Guzman",
                packageName = "Meredith Clements",
                fqn = "vidisse",
                isPrivate = false
            )
        ),
        services = persistentListOf(
            AppComponentInfo(
                name = "Dorothea Guzman",
                packageName = "Meredith Clements",
                fqn = "vidisse",
                isPrivate = false
            )
        ),
        broadcastReceivers = persistentListOf(
            AppComponentInfo(
                name = "Dorothea Guzman",
                packageName = "Meredith Clements",
                fqn = "vidisse",
                isPrivate = false
            )
        ),
        permissions = persistentListOf(
            AppPermissionInfo(
                permission = "consectetuer",
                group = null,
                description = null,
                isDangerous = false
            )
        )
    )

    ThemedPreview {
        AppDetailsUI(
            modifier = Modifier.fillMaxSize(),
            details = dummyAppDetails
        )
    }
}