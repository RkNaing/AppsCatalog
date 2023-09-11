package com.rkzmn.appsdataprovider

import android.content.Context
import com.rkzmn.appsdataprovider.model.App
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission

interface AppDataProvider {
    fun getApps(): List<App>
    fun getAppServices(packageName: String): List<AppComponent>
    fun getAppActivities(packageName: String): List<AppComponent>
    fun getAppReceivers(packageName: String): List<AppComponent>
    fun getAppPermissions(packageName: String): List<AppPermission>
    companion object {
        fun getInstance(context: Context): AppDataProvider = AndroidAppDataProvider(context)
    }
}
