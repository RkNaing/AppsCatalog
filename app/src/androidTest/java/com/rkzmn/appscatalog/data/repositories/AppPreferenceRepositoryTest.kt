package com.rkzmn.appscatalog.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.domain.model.AppTheme
import com.rkzmn.appscatalog.domain.repositories.AppPreferenceRepository
import com.rkzmn.appscatalog.test.common.MainDispatcherRule
import com.rkzmn.appscatalog.utils.AppsCatalogAndroidTest
import com.rkzmn.appscatalog.utils.android.AndroidVersions
import com.rkzmn.appscatalog.utils.android.isSDKIntAtLeast
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
class AppPreferenceRepositoryTest : AppsCatalogAndroidTest() {

    @Inject
    lateinit var appPreferenceRepository: AppPreferenceRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @BindValue
    val dispatcherProvider = TestCoroutineDispatcherProvider(mainDispatcherRule.testDispatcher)

    @Test
    fun setAppTheme_savedCorrectly() = runTest {
        appPreferenceRepository.appTheme.test {
            val initialValue = awaitItem()
            assertThat(initialValue).isEqualTo(AppTheme.FOLLOW_SYSTEM)

            appPreferenceRepository.setAppTheme(AppTheme.DARK)
            advanceUntilIdle()

            val updatedTheme = awaitItem()
            assertThat(updatedTheme).isEqualTo(AppTheme.DARK)
        }
    }

    @Test
    fun setUseDynamicColors_savedCorrectly() = runTest {
        appPreferenceRepository.isUsingDynamicColors.test {
            val expectedInitialValue = isSDKIntAtLeast(AndroidVersions.S)
            val initialValue = awaitItem()
            assertThat(initialValue).isEqualTo(expectedInitialValue)

            appPreferenceRepository.setUseDynamicColors(!expectedInitialValue)
            advanceUntilIdle()

            val updatedState = awaitItem()
            assertThat(updatedState).isEqualTo(!expectedInitialValue)
        }
    }
}
