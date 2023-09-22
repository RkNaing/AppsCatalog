package com.rkzmn.appscatalog.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.rkzmn.appscatalog.utils.android.clear
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class AppsCatalogAndroidTest {

    protected lateinit var context: Context

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Before
    open fun setup() {
        context = testContext
        hiltRule.inject()
        runBlocking {
            dataStore.clear()
        }
    }

    @After
    open fun tearDown() {
    }
}
