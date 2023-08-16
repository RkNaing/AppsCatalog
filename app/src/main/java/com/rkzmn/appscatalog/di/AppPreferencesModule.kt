package com.rkzmn.appscatalog.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppPreferencesModule {

    private val Context.appDataStore by preferencesDataStore("APPS_CATALOG_PREFS")

    @Singleton
    @Provides
    fun provideDataStorePreferences(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.appDataStore
}
