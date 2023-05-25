package com.rkzmn.appscatalog.utils.kotlin

import kotlinx.coroutines.flow.MutableStateFlow
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import java.util.Locale

/**
 * Returns a human-readable version of the file size, where the input represents a specific number of bytes.
 *
 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is rounded down to the
 * nearest GB boundary.
 *
 * @param bytes the number of bytes
 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
 */
fun byteCountToDisplaySize(bytes: Long): String {
    if (-1000 < bytes && bytes < 1000) {
        return "$bytes B"
    }
    val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
    var value = bytes
    while (value <= -999950 || value >= 999950) {
        value /= 1000
        ci.next()
    }
    return "%.1f %cB".format(locale = Locale.ENGLISH, value / 1000.0, ci.current())
}

suspend inline fun <T> MutableStateFlow<T>.emitUpdate(function: (T) -> T) = emit(function(value))
