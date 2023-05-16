package com.rkzmn.appscatalog.domain.repositories

import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType

interface AppDataRepository {

    suspend fun getAllApps(
        isRefresh: Boolean = false,
        sortOption: AppSortOption = AppSortOption.NAME_ASC,
        listType: AppsListType = AppsListType.ALL,
    ): List<AppInfo>
}