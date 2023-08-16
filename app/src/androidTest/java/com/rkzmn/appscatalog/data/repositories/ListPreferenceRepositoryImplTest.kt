package com.rkzmn.appscatalog.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ListPreferenceRepositoryImplTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher + Job())

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { tmpFolder.newFile("test.preferences_pb") }
    )

    private val sut = ListPreferenceRepositoryImpl(
        dataStore = testDataStore,
        dispatcherProvider = object : CoroutineDispatcherProvider {
            override val main: CoroutineDispatcher = testDispatcher
            override val default: CoroutineDispatcher = testDispatcher
            override val io: CoroutineDispatcher = testDispatcher
            override val unconfined: CoroutineDispatcher = testDispatcher
        }
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getAppsListType() = testScope.runTest {
        val defaultListType = sut.appsListType.first()
        assertThat(defaultListType).isEqualTo(AppsListType.ALL)
    }

    @Test
    fun setAppsListType() = testScope.runTest {
        val inputListType = AppsListType.INSTALLED
        sut.setAppsListType(inputListType)

        val newListType = sut.appsListType.first()
        assertThat(newListType).isEqualTo(inputListType)
    }

    @Test
    fun getDisplayType() = testScope.runTest {
        val defaultDisplayType = sut.displayType.first()
        assertThat(defaultDisplayType).isEqualTo(AppsDisplayType.GRID)
    }

    @Test
    fun setAppsDisplayType() = testScope.runTest {
        val inputDisplayType = AppsDisplayType.LIST
        sut.setAppsDisplayType(inputDisplayType)

        val newDisplayType = sut.displayType.first()
        assertThat(newDisplayType).isEqualTo(inputDisplayType)
    }

    @Test
    fun getSortOption() = testScope.runTest {
        val defaultSortOption = sut.sortOption.first()
        assertThat(defaultSortOption).isEqualTo(AppSortOption.NAME_ASC)
    }

    @Test
    fun setSortOption() = testScope.runTest {
        val inputSortOption = AppSortOption.SIZE_DESC
        sut.setSortOption(inputSortOption)

        val newSortOption = sut.sortOption.first()
        assertThat(newSortOption).isEqualTo(inputSortOption)
    }
}
