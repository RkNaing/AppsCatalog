package com.rkzmn.appscatalog.di

import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProvider
import com.rkzmn.appscatalog.utils.kotlin.CoroutineDispatcherProviderImpl
import com.rkzmn.appsdataprovider.AppDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl()
    }

    @Singleton
    @Provides
    fun provideAppDataProvider(): AppDataProvider = AppDataProvider.getInstance()
}
