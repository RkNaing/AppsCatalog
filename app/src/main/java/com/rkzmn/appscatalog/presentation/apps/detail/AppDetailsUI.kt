package com.rkzmn.appscatalog.presentation.apps.detail

import android.content.Context
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppDetailsUI(
    details: AppDetails,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val tabs = remember(details) { details.tabTitles.toImmutableList() }
    val pagerState = rememberPagerState(initialPage = 0)
    val selectedTabIndex = pagerState.currentPage
    val subtitleText by remember(key1 = selectedTabIndex, key2 = details) {
        mutableStateOf(
            prepareSubtitle(
                selectedTab = tabs.getOrNull(selectedTabIndex),
                details = details,
                context = context
            )
        )
    }

    LaunchedEffect(key1 = details) { pagerState.scrollToPage(0) }

    Column(
        modifier = modifier,
    ) {
        TitleSection(
            appName = details.appName,
            packageName = details.packageName,
            icon = details.appIcon,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingMedium),
        )

        SubtitleSection(
            subtitle = subtitleText,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = spacingExtraLarge)
        )

        SectionTabBar(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { index ->
                coroutineScope.launch { pagerState.scrollToPage(index) }
            }
        )

        Divider(modifier = Modifier.fillMaxWidth())

        AppDetailsContentPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            details = details,
            pagerState = pagerState,
            pageCount = tabs.size,
            selectedTab = { index -> tabs.getOrNull(index) }
        )
    }
}

@Composable
private fun TitleSection(
    appName: String?,
    packageName: String,
    icon: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(
            iconPath = icon,
            contentDescription = appName,
            modifier = Modifier
                .padding(spacingMedium)
                .size(appIconSize)
        )
        if (!appName.isNullOrBlank()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = spacingMedium),
                text = appName,
                maxLines = 2,
                style = MaterialTheme.typography.headlineSmall
            )
        } else {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = spacingMedium),
                text = packageName,
                maxLines = 2,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SubtitleSection(
    subtitle: AnnotatedString?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        // Needs method FQN: https://stackoverflow.com/a/69669445
        androidx.compose.animation.AnimatedVisibility(
            visible = !subtitle.isNullOrBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (!subtitle.isNullOrBlank()) {
                Text(
                    modifier = Modifier.animateContentSize(
                        animationSpec = tween(durationMillis = 200)
                    ),
                    text = subtitle,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun SectionTabBar(
    tabs: ImmutableList<AppDetailTab>,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onTabSelected: (Int) -> Unit,
) {
    ScrollableTabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        edgePadding = spacingMedium,
        divider = { }
    ) {
        tabs.forEachIndexed { index, tab ->
            val isTabSelected = index == selectedTabIndex
            Tab(
                selected = isTabSelected,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                onClick = { onTabSelected(index) },
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AppDetailsContentPager(
    details: AppDetails,
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    selectedTab: (Int) -> AppDetailTab?,
) {
    HorizontalPager(
        modifier = modifier,
        pageCount = pageCount,
        state = pagerState,
    ) { position ->

        val pageContentModifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium)

        when (selectedTab(position)) {
            AppDetailTab.OVERVIEW -> {
                AppInfoContainer(
                    modifier = pageContentModifier.verticalScroll(state = rememberScrollState()),
                    details = details
                )
            }

            AppDetailTab.PERMISSIONS -> {
                AppPermissionsList(
                    modifier = pageContentModifier,
                    permissions = details.permissions,
                )
            }

            AppDetailTab.ACTIVITIES -> {
                AppActivitiesList(
                    modifier = pageContentModifier,
                    activities = details.activities,
                )
            }

            AppDetailTab.SERVICES -> {
                AppServicesList(
                    modifier = pageContentModifier,
                    services = details.services,
                )
            }

            AppDetailTab.RECEIVERS -> {
                AppBroadcastReceiversList(
                    modifier = pageContentModifier,
                    receivers = details.broadcastReceivers
                )
            }

            null -> Unit
        }
    }
}

private fun prepareSubtitle(
    selectedTab: AppDetailTab?,
    details: AppDetails,
    context: Context
): AnnotatedString {
    if (selectedTab == null) {
        return AnnotatedString("")
    }

    val itemSize = details.itemSize(selectedTab)
    return if (itemSize > 0) {
        createCountLabelAnnotatedString(
            count = itemSize,
            label = context.getString(selectedTab.label)
        )
    } else {
        AnnotatedString("")
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

private fun AppDetails.itemSize(tab: AppDetailTab): Int = when (tab) {
    AppDetailTab.OVERVIEW -> 0
    AppDetailTab.PERMISSIONS -> permissions.size
    AppDetailTab.ACTIVITIES -> activities.size
    AppDetailTab.SERVICES -> services.size
    AppDetailTab.RECEIVERS -> broadcastReceivers.size
}

private enum class AppDetailTab(@StringRes val label: Int) {
    OVERVIEW(AppStrings.lbl_overview),
    PERMISSIONS(AppStrings.lbl_permissions),
    ACTIVITIES(AppStrings.lbl_activities),
    SERVICES(AppStrings.lbl_services),
    RECEIVERS(AppStrings.lbl_receivers),
}

// /////////////////////////////////////////////////////////////////////////
// Previews
// /////////////////////////////////////////////////////////////////////////

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
