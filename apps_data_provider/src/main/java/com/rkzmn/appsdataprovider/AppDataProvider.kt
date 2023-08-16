package com.rkzmn.appsdataprovider

import android.content.Context
import com.rkzmn.appsdataprovider.model.App
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission

interface AppDataProvider {

    fun getApps(context: Context): List<App>

    fun getAppServices(
        context: Context,
        packageName: String
    ): List<AppComponent>

    fun getAppActivities(
        context: Context,
        packageName: String
    ): List<AppComponent>

    fun getAppReceivers(
        context: Context,
        packageName: String
    ): List<AppComponent>

    fun getAppPermissions(
        context: Context,
        packageName: String
    ): List<AppPermission>

    companion object {
        fun getInstance(): AppDataProvider = AppDataProviderImpl()
    }
}
