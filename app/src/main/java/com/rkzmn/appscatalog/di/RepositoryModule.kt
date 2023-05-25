package com.rkzmn.appscatalog.di

import com.rkzmn.appscatalog.data.repositories.AndroidAppDataRepository
import com.rkzmn.appscatalog.data.repositories.AppPreferenceRepositoryImpl
import com.rkzmn.appscatalog.data.repositories.ListPreferenceRepositoryImpl
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
import com.rkzmn.appscatalog.domain.repositories.AppPreferenceRepository
import com.rkzmn.appscatalog.domain.repositories.ListPreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindAppDataRepository(
        impl: AndroidAppDataRepository
    ): AppDataRepository

    @Binds
    abstract fun bindListPreferenceRepository(
        impl: ListPreferenceRepositoryImpl
    ): ListPreferenceRepository

    @Binds
    abstract fun bindAppPreferenceRepository(
        impl: AppPreferenceRepositoryImpl
    ): AppPreferenceRepository
} // Note on IDE showing as unused code : https://stackoverflow.com/a/68828417
