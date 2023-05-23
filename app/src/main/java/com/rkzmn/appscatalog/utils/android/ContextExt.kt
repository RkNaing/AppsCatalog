package com.rkzmn.appscatalog.utils.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import timber.log.Timber

fun Context.launch(
    packageName: String,
    componentFqn: String
): Boolean {
    return try {
        val intent = Intent()
        intent.component = ComponentName(
            packageName,
            componentFqn
        )
        startActivity(intent)
        true
    } catch (e: Exception) {
        Timber.e(e, "launch: Failed to launch $componentFqn")
        false
    }
}

fun Context.toast(
    @StringRes messageResId: Int,
    length: Int = Toast.LENGTH_SHORT
) = Toast.makeText(this, messageResId, length).show()

fun Context.toast(
    message: String,
    length: Int = Toast.LENGTH_SHORT
) = Toast.makeText(this, message, length).show()