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

    private val dummyAppItem: AppItem
        get() = AppItem(
            appName = "AppsCatalog",
            appIcon = null,
            readableSize = "133.1 MB",
            packageName = "com.rkzmn.appscatalog",
            version = "1.0.1",
            appTypeIndicators = persistentListOf(
                AppTypeIndicator.system,
                AppTypeIndicator.installed,
                AppTypeIndicator.debuggable
            ),
            isSelected = false
        )

    @Test
    fun appListItem_assert_showsAllAppTypeIndicators() {
        val item = dummyAppItem
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
        with(composeTestRule) {
            setContent {
                var item by remember { mutableStateOf(dummyAppItem) }
                AppListItem(
                    appItem = item,
                    onClicked = {
                        item = item.copy(isSelected = !item.isSelected)
                    }
                )
            }
            onNodeWithTag(TEST_TAG_APP_ITEM).assertExists()
            onNodeWithTag(TEST_TAG_APP_ITEM_SELECTED).assertDoesNotExist()
            onNodeWithTag(TEST_TAG_APP_ITEM).performClick()
            onNodeWithTag(TEST_TAG_APP_ITEM).assertDoesNotExist()
            onNodeWithTag(TEST_TAG_APP_ITEM_SELECTED).assertExists()
        }
    }
}
