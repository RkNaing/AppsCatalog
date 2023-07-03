package com.rkzmn.appscatalog.presentation.apps.list

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppListItemTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appListItem_assert_showsAllAppTypeIndicators() {
        val item = AppItem(
            appName = "AppsCatalog",
            appIcon = null,
            readableSize = null,
            packageName = "com.rkzmn.appscatalog",
            version = "augue",
            appTypeIndicators = persistentListOf(
                AppTypeIndicator.system,
                AppTypeIndicator.installed,
                AppTypeIndicator.debuggable
            ),
            isSelected = false
        )
        composeTestRule.setContent {
            AppListItem(
                appItem = item,
                onClicked = {}
            )
        }
        val context = composeTestRule.activity
        item.appTypeIndicators.forEach { indicator ->
            val contentDescription = indicator.contentDescription.asString(context)
            composeTestRule
                .onNodeWithContentDescription(label = contentDescription)
                .assertExists(errorMessageOnFail = "$contentDescription icon missing.")
        }
    }

    @Test
    fun appListItem_assert_showsSelectedAndNormalUIAccordingly() {
        composeTestRule.setContent {
            var item by remember {
                mutableStateOf(
                    AppItem(
                        appName = "AppsCatalog",
                        appIcon = null,
                        readableSize = null,
                        packageName = "com.rkzmn.appscatalog",
                        version = "augue",
                        appTypeIndicators = persistentListOf(
                            AppTypeIndicator.system,
                            AppTypeIndicator.installed,
                            AppTypeIndicator.debuggable
                        ),
                        isSelected = false
                    )
                )
            }
            AppListItem(
                appItem = item,
                onClicked = {
                    item = item.copy(isSelected = !item.isSelected)
                }
            )
        }
        composeTestRule.onNodeWithTag(TEST_TAG_APP_ITEM).assertExists()
        composeTestRule.onNodeWithTag(TEST_TAG_APP_ITEM_SELECTED).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TEST_TAG_APP_ITEM).performClick()
        composeTestRule.onNodeWithTag(TEST_TAG_APP_ITEM).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TEST_TAG_APP_ITEM_SELECTED).assertExists()
    }
}
