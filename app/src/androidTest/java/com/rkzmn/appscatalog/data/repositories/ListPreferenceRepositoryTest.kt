package com.rkzmn.appscatalog.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.domain.model.AppsListType
import com.rkzmn.appscatalog.domain.repositories.ListPreferenceRepository
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.test.common.MainDispatcherRule
import com.rkzmn.appscatalog.utils.AppsCatalogAndroidTest
import com.rkzmn.appscatalog.utils.kotlin.TestCoroutineDispatcherProvider
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class ListPreferenceRepositoryTest : AppsCatalogAndroidTest() {

    @Inject
    lateinit var listPreferenceRepository: ListPreferenceRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @BindValue
    val dispatcherProvider = TestCoroutineDispatcherProvider(mainDispatcherRule.testDispatcher)

    @Test
    fun setAppsListType_savedCorrectly() = runTest {
        listPreferenceRepository.appsListType.test {
            val initialEmit = awaitItem()
            assertThat(initialEmit).isEqualTo(AppsListType.ALL)

            listPreferenceRepository.setAppsListType(AppsListType.INSTALLED)
            advanceUntilIdle()

            val updatedEmit = awaitItem()
            assertThat(updatedEmit).isEqualTo(AppsListType.INSTALLED)
        }
    }

    @Test
    fun setAppsDisplayType_savedCorrectly() = runTest {
        listPreferenceRepository.displayType.test {
            val initialEmit = awaitItem()
            assertThat(initialEmit).isEqualTo(AppsDisplayType.GRID)

            listPreferenceRepository.setAppsDisplayType(AppsDisplayType.LIST)
            advanceUntilIdle()

            val updatedEmit = awaitItem()
            assertThat(updatedEmit).isEqualTo(AppsDisplayType.LIST)
        }
    }

    @Test
    fun setAppsSorts_savedCorrectly() = runTest {
        listPreferenceRepository.sortOption.test {
            val initialEmit = awaitItem()
            assertThat(initialEmit).isEqualTo(AppSortOption.NAME_ASC)

            listPreferenceRepository.setSortOption(AppSortOption.SIZE_DESC)
            advanceUntilIdle()

            val updatedEmit = awaitItem()
            assertThat(updatedEmit).isEqualTo(AppSortOption.SIZE_DESC)
        }
    }
}
