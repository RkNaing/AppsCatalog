package com.rkzmn.appscatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.rkzmn.appscatalog.domain.model.AppTheme
import com.rkzmn.appscatalog.domain.repositories.AppPreferenceRepository
import com.rkzmn.appscatalog.navigation.graphs.RootNavGraph
import com.rkzmn.appscatalog.ui.theme.AppsCatalogTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPrefRepo: AppPreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val currentAppTheme by appPrefRepo.appTheme
                .collectAsStateWithLifecycle(initialValue = AppTheme.FOLLOW_SYSTEM)

            val isSystemInDarkTheme = isSystemInDarkTheme()
            val isDarkTheme by remember(currentAppTheme) {
                mutableStateOf(
                    when (currentAppTheme) {
                        AppTheme.LIGHT -> false
                        AppTheme.DARK -> true
                        AppTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme
                    }
                )
            }

            AppsCatalogTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false,
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    RootNavGraph(navHostController = rememberNavController())
                }
            }
        }
    }
}