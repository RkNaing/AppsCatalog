package com.rkzmn.appscatalog.data.repositories

import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.test.common.MainDispatcherRule
import com.rkzmn.appscatalog.test.common.TestAppDataProvider
import com.rkzmn.appscatalog.test.common.isSortedBy
import com.rkzmn.appscatalog.utils.kotlin.TestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppDataRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var appDataProvider: TestAppDataProvider

    private lateinit var appDataRepository: AppDataRepository

    @Before
    fun setup() {
        val dispatcherProvider = TestCoroutineDispatcherProvider(
            testDispatcher = mainDispatcherRule.testDispatcher
        )

        appDataProvider = TestAppDataProvider()

        appDataRepository = AndroidAppDataRepository(
            appDataProvider = appDataProvider,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `getAllApps returns all apps including both system and installed apps`() = runTest {
        val allApps = appDataRepository.getAllApps()

        assertThat(allApps.size).isEqualTo(appDataProvider.dummyApps.size)
        assertThat(allApps.any { it.isSystemApp }).isTrue() // assert contains system apps
        assertThat(allApps.any { !it.isSystemApp }).isTrue() // assert contains installed apps

        assertThat(appDataProvider.wasGetAppsCalled()).isTrue() // assert data is fresh loaded
    }

    @Test
    fun `getAllApps with list type system returns only system apps`() = runTest {
        val allApps = appDataRepository.getAllApps(listType = AppsListType.SYSTEM)

        assertThat(allApps.size).isEqualTo(appDataProvider.dummyApps.count { it.isSystemApp })
        assertThat(allApps.all { it.isSystemApp }).isTrue() // assert contains system apps
    }

    @Test
    fun `getAllApps with list type installed returns only installed apps`() = runTest {
        val allApps = appDataRepository.getAllApps(listType = AppsListType.INSTALLED)

        assertThat(allApps.size).isEqualTo(appDataProvider.dummyApps.count { !it.isSystemApp })
        assertThat(allApps.all { !it.isSystemApp }).isTrue() // assert contains system apps
    }

    @Test
    fun `getAllApps refresh works correctly`() = runTest {
        appDataRepository.getAllApps()

        assertThat(appDataProvider.wasGetAppsCalled()).isTrue() // Initial refresh load

        appDataRepository.getAllApps()
        assertThat(appDataProvider.wasGetAppsCalled()).isFalse() // assert doesn't refresh twice

        appDataRepository.getAllApps(isRefresh = true)
        assertThat(appDataProvider.wasGetAppsCalled()).isTrue() // assert refresh when requested
    }

    @Test
    fun `getAllApps provides correctly sorted`() = runTest {
        suspend fun <C : Comparable<C>> assertSorting(
            sortOption: AppSortOption,
            ascending: Boolean,
            component: AppInfo.() -> C?
        ) {
            val allApps = appDataRepository.getAllApps(sortOption = sortOption)
            assertThat(allApps.isSortedBy(ascending = ascending, component = component)).isTrue()
        }

        assertSorting(
            sortOption = AppSortOption.NAME_ASC,
            ascending = true,
            AppInfo::appName
        )
        assertSorting(
            sortOption = AppSortOption.NAME_DESC,
            ascending = false,
            AppInfo::appName
        )
        assertSorting(
            sortOption = AppSortOption.INSTALLED_DATE_ASC,
            ascending = true,
            AppInfo::installedTimestamp
        )
        assertSorting(
            sortOption = AppSortOption.INSTALLED_DATE_DESC,
            ascending = false,
            AppInfo::installedTimestamp
        )
        assertSorting(
            sortOption = AppSortOption.LAST_UPDATED_ASC,
            ascending = true,
            AppInfo::lastUpdatedTimestamp
        )
        assertSorting(
            sortOption = AppSortOption.LAST_UPDATED_DESC,
            ascending = false,
            AppInfo::lastUpdatedTimestamp
        )
        assertSorting(
            sortOption = AppSortOption.SIZE_ASC,
            ascending = true,
            AppInfo::appSize
        )
        assertSorting(
            sortOption = AppSortOption.SIZE_DESC,
            ascending = false,
            AppInfo::appSize
        )
        assertSorting(
            sortOption = AppSortOption.LAST_USED_ASC,
            ascending = true,
            AppInfo::lastUsedTimestamp
        )
        assertSorting(
            sortOption = AppSortOption.LAST_USED_DESC,
            ascending = false,
            AppInfo::lastUsedTimestamp
        )
    }

    @Test
    fun `searchByNameAndPackageName returns correct results`() = runTest {
        appDataRepository.getAllApps()

        val packageQuery = "com.rkzmn.appscatalog.dummy.app11"
        val searchByPackageResult = appDataRepository.searchByNameAndPackageName(packageQuery)
        assertThat(searchByPackageResult).hasSize(1)
        assertThat(searchByPackageResult[0].packageName).isEqualTo(packageQuery)

        val nameQuery = "App 12"
        val searchByNameResult = appDataRepository.searchByNameAndPackageName(nameQuery)
        assertThat(searchByNameResult).hasSize(1)
        assertThat(searchByNameResult[0].appName).isEqualTo(nameQuery)

        val partialQuery = "app2"
        val partialQueryResult = appDataRepository.searchByNameAndPackageName(partialQuery)
        assertThat(partialQueryResult).hasSize(2)

        // Empty query
        assertThat(appDataRepository.searchByNameAndPackageName("")).hasSize(0)

        // Invalid Query
        assertThat(appDataRepository.searchByNameAndPackageName("Not Exists")).hasSize(0)
    }
}
