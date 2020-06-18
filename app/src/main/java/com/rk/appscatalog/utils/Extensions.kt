package com.rk.appscatalog.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
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

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layout, this, attachToRoot)

fun AppCompatTextView.setTextAsync(text: CharSequence?) {
    text?.let {
        val textMetricsParams = TextViewCompat.getTextMetricsParams(this)
        val futureParam = PrecomputedTextCompat.getTextFuture(it, textMetricsParams, null)
        setTextFuture(futureParam)
    } ?: setText(null)
}

fun View.makeGone() {
    applyVisibility(View.GONE)
}

fun View.makeVisible() {
    applyVisibility(View.VISIBLE)
}

fun View.makeInvisible() {
    applyVisibility(View.INVISIBLE)
}

private fun View.applyVisibility(targetVisibility: Int) {
    if (visibility != targetVisibility) {
        visibility = targetVisibility
    }
}
