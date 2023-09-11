package com.rkzmn.appscatalog.presentation.apps

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.data.repositories.AndroidAppDataRepository
import com.rkzmn.appscatalog.data.repositories.FakeListPreferenceRepository
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.list.AppItem
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.test.common.MainDispatcherRule
import com.rkzmn.appscatalog.test.common.TestAppDataProvider
import com.rkzmn.appscatalog.test.common.isSortedBy
import com.rkzmn.appscatalog.utils.kotlin.TestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class AppsViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AppsViewModel
    private lateinit var appDataProvider: TestAppDataProvider

    @Before
    fun setUp() {
        val dispatcherProvider = TestCoroutineDispatcherProvider(
            testDispatcher = mainDispatcherRule.testDispatcher
        )

        appDataProvider = TestAppDataProvider()

        val appDataRepository = AndroidAppDataRepository(
            appDataProvider = appDataProvider,
            dispatcherProvider = dispatcherProvider
        )

        viewModel = AppsViewModel(
            repository = appDataRepository,
            listPrefRepo = FakeListPreferenceRepository(),
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `Initial load emits loading state and then apps list`() = runTest {
        viewModel.appsListState.test {
            val initialEmission = awaitItem() // ignore first emission
            println(initialEmission)

            val firstEmission = awaitItem()
            assertThat(firstEmission.isLoading).isTrue()

            val secondEmission = awaitItem()
            assertThat(secondEmission.isLoading).isFalse()
            assertThat(secondEmission.apps).isNotEmpty()
        }
    }

    @Test
    fun `Refresh app emits states correctly (loading + isRefresh) and then apps list`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsListState.test {
            viewModel.refreshAppsList()

            skipItems(1) // skip initial
            val firstEmission = awaitItem()
            assertThat(firstEmission.isLoading).isTrue()
            assertThat(firstEmission.isRefresh).isTrue()

            val secondEmission = awaitItem()
            assertThat(secondEmission.isLoading).isFalse()
            assertThat(secondEmission.isRefresh).isFalse()
            assertThat(secondEmission.apps).isNotEmpty()
        }
    }

    @Test
    fun `Select app list type updates the appsListState correctly`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsListState.test {
            val input = AppsListType.INSTALLED
            viewModel.onSelectListType(input)

            skipItems(2) // skip initial + loading for onSelectListType states
            val updatedState = awaitItem()
            assertThat(updatedState.listType).isEqualTo(input)
            val installedAppsNames = appDataProvider.dummyApps
                .filter { !it.isSystemApp }
                .map { it.packageName }
                .sorted()

            assertThat(
                updatedState.apps
                    .map { it.packageName }
                    .sorted()
            ).isEqualTo(installedAppsNames)
        }
    }

    @Test
    fun `Select display type updates the appsListState correctly`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsListState.test {
            val input = AppsDisplayType.LIST
            viewModel.onSelectDisplayType(input)

            skipItems(1) // skip initial
            val updatedState = awaitItem()
            assertThat(updatedState.listDisplayType).isEqualTo(input)
        }
    }

    @Test
    fun `Select sort option provided correctly sorted apps list`() = runTest(timeout = 10.minutes) {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsListState.test {
            val input = AppSortOption.NAME_DESC
            viewModel.onSelectSortOption(input)

            skipItems(2) // skip initial + loading for onSelectSortOption states
            val updatedState = awaitItem()
            assertThat(updatedState.sortBy).isEqualTo(input)
            val isSortedByNameDesc = updatedState.apps.isSortedBy(
                ascending = false,
                component = AppItem::appName
            )
            assertThat(isSortedByNameDesc).isTrue()
        }
    }

    @Test
    fun `Load app details by package name emits states correctly`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appDetailsScreenState.test {
            val initialEmission = awaitItem()
            assertThat(initialEmission.isLoading).isFalse()
            assertThat(initialEmission.details).isNull()

            val inputPackageName = "com.rkzmn.appscatalog.dummy.app12"
            viewModel.loadAppDetails(inputPackageName)

            val firstEmission = awaitItem()
            assertThat(firstEmission.isLoading).isTrue()
            assertThat(firstEmission.details).isNull()

            val secondEmission = awaitItem()
            assertThat(secondEmission.isLoading).isFalse()
            assertThat(secondEmission.details).isNotNull()
            assertThat(secondEmission.details!!.packageName).isEqualTo(inputPackageName)
        }
    }

    @Test
    fun `Search by package name emits correct search result and states`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsSearchState.test {
            val initialState = awaitItem()
            assertThat(initialState.results).isEmpty()
            assertThat(initialState.isActive).isFalse()
            assertThat(initialState.query).isEmpty()

            val inputPackageName = "com.rkzmn.appscatalog.dummy.app12"
            viewModel.onSearchQueryChange(inputPackageName)

            skipItems(1) // skip search query update state
            val searchResult = awaitItem()
            assertThat(searchResult.query).isEqualTo(inputPackageName)
            assertThat(searchResult.results).hasSize(1)
            assertThat(searchResult.results[0].packageName.text).isEqualTo(inputPackageName)
            assertThat(searchResult.isActive).isFalse()
        }
    }

    @Test
    fun `Search by app name emits correct search result and states`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsSearchState.test {
            val initialState = awaitItem()
            assertThat(initialState.results).isEmpty()
            assertThat(initialState.isActive).isFalse()
            assertThat(initialState.query).isEmpty()

            val name = "App 2"
            viewModel.onSearchQueryChange(name)

            skipItems(1) // skip search query update state
            val searchResult = awaitItem()
            assertThat(searchResult.query).isEqualTo(name)
            assertThat(searchResult.results).hasSize(2)
            assertThat(searchResult.results.all { it.appName?.text?.startsWith(name) == true }).isTrue()
            assertThat(searchResult.isActive).isFalse()
        }
    }

    @Test
    fun `Set search active false resets search state`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsSearchState.test {
            val initialState = awaitItem()
            assertThat(initialState.results).isEmpty()
            assertThat(initialState.isActive).isFalse()
            assertThat(initialState.query).isEmpty()

            val name = "App 1"
            viewModel.onSearchQueryChange(name)

            skipItems(1) // skip search query update state
            val searchResult = awaitItem()
            assertThat(searchResult.query).isEqualTo(name)
            assertThat(searchResult.results).isNotEmpty()

            viewModel.onSearchStatusChange(false)

            val resetState = awaitItem()
            assertThat(resetState.results).isEmpty()
            assertThat(resetState.isActive).isFalse()
            assertThat(resetState.query).isEmpty()
        }
    }

    @Test
    fun `Select app marks correctly app as selected`() = runTest {
        advanceUntilIdle() // Wait until initial data load is done.

        viewModel.appsListState.test {
            skipItems(1)

            val firstSelectPackageName = "com.rkzmn.appscatalog.dummy.app12"
            viewModel.onAppSelected(firstSelectPackageName)

            val firstSelectedState = awaitItem()
            assertThat(
                firstSelectedState.apps.single {
                    it.packageName == firstSelectPackageName
                }.isSelected
            ).isTrue()

            val secondSelectPackageName = "com.rkzmn.appscatalog.dummy.app15"
            viewModel.onAppSelected(secondSelectPackageName)

            val secondSelectedState = awaitItem()
            assertThat(
                secondSelectedState.apps.single {
                    it.packageName == firstSelectPackageName
                }.isSelected
            ).isFalse()
            assertThat(
                secondSelectedState.apps.single {
                    it.packageName == secondSelectPackageName
                }.isSelected
            ).isTrue()

            viewModel.clearSelection()

            val selectionClearedState = awaitItem()
            assertThat(selectionClearedState.apps.filter { it.isSelected }).isEmpty()
        }
    }
}
