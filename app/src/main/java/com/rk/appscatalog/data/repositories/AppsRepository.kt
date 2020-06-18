package com.rk.appscatalog.data.repositories

import android.content.Context
import com.rk.appscatalog.data.models.AppItem
import com.rk.appscatalog.data.repositories.impl.AppsRepositoryImpl

/**
 * Created by ZMN on 17/06/2020.
 **/
interface AppsRepository {

    companion object {
        val instance: AppsRepository
            get() = AppsRepositoryImpl
    }

    suspend fun getAllApps(context: Context): List<AppItem>

    suspend fun getApps(context: Context, isSystem: Boolean = false): List<AppItem>

}