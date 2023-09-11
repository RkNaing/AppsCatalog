package com.rkzmn.appscatalog.test.common

import com.rkzmn.appsdataprovider.AppDataProvider
import com.rkzmn.appsdataprovider.model.App
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission
import kotlin.random.Random

class TestAppDataProvider : AppDataProvider {

    val dummyApps = generateDummyApps()

    override fun getApps(): List<App> = dummyApps

    override fun getAppServices(packageName: String): List<AppComponent> {
        return generateDummyAppComponent(
            count = 50,
            packageName = packageName,
            type = AppComponent.Type.SERVICE
        )
    }

    override fun getAppActivities(packageName: String): List<AppComponent> {
        return generateDummyAppComponent(
            count = 360,
            packageName = packageName,
            type = AppComponent.Type.ACTIVITY
        )
    }

    override fun getAppReceivers(packageName: String): List<AppComponent> {
        return generateDummyAppComponent(
            count = 36,
            packageName = packageName,
            type = AppComponent.Type.BROADCAST
        )
    }

    override fun getAppPermissions(packageName: String): List<AppPermission> {
        val dummyPermissions = mutableListOf<AppPermission>()
        for (i in 1..DUMMY_PERMISSIONS_COUNT) {
            dummyPermissions += AppPermission(
                permission = "SamplePermission$i",
                group = "Mock Permissions",
                description = "This is just a mock permission for testing.",
                isDangerous = Random.nextBoolean()
            )
        }
        return dummyPermissions
    }

    private fun generateDummyAppComponent(
        count: Int = 250,
        packageName: String,
        type: AppComponent.Type
    ): List<AppComponent> {
        val dummyComponents = mutableListOf<AppComponent>()
        val namePrefix = when (type) {
            AppComponent.Type.SERVICE -> "DemoService"
            AppComponent.Type.ACTIVITY -> "DemoActivity"
            AppComponent.Type.BROADCAST -> "DemoBroadcastReceiver"
        }

        for (i in 1..count) {
            val name = "$namePrefix$i"
            dummyComponents += AppComponent(
                name = name,
                packageName = packageName,
                fqn = "$packageName.$name",
                isPrivate = Random.nextBoolean(),
                type = type
            )
        }
        return dummyComponents
    }

    private fun generateDummyApps(): List<App> {
        val dummyApps = mutableListOf<App>()

        for (i in 1..DUMMY_APPS_COUNT) {
            val appIcon = "icon_$i.png"
            val versionCode = i.toLong()
            val versionName = "1.0.$i"
            val packageName = "com.rkzmn.appscatalog.dummy.app$i"
            val appName = if (i % 7 == 0) packageName else "App $i"
            val isSystemApp = i % 2 == 0
            val isDebuggable = i % 3 == 0
            val installationSource = if (isSystemApp) "System" else "User"
            val installedTimestamp = System.currentTimeMillis()
            val lastUpdatedTimestamp = System.currentTimeMillis()
            val lastUsedTimestamp = if (i % 4 == 0) System.currentTimeMillis() else null
            val appSize = i * 1024L
            val minSdk = Random.nextInt(from = 1, until = 33)
            val targetSdk = 33
            val compileSdk = 33

            dummyApps += App(
                appName = appName,
                appIcon = appIcon,
                versionCode = versionCode,
                versionName = versionName,
                packageName = packageName,
                isSystemApp = isSystemApp,
                isDebuggable = isDebuggable,
                installationSource = installationSource,
                installedTimestamp = installedTimestamp,
                lastUpdatedTimestamp = lastUpdatedTimestamp,
                lastUsedTimestamp = lastUsedTimestamp,
                appSize = appSize,
                minSdk = minSdk,
                targetSdk = targetSdk,
                compileSdk = compileSdk,
            )
        }

        return dummyApps
    }
    companion object {
        private const val DUMMY_APPS_COUNT = 20
        private const val DUMMY_PERMISSIONS_COUNT = 45
    }
}
