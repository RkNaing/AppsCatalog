package com.rkzmn.appscatalog.presentation.apps.list

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.HiltTestActivity
import com.rkzmn.appscatalog.data.TestAppDataProvider
import com.rkzmn.appscatalog.di.AppModule
import com.rkzmn.appscatalog.di.AppPreferencesModule
import com.rkzmn.appscatalog.navigation.destination.AppSettingsDestination
import com.rkzmn.appscatalog.navigation.graphs.RootNavGraph
import com.rkzmn.appscatalog.presentation.apps.detail.states.AppDetailsScreenState
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsDisplayType
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsListScreenState
import com.rkzmn.appscatalog.presentation.apps.list.states.AppsSearchState
import com.rkzmn.appscatalog.ui.theme.AppsCatalogTheme
import com.rkzmn.appscatalog.utils.android.compose.LocalWindowSize
import com.rkzmn.appscatalog.utils.app.AppStrings
import com.rkzmn.appscatalog.utils.getString
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import com.rkzmn.appscatalog.utils.mockWindowSizeClass
import com.rkzmn.appscatalog.utils.onNodeWithContentDescriptionRes
import com.rkzmn.appsdataprovider.AppDataProvider
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@UninstallModules(AppPreferencesModule::class, AppModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppsListScreenTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

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

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun verify_appsListScreenIsShownAsRoot() {
        composeTestRule.setContent {
//            val windowSize = calculateWindowSizeClass(activity = composeTestRule.activity)
            val navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppsCatalogTheme {
                CompositionLocalProvider(LocalWindowSize provides windowSize) {
                    RootNavGraph(navController)
                }
            }
        }
        composeTestRule.onNodeWithTag(TEST_TAG_APPS_LIST_SCREEN).assertIsDisplayed()
    }

    @Test
    fun verify_masterDetailLayoutIsShownOnTabletLandscapeMode() {
        composeTestRule.setContent {
            AppsCatalogTheme {
                val mockWindowSize = mockWindowSizeClass(
                    width = WindowWidthSizeClass.Expanded,
                    height = WindowHeightSizeClass.Medium
                )
                CompositionLocalProvider(LocalWindowSize provides mockWindowSize) {
                    AppListScreen(
                        state = AppsListScreenState(),
                        searchState = AppsSearchState(),
                        windowSize = mockWindowSize,
                        onItemClicked = {},
                        onSelectAppListType = {},
                        onSelectDisplayType = {},
                        onSelectSortOption = {},
                        onClickedSettings = { /*TODO*/ },
                        onSearchQueryChanged = {},
                        onSearchStatusChanged = {},
                        appDetailsProvider = { AppDetailsScreenState() }
                    )
                }
            }
        }
        with(composeTestRule) {
            onNodeWithText(text = getString(AppStrings.msg_select_app_to_view))
        }.assertIsDisplayed()
    }

    @Test
    fun verify_clickFilterMenu_showsFilterBottomSheet() {
        composeTestRule.setContent {
            AppListScreen(
                state = AppsListScreenState(),
                searchState = AppsSearchState(),
                windowSize = windowSize,
                onItemClicked = {},
                onSelectAppListType = {},
                onSelectDisplayType = {},
                onSelectSortOption = {},
                onClickedSettings = { /*TODO*/ },
                onSearchQueryChanged = {},
                onSearchStatusChanged = {},
                appDetailsProvider = { AppDetailsScreenState() }
            )
        }
        composeTestRule.onNodeWithContentDescriptionRes(
            AppStrings.content_desc_tune_app_list
        ).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag(TEST_TAG_APPS_LIST_OPTIONS).assertIsDisplayed()
    }

    @Test
    fun verify_filterDisplayTypeOptionUpdatesListLayout() {
        composeTestRule.setContent {
            var appsListState by remember { mutableStateOf(AppsListScreenState()) }
            AppListScreen(
                state = appsListState,
                searchState = AppsSearchState(),
                windowSize = windowSize,
                onItemClicked = {},
                onSelectAppListType = {},
                onSelectDisplayType = { appsListState = appsListState.copy(listDisplayType = it) },
                onSelectSortOption = {},
                onClickedSettings = { /*TODO*/ },
                onSearchQueryChanged = {},
                onSearchStatusChanged = {},
                appDetailsProvider = { AppDetailsScreenState() }
            )
        }

        composeTestRule.onNodeWithContentDescriptionRes(
            AppStrings.content_desc_tune_app_list
        ).performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(TEST_TAG_APPS_GRID).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_APPS_LIST).assertDoesNotExist()

        val testTagGrid = AppsDisplayType.LIST.label.asString(composeTestRule.activity)
        composeTestRule.onNodeWithTag(testTag = testTagGrid).performClick()
        composeTestRule.waitForIdle()
        Espresso.pressBack()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(TEST_TAG_APPS_LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TEST_TAG_APPS_GRID).assertDoesNotExist()
    }

    @Test
    fun verify_clickOnSettingOpensAppSettingsScreen() {
        val navController = TestNavHostController(composeTestRule.activity)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        composeTestRule.setContent {
            AppsCatalogTheme {
                CompositionLocalProvider(LocalWindowSize provides windowSize) {
                    RootNavGraph(navController)
                }
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescriptionRes(AppStrings.lbl_settings).performClick()
        composeTestRule.waitForIdle()
        val currentRoute = navController.currentDestination?.route
        assertThat(currentRoute).isEqualTo(AppSettingsDestination.route)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private val windowSize: WindowSizeClass
        @Composable
        get() = calculateWindowSizeClass(activity = composeTestRule.activity)
}
