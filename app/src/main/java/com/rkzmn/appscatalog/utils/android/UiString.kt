package com.rkzmn.appscatalog.utils.android

import android.content.Context
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * A simple wrapper for representing UI text content with plain [String] and [StringRes]
 */
sealed class UiString : Parcelable {

    @Composable
    abstract fun asString(): String

    abstract fun asString(context: Context): String

    @Parcelize
    data class DynamicString(private val value: String) : UiString() {

        @Composable
        override fun asString(): String = value

        override fun asString(context: Context): String = value

    }

    @Parcelize
    class StringResource(
        @StringRes private val resId: Int,
        private vararg val args: @RawValue Any
    ) : UiString() {

        @Composable
        override fun asString(): String = if (args.isNotEmpty()) {
            stringResource(id = resId, args)
        } else {
            stringResource(id = resId)
        }

        override fun asString(context: Context): String = if (args.isNotEmpty()) {
            context.getString(resId, args)
        } else {
            context.getString(resId)
        }

    }

    @Parcelize
    class PluralsResource(
        @PluralsRes private val resId: Int,
        private val count: Int,
        private vararg val args: @RawValue Any
    ) : UiString() {

        @Composable
        override fun asString(): String = if (args.isNotEmpty()) {
            pluralStringResource(id = resId, count = count, formatArgs = args)
        } else {
            pluralStringResource(id = resId, count = count)
        }

        override fun asString(context: Context): String = if (args.isNotEmpty()) {
            context.resources.getQuantityString(resId, count, args)
        } else {
            context.resources.getQuantityString(resId, count)
        }

    }

    companion object {
        val empty = DynamicString("")

        fun from(string: String): UiString = DynamicString(string)

        fun plurals(@PluralsRes resId: Int, count: Int, vararg args: @RawValue Any): UiString =
            PluralsResource(resId = resId, count = count, args = args)

        fun from(@StringRes stringRes: Int, vararg args: @RawValue Any): UiString =
            StringResource(stringRes, args)
    }
}