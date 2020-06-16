package com.rk.appscatalog

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "Extensions"

fun Long?.toReadableTimestamp(): String? =
    if (this != null) Date(this).toReadableTimestamp() else null

fun Date?.toReadableTimestamp(): String? {

    if (this != null) {
        try {
            val sdf = SimpleDateFormat("dd MMMM yyyy hh:mm:ss a", Locale.US)
            return sdf.format(this)
        } catch (e: Exception) {
            Log.e(TAG, "toReadableTimestamp: ", e)
        }
    }

    return null
}

fun String?.defaultOnNullOrEmpty(default: String = "-"): String =
    if (this == null || this.isEmpty() || this.isBlank()) default else this