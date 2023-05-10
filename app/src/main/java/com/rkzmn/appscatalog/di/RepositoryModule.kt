package com.rkzmn.appscatalog.di

import com.rkzmn.appscatalog.data.repositories.AndroidAppDataRepository
import com.rkzmn.appscatalog.domain.repositories.AppDataRepository
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
    abstract fun bindAppDataRepository(impl: AndroidAppDataRepository): AppDataRepository

}