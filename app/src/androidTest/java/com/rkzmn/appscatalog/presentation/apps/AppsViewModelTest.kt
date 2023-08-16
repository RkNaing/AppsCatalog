package com.rkzmn.appscatalog.presentation.apps

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.data.TestAppDataProvider
import com.rkzmn.appscatalog.di.AppModule
import com.rkzmn.appscatalog.di.AppPreferencesModule
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.domain.repositories.ListPreferenceRepository
import com.rkzmn.appscatalog.presentation.apps.list.AppItem
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import com.rkzmn.appsdataprovider.AppDataProvider
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@MediumTest
@HiltAndroidTest
@UninstallModules(AppPreferencesModule::class, AppModule::class)
@RunWith(AndroidJUnit4::class)
class AppsViewModelTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDataRepository: AppDataRepository

    @Inject
    lateinit var listPrefRepo: ListPreferenceRepository

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher + Job())

    @JvmField
    @BindValue
    val dispatcherProvider: CoroutineDispatcherProvider = object : CoroutineDispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val unconfined: CoroutineDispatcher = testDispatcher
    }

    @JvmField
    @BindValue
    val dataProvider: AppDataProvider = TestAppDataProvider()

    @BindValue
    @JvmField
    val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = {
            tmpFolder.newFile("test.preferences_pb")
                .also { println("Test DATASTORE => ${it.absolutePath}") }
        }
    )

    private lateinit var sut: AppsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
        sut = AppsViewModel(
            repository = appDataRepository,
            listPrefRepo = listPrefRepo,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun onSelectListType_appsListStateIsUpdatedCorrectly() = testScope.runTest {
        advanceUntilIdle() // Waits until all pending executions finish inside view model's init block
        sut.appsListState.test {
            val initialState = awaitItem()
            assertThat(initialState.listType).isEqualTo(AppsListType.ALL)
            assertThat(initialState.apps).isNotEmpty()
            assertThat(
                initialState.apps
                    .flatMap(AppItem::appTypeIndicators)
                    .containsAll(listOf(AppTypeIndicator.system, AppTypeIndicator.installed))
            ).isTrue()

            for (input in listOf(AppsListType.INSTALLED, AppsListType.SYSTEM)) {
                sut.onSelectListType(input)
                runCurrent()

                val loadingState = awaitItem()
                assertThat(loadingState.isLoading).isTrue()

                advanceUntilIdle()

                val updatedState = awaitItem()
                assertThat(updatedState.listType).isEqualTo(input)

                assertThat(
                    updatedState.apps.all {
                        val appTypeIndicators = it.appTypeIndicators
                        when (input) {
                            AppsListType.ALL -> appTypeIndicators.isNotEmpty()
                            AppsListType.INSTALLED -> appTypeIndicators.contains(AppTypeIndicator.installed)
                            AppsListType.SYSTEM -> appTypeIndicators.contains(AppTypeIndicator.system)
                        }
                    }
                ).isTrue()
            }
        }
    }

    @Test
    fun onSelectDisplayType_appsListStateIsUpdatedCorrectly() = testScope.runTest {
        advanceUntilIdle()
        sut.appsListState.test {
            val initialState = awaitItem()
            assertThat(initialState.listDisplayType).isEqualTo(AppsDisplayType.GRID)

            val input = AppsDisplayType.LIST
            sut.onSelectDisplayType(input)

            advanceUntilIdle()

            val updatedState = awaitItem()
            assertThat(updatedState.listDisplayType).isEqualTo(input)
        }
    }

    @Test
    fun onSelectSortOption_appsListStateIsUpdatedCorrectly() = testScope.runTest {
        sut.appsListState.test {
            val initialState = awaitItem()
            assertThat(initialState.sortBy).isEqualTo(AppSortOption.NAME_ASC)

            val input = AppSortOption.SIZE_DESC
            sut.onSelectSortOption(input)

            advanceUntilIdle()
            val updatedState = expectMostRecentItem()
            assertThat(updatedState.sortBy).isEqualTo(input)
        }
    }

    @Test
    fun onAppSelected_marksSelectedCorrectly() = testScope.runTest {
        advanceUntilIdle()
        sut.appsListState.test {
            val initialState = awaitItem()
            assertThat(initialState.apps.isNotEmpty()).isTrue()
            assertThat(initialState.apps.all { !it.isSelected }).isTrue()

            val packageToSelect = "com.rkzmn.appscatalog.dummy.app6"
            sut.onAppSelected(packageName = packageToSelect)
            advanceUntilIdle()

            val updatedState = awaitItem()
            val selectedApps = updatedState.apps.filter { it.isSelected }
            assertThat(
                selectedApps.size == 1 && with(selectedApps.first()) { isSelected && packageName == packageToSelect }
            ).isTrue()

            sut.clearSelection()
            advanceUntilIdle()

            val selectionClearedState = awaitItem()
            assertThat(selectionClearedState.apps.all { !it.isSelected }).isTrue()
        }
    }

    @Test
    fun onSearchStatusChange_updatesSearchStateCorrectly() = runTest {
        advanceUntilIdle()
        sut.appsSearchState.test {
            val initialState = awaitItem()
            assertThat(initialState.isActive).isFalse()

            sut.onSearchStatusChange(isActive = true)
            advanceUntilIdle()

            val activeState = awaitItem()
            assertThat(activeState.isActive).isTrue()
        }
    }

    @Test
    fun onSearchQueryChange_updatesSearchStateCorrectly() = runTest {
        advanceUntilIdle()
        sut.appsSearchState.test {
            val initialState = awaitItem()
            assertThat(initialState.results.isEmpty()).isTrue()

            val query = "app1"
            sut.onSearchQueryChange(query)
            runCurrent()

            val updatedState = awaitItem()
            assertThat(
                updatedState.results.all {
                    it.packageName.contains(query) || (it.appName?.contains(query) == true)
                }
            ).isTrue()
        }
    }

    @Test
    fun loadAppDetail_loadsCorrectAppDetails() = runTest {
        advanceUntilIdle()
        sut.appDetailsScreenState.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.details).isNull()

            val inputPackageName = "com.rkzmn.appscatalog.dummy.app5"
            sut.loadAppDetails(inputPackageName)
            runCurrent()

            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()
            advanceUntilIdle()

            val updatedState = awaitItem()
            assertThat(updatedState.isLoading).isFalse()
            assertThat(updatedState.details).isNotNull()
            assertThat(updatedState.details?.packageName).isEqualTo(inputPackageName)
        }
    }
}
