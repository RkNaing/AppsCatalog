package com.rkzmn.appscatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.rkzmn.appscatalog.domain.model.AppTheme
import com.rkzmn.appscatalog.domain.repositories.AppPreferenceRepository
import com.rkzmn.appscatalog.navigation.graphs.RootNavGraph
import com.rkzmn.appscatalog.ui.theme.AppsCatalogTheme
import com.rkzmn.appscatalog.utils.android.compose.LocalWindowSize
import com.rkzmn.appscatalog.utils.android.toast
import com.rkzmn.appscatalog.utils.app.AppStrings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPrefRepo: AppPreferenceRepository

    private var lastBackPressTime = 0L

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) { handleBackPress() }

        setContent {
            val currentAppTheme by appPrefRepo.appTheme
                .collectAsStateWithLifecycle(initialValue = AppTheme.FOLLOW_SYSTEM)

            val useDynamicColors by appPrefRepo.isUsingDynamicColors
                .collectAsStateWithLifecycle(initialValue = true)

            val isDarkTheme = when (currentAppTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            }

            val windowSize = calculateWindowSizeClass(activity = this)

            AppsCatalogTheme(
                darkTheme = isDarkTheme,
                dynamicColor = useDynamicColors,
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalWindowSize provides windowSize) {
                        RootNavGraph(navHostController = rememberNavController())
                    }
                }
            }
        }
    }

    private fun handleBackPress() {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastBackPressTime) < BACK_PRESS_TIME_GAP_MILLIS) {
            finish()
        } else {
            lastBackPressTime = currentTime
            toast(AppStrings.msg_double_back_to_exit)
        }
    }

    companion object {
        private const val BACK_PRESS_TIME_GAP_MILLIS = 2000
    }
}
