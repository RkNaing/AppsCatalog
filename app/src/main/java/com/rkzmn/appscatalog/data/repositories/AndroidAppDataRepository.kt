package com.rkzmn.appscatalog.data.repositories

import android.content.Context
import com.rkzmn.apps_data_provider.getApps
import com.rkzmn.appscatalog.domain.mappers.getAppInfo
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppInfoSortOption
import com.rkzmn.appscatalog.domain.model.AppInfoSortOption.Property.INSTALLED_DATE
import com.rkzmn.appscatalog.domain.model.AppInfoSortOption.Property.LAST_UPDATED
import com.rkzmn.appscatalog.domain.model.AppInfoSortOption.Property.LAST_USED
import com.rkzmn.appscatalog.domain.model.AppInfoSortOption.Property.NAME
import com.rkzmn.appscatalog.domain.model.AppInfoSortOption.Property.SIZE
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AndroidAppDataRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : AppDataRepository {

    private val apps = mutableListOf<AppInfo>()

    override suspend fun getAllApps(
        isRefresh: Boolean,
        sortOption: AppInfoSortOption,
        listType: AppsListType
    ): List<AppInfo> {
        Timber.d("getAllApps() called with: isRefresh = [$isRefresh], sortOption = [$sortOption], listType = [$listType]")
        return withContext(dispatcherProvider.default) {
            if (isRefresh || apps.isEmpty()) {
                apps.addAll(getApps(context).map { it.getAppInfo() })
            }

            val selector: (AppInfo) -> Comparable<*>? = when (sortOption.property) {
                NAME -> AppInfo::appName
                INSTALLED_DATE -> AppInfo::installedTimestamp
                LAST_UPDATED -> AppInfo::lastUpdatedTimestamp
                SIZE -> AppInfo::appSize
                LAST_USED -> AppInfo::lastUsedTimestamp
            }

            val allApps = apps.sortedWith(
                comparator = if (sortOption.isDescending) {
                    compareBy(selector)
                } else {
                    compareByDescending(
                        selector
                    )
                }
            )

            when (listType) {
                AppsListType.ALL -> allApps
                AppsListType.INSTALLED -> allApps.filter { !it.isSystemApp }
                AppsListType.SYSTEM -> allApps.filter { it.isSystemApp }
            }

        }.also {
            Timber.d("getAllApps() returned: ${it.size} Apps. Sorted By ")
        }
    }

}