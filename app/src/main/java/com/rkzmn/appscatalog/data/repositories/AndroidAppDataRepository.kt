package com.rkzmn.appscatalog.data.repositories

import com.rkzmn.appscatalog.domain.mappers.displayName
import com.rkzmn.appscatalog.domain.mappers.getAppInfo
import com.rkzmn.appscatalog.domain.mappers.getComponentInfo
import com.rkzmn.appscatalog.domain.mappers.getPermissionInfo
import com.rkzmn.appscatalog.domain.mappers.indicators
import com.rkzmn.appscatalog.domain.mappers.readableSize
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.domain.model.AppDetails
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import com.rkzmn.appscatalog.utils.kotlin.DateTimeFormat
import com.rkzmn.appscatalog.utils.kotlin.asFormattedDate
import com.rkzmn.appsdataprovider.AppDataProvider
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AndroidAppDataRepository @Inject constructor(
    private val appDataProvider: AppDataProvider,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : AppDataRepository {

    private val apps = mutableListOf<AppInfo>()
    private val appDetails = mutableMapOf<String, AppDetails>()

    override suspend fun getAllApps(
        isRefresh: Boolean,
        sortOption: AppSortOption,
        listType: AppsListType
    ): List<AppInfo> {
        Timber.d(
            "getAllApps() called with: isRefresh = [$isRefresh], " +
                "sortOption = [$sortOption], listType = [$listType]"
        )
        return withContext(dispatcherProvider.default) {
            if (isRefresh || apps.isEmpty()) {
                apps.apply {
                    clear()
                    addAll(appDataProvider.getApps().map { it.getAppInfo() })
                }
            }

            val allApps = apps.sortedWith(comparator = sortOption.appInfoComparator)

            filterAppsList(listType, allApps)
        }.also {
            Timber.d("getAllApps() returned: ${it.size} Apps. Sorted By ")
        }
    }

    override suspend fun searchByNameAndPackageName(query: String): List<AppInfo> {
        Timber.d("searchByNameAndPackageName() called with: query = [$query]")
        if (query.isBlank()) {
            return emptyList()
        }
        return withContext(dispatcherProvider.default) {
            apps.filter { app ->
                app.appName.contains(query, ignoreCase = true) ||
                    app.packageName.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun getAppDetails(packageName: String): AppDetails? {
        Timber.d("getAppDetails() called with: packageName = [$packageName]")
        val cache = appDetails[packageName]
        if (cache != null) {
            return cache
        }

        return withContext(dispatcherProvider.default) {
            val appInfo =
                apps.firstOrNull { it.packageName == packageName } ?: return@withContext null

            val dateTimeFormat = DateTimeFormat.display_date_full_time
            val comparator = compareBy<AppComponentInfo>({ it.packageName }, { it.name })
            val details = AppDetails(
                activities = appDataProvider
                    .getAppActivities(packageName = packageName)
                    .map(AppComponent::getComponentInfo)
                    .sortedWith(comparator)
                    .toImmutableList(),
                services = appDataProvider
                    .getAppServices(packageName = packageName)
                    .map(AppComponent::getComponentInfo)
                    .sortedWith(comparator)
                    .toImmutableList(),
                broadcastReceivers = appDataProvider
                    .getAppReceivers(packageName = packageName)
                    .map(AppComponent::getComponentInfo)
                    .sortedWith(comparator)
                    .toImmutableList(),
                permissions = appDataProvider
                    .getAppPermissions(packageName = packageName)
                    .map(AppPermission::getPermissionInfo)
                    .toImmutableList(),
                appName = appInfo.displayName,
                appIcon = appInfo.appIcon,
                versionCode = appInfo.versionCode,
                versionName = appInfo.versionName,
                packageName = appInfo.packageName,
                appTypeIndicators = appInfo.indicators,
                installationSource = appInfo.installationSource,
                installedTimestamp = appInfo.installedTimestamp.asFormattedDate(dateTimeFormat),
                lastUpdatedTimestamp = appInfo.lastUpdatedTimestamp.asFormattedDate(dateTimeFormat),
                lastUsedTimestamp = appInfo.lastUsedTimestamp?.asFormattedDate(dateTimeFormat),
                appSize = appInfo.readableSize,
                minAndroidVersion = with(appInfo) { "$minAndroidVersion ($minSdk)" },
                targetAndroidVersion = with(appInfo) { "$targetAndroidVersion ($targetSdk)" },
                compileSdkAndroidVersion = with(appInfo) { "$compileSdkAndroidVersion ($compileSdk)" },
            )
            appDetails[packageName] = details
            details
        }
    }

    private fun filterAppsList(
        listType: AppsListType,
        apps: List<AppInfo>
    ): List<AppInfo> = when (listType) {
        AppsListType.ALL -> apps
        AppsListType.INSTALLED -> apps.filter { !it.isSystemApp }
        AppsListType.SYSTEM -> apps.filter { it.isSystemApp }
    }

    private val AppSortOption.appInfoComparator: Comparator<AppInfo>
        get() = when (this) {
            AppSortOption.NAME_ASC -> compareBy(AppInfo::appName)
            AppSortOption.NAME_DESC -> compareByDescending(AppInfo::appName)
            AppSortOption.INSTALLED_DATE_ASC -> compareBy(AppInfo::installedTimestamp)
            AppSortOption.INSTALLED_DATE_DESC -> compareByDescending(AppInfo::installedTimestamp)
            AppSortOption.LAST_UPDATED_ASC -> compareBy(AppInfo::lastUpdatedTimestamp)
            AppSortOption.LAST_UPDATED_DESC -> compareByDescending(AppInfo::lastUpdatedTimestamp)
            AppSortOption.SIZE_ASC -> compareBy(AppInfo::appSize)
            AppSortOption.SIZE_DESC -> compareByDescending(AppInfo::appSize)
            AppSortOption.LAST_USED_ASC -> compareBy(AppInfo::lastUsedTimestamp)
            AppSortOption.LAST_USED_DESC -> compareByDescending(AppInfo::lastUsedTimestamp)
        }
}
