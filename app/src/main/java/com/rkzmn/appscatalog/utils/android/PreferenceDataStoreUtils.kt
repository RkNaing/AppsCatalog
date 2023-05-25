package com.rkzmn.appscatalog.utils.android

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Datastore utility methods for observing, retrieving and setting values to a specific preferences key.
 * [References](https://tinyurl.com/2kj45u5b)
 */

// /////////////////////////////////////////////////////////////////////////
// Utilities for observing values
// /////////////////////////////////////////////////////////////////////////

/**
 * Watches value changes of a specific [Preferences.Key]
 *
 * @param T Type of data to retrieve
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @param defaultValue The default value of the retrieved is missing or any failure occurs.
 * @return [Flow] of type [T]
 */
@JvmName("watchWithDefaultValue")
fun <T> Preferences.Key<T>.watchValueWithDefault(
    dataStore: DataStore<Preferences>,
    defaultValue: T,
): Flow<T> =
    dataStore.data
        .catchAndHandleError()
        .map { preferences -> preferences[this] ?: defaultValue }

/**
 * Watches value changes of a specific [Preferences.Key]
 *
 * @param T Type of data to retrieve
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @return [Flow] of type **nullable** [T]. The emitted value will be **null** if
 * the retrieved is missing or any failure occurs.
 */
@JvmName("watchWithNullAsDefaultValue")
fun <T> Preferences.Key<T>.watchValue(dataStore: DataStore<Preferences>): Flow<T?> =
    dataStore.data
        .catchAndHandleError()
        .map { preferences -> preferences[this] }

/**
 * Watches value changes of a specific [Preferences.Key]
 *
 * @param T Data type that the [Preferences.Key] holds.
 * @param M Destination data type that the retrieved [T] will be converted to.
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @param mapper [T] to [M] Converter
 * @return [Flow] of type **nullable** [M]. The emitted value will be **null** if
 * the retrieved is missing or any failure occurs.
 */
@JvmName("watchWithValueConverter")
fun <T, M> Preferences.Key<T>.watchValueWithMapper(
    dataStore: DataStore<Preferences>,
    mapper: (T?) -> M?,
): Flow<M?> = dataStore.data
    .catchAndHandleError()
    .map { preferences -> mapper(preferences[this]) }

/**
 * Watches value changes of a specific [Preferences.Key]
 *
 * @param T Data type that the [Preferences.Key] holds.
 * @param M Destination data type that the retrieved [T] will be converted to.
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @param mapper [T] to [M] Converter
 * @return [Flow] of type **non-null** [M].
 */
@JvmName("watchWithNonNullValueConverter")
fun <T, M> Preferences.Key<T>.watchValueWithNonNullMapper(
    dataStore: DataStore<Preferences>,
    mapper: (T?) -> M,
): Flow<M> = dataStore.data
    .catchAndHandleError()
    .map { preferences -> mapper(preferences[this]) }

// /////////////////////////////////////////////////////////////////////////
// Utilities for getting values
// /////////////////////////////////////////////////////////////////////////
/**
 * Retrieve value of a specific [Preferences.Key]
 *
 * @param T Data type that the [Preferences.Key] holds.
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @param defaultValue The default value of the retrieved is missing or any failure occurs.
 * @return The retrieved value of type [T] or [defaultValue].
 */
suspend fun <T> Preferences.Key<T>.getValueOrDefault(
    dataStore: DataStore<Preferences>,
    defaultValue: T,
): T = dataStore.data
    .catchAndHandleError()
    .map { preferences -> preferences[this] }
    .firstOrNull() ?: defaultValue

/**
 * Retrieve value of a specific [Preferences.Key]
 *
 * @param T Data type that the [Preferences.Key] holds.
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @return The retrieved value of type [T] or **null** if the retrieved is missing or any failure occurs..
 */
suspend fun <T> Preferences.Key<T>.getValueOrNull(dataStore: DataStore<Preferences>) =
    dataStore.data
        .catchAndHandleError()
        .map { preferences -> preferences[this] }
        .firstOrNull()

/**
 * Retrieve value of a specific [Preferences.Key]
 *
 * @param T Data type that the [Preferences.Key] holds.
 * @param M Destination data type that the retrieved [T] will be converted to.
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @param mapper [T] to [M] Converter
 * @return The retrieved value of type **nullable** [M]. The returned value will be **null** if
 * the retrieved is missing or any failure occurs.
 */
suspend fun <T, M> Preferences.Key<T>.getValueWithMapper(
    dataStore: DataStore<Preferences>,
    mapper: (T?) -> M?,
): M? = mapper(getValueOrNull(dataStore))

/**
 * Retrieve value of a specific [Preferences.Key]
 *
 * @param T Data type that the [Preferences.Key] holds.
 * @param M Destination data type that the retrieved [T] will be converted to.
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @param mapper [T] to [M] Converter
 * @return The retrieved value of type **nullable** [M]. The returned value will be **null** if
 * the retrieved is missing or any failure occurs.
 */
suspend fun <T, M> Preferences.Key<T>.getValueWithNonNullMapper(
    dataStore: DataStore<Preferences>,
    mapper: (T?) -> M,
): M = mapper(getValueOrNull(dataStore))

// /////////////////////////////////////////////////////////////////////////
// Utilities for setting values
// /////////////////////////////////////////////////////////////////////////

/**
 * Sets [value] to a specific [Preferences.Key].
 * Passing **null** [value] will remove the [Preferences.Key]!
 *
 * @param T Data type that the [Preferences.Key] holds.
 * @param dataStore Source [DataStore] [Preferences] to retrieve the data from.
 * @param value The data to be set.
 */
suspend fun <T> Preferences.Key<T>.setValue(dataStore: DataStore<Preferences>, value: T?) {
    dataStore.edit { preferences ->
        if (value == null) {
            preferences.remove(this)
        } else {
            preferences[this] = value
        }
    }
}

/**
 *
 * @param[key] Key to remove
 * @receiver[DataStore] of [Preferences]
 */
suspend fun DataStore<Preferences>.remove(key: Preferences.Key<*>) {
    edit { preferences -> preferences.remove(key) }
}

/**
 *
 * @param[key] Key to remove
 * @receiver[DataStore] of [Preferences]
 */
suspend fun DataStore<Preferences>.remove(filter: (Preferences.Key<*>) -> Boolean) {
    edit { preferences ->
        preferences.asMap()
            .filter { filter(it.key) }.keys
            .forEach {
                val deletedValue = preferences.remove(it)
                Timber.d("remove: Deleted $it = $deletedValue")
            }
    }
}

/**
 * Catches and logs errors emitted by [Flow] of [Preferences].
 *
 * @return [Flow] of [Preferences] (Fluent API)
 */
private fun Flow<Preferences>.catchAndHandleError(): Flow<Preferences> {
    this.catch { exception ->
        Timber.e(exception)
        emit(emptyPreferences())
    }
    return this@catchAndHandleError
}
