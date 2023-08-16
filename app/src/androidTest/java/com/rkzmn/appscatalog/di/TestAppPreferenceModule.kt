package com.rkzmn.appscatalog.di

// @Module
// @TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [AppPreferencesModule::class]
// )
// class TestAppPreferenceModule {
//
//    @get:Rule
//    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Singleton
//    @Provides
//    fun provideDataStorePreferences(@ApplicationContext context: Context): DataStore<Preferences> {
//        val testDispatcher = UnconfinedTestDispatcher()
//        val testScope = TestScope(testDispatcher + Job())
//        return PreferenceDataStoreFactory.create(
//            scope = testScope,
//            produceFile = { tmpFolder.newFile("APPS_CATALOG_PREFS") }
//        )
//    }
// }
