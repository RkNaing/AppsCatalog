package com.rkzmn.appscatalog.domain.repositories

import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppInfoSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType

interface AppDataRepository {

    suspend fun getAllApps(
        isRefresh: Boolean = false,
        sortOption: AppInfoSortOption = AppInfoSortOption(),
        listType: AppsListType = AppsListType.ALL,
    ): List<AppInfo>
}