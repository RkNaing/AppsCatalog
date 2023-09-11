package com.rkzmn.appscatalog.utils.kotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * An abstraction layer for accessing [CoroutineDispatcher] for better testability.
 */
interface CoroutineDispatcherProvider {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

/**
 * A standard implementation of [CoroutineDispatcherProvider].
 *
 * @property main Defaults to [Dispatchers.Main]
 * @property default Defaults to [Dispatchers.Default]
 * @property io Defaults to [Dispatchers.IO]
 * @property unconfined Defaults to [Dispatchers.Unconfined]
 */
class StandardCoroutineDispatcherProvider(
    override val main: CoroutineDispatcher = Dispatchers.Main,
    override val default: CoroutineDispatcher = Dispatchers.Default,
    override val io: CoroutineDispatcher = Dispatchers.IO,
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined,
) : CoroutineDispatcherProvider
