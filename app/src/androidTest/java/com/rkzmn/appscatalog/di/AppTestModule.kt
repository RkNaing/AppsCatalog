package com.rkzmn.appscatalog.di

// @Module
// @TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [AppModule::class]
// )
// class AppTestModule {
//    @Provides
//    fun provideCoroutineDispatcher(): CoroutineDispatcherProvider {
//        Timber.d("provideCoroutineDispatcher(Test)")
//        return object : CoroutineDispatcherProvider {
//            override val main: CoroutineDispatcher = StandardTestDispatcher()
//            override val default: CoroutineDispatcher = StandardTestDispatcher()
//            override val io: CoroutineDispatcher = StandardTestDispatcher()
//            override val unconfined: CoroutineDispatcher = StandardTestDispatcher()
//        }
//    }
//
//    @Provides
//    fun provideAppDataProvider(): AppDataProvider = TestAppDataProvider()
// }
