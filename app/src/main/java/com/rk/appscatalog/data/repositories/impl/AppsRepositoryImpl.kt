package com.rk.appscatalog.data.repositories.impl

import android.content.Context
import com.rk.appscatalog.data.datasource.AppsListStore
import com.rk.appscatalog.data.models.App
import com.rk.appscatalog.data.models.AppItem
import com.rk.appscatalog.data.repositories.AppsRepository

/**
 * Created by ZMN on 17/06/2020.
 **/
object AppsRepositoryImpl : AppsRepository {
    override suspend fun getAllApps(context: Context): List<AppItem> {
        return AppsListStore.getApps(context)
            .map(this::getAppItem)
            .sortedBy { it.name }
    }

    override suspend fun getApps(context: Context, isSystem: Boolean): List<AppItem> {
        return AppsListStore.getApps(context)
            .filter { it.isSystemApp == isSystem }
            .map(this::getAppItem)
            .sortedBy { it.name }
    }

    private fun getAppItem(app: App): AppItem =
        AppItem(app.name, app.icon, app.packageName, app.versionName)

}