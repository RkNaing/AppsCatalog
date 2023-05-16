package com.rkzmn.appscatalog.data.repositories

import android.content.Context
import com.rkzmn.apps_data_provider.getApps
import com.rkzmn.appscatalog.domain.mappers.getAppInfo
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppSortOption
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
        sortOption: AppSortOption,
        listType: AppsListType
    ): List<AppInfo> {
        Timber.d("getAllApps() called with: isRefresh = [$isRefresh], sortOption = [$sortOption], listType = [$listType]")
        return withContext(dispatcherProvider.default) {
            if (isRefresh || apps.isEmpty()) {
                apps.addAll(getApps(context).map { it.getAppInfo() })
            }

            val comparator = when (sortOption) {
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

            val allApps = apps.sortedWith(comparator = comparator)

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