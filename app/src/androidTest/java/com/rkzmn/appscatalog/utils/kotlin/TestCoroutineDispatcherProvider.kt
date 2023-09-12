package com.rkzmn.appscatalog.utils.kotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
class TestCoroutineDispatcherProvider(
    var testDispatcher: TestDispatcher = StandardTestDispatcher()
) : CoroutineDispatcherProvider {
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val unconfined: CoroutineDispatcher
        get() = testDispatcher
}
