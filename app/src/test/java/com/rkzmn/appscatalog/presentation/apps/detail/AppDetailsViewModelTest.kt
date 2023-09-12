package com.rkzmn.appscatalog.presentation.apps.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.data.repositories.AndroidAppDataRepository
import com.rkzmn.appscatalog.domain.mappers.displayName
import com.rkzmn.appscatalog.domain.mappers.getAppInfo
import com.rkzmn.appscatalog.domain.mappers.getComponentInfo
import com.rkzmn.appscatalog.domain.mappers.getPermissionInfo
import com.rkzmn.appscatalog.domain.mappers.indicators
import com.rkzmn.appscatalog.domain.mappers.readableSize
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.domain.model.AppDetails
import com.rkzmn.appscatalog.navigation.ARG_PACKAGE_NAME
import com.rkzmn.appscatalog.test.common.MainDispatcherRule
import com.rkzmn.appscatalog.test.common.TestAppDataProvider
import com.rkzmn.appscatalog.utils.kotlin.DateTimeFormat
import com.rkzmn.appscatalog.utils.kotlin.TestCoroutineDispatcherProvider
import com.rkzmn.appscatalog.utils.kotlin.asFormattedDate
import com.rkzmn.appsdataprovider.model.AppComponent
import com.rkzmn.appsdataprovider.model.AppPermission
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dispatcherProvider: TestCoroutineDispatcherProvider

    private lateinit var appDataProvider: TestAppDataProvider

    private lateinit var appDataRepository: AndroidAppDataRepository

    @Before
    fun setUp() {
        dispatcherProvider = TestCoroutineDispatcherProvider(
            testDispatcher = mainDispatcherRule.testDispatcher
        )

        appDataProvider = TestAppDataProvider()

        appDataRepository = AndroidAppDataRepository(
            appDataProvider = appDataProvider,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `Test app detail loads correctly`() = runTest {
        val inputPackageName = "com.rkzmn.appscatalog.dummy.app12"
        appDataRepository.getAllApps()

        val viewModel = AppDetailsViewModel(
            repository = appDataRepository,
            savedStateHandle = SavedStateHandle(mapOf(ARG_PACKAGE_NAME to inputPackageName)),
            dispatcherProvider = dispatcherProvider
        )
        advanceUntilIdle()
        viewModel.appDetailsScreenState.test {
            awaitItem()
            val detailState = awaitItem()
            assertThat(detailState.details).isNotNull()

            val expected = getAppDetails(inputPackageName)
            println(detailState.details)
            repeat(10) { println() }
            println(expected)
            assertThat(detailState.details).isEqualTo(expected)
        }
    }

    private fun getAppDetails(packageName: String): AppDetails {
        val comparator = compareBy<AppComponentInfo>({ it.packageName }, { it.name })
        val dateTimeFormat = DateTimeFormat.display_date_full_time
        val appInfo = appDataProvider.dummyApps.first { it.packageName == packageName }.getAppInfo()
        return AppDetails(
            activities = appDataProvider.getAppActivities(appInfo.packageName)
                .map(AppComponent::getComponentInfo)
                .sortedWith(comparator)
                .toImmutableList(),
            services = appDataProvider
                .getAppServices(packageName = appInfo.packageName)
                .map(AppComponent::getComponentInfo)
                .sortedWith(comparator)
                .toImmutableList(),
            broadcastReceivers = appDataProvider
                .getAppReceivers(packageName = appInfo.packageName)
                .map(AppComponent::getComponentInfo)
                .sortedWith(comparator)
                .toImmutableList(),
            permissions = appDataProvider
                .getAppPermissions(packageName = appInfo.packageName)
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
    }
}
