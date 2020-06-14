package com.rk.appscatalog

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "Extensions"

fun Long?.toReadableTimestamp(): String? {

    if (this != null){
        try {
            val date = Date(this)
            val sdf = SimpleDateFormat("dd MMMM yyyy hh:mm:ss a",Locale.US)
            return sdf.format(date)
        } catch (e: Exception) {
            Log.e(TAG, "toReadableTimestamp: ", e)
        }
    }

    return null
}