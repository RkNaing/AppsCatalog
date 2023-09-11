package com.rkzmn.appscatalog.di

import android.content.Context
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import com.rkzmn.appscatalog.utils.kotlin.StandardCoroutineDispatcherProvider
import com.rkzmn.appsdataprovider.AppDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcherProvider {
        return StandardCoroutineDispatcherProvider()
    }

    @Singleton
    @Provides
    fun provideAppDataProvider(@ApplicationContext context: Context): AppDataProvider =
        AppDataProvider.getInstance(context)
}
