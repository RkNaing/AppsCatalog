package com.rkzmn.appscatalog.navigation.destination

import androidx.annotation.VisibleForTesting

sealed class Destination(
    val label: String,
    val arguments: Array<String>? = null,
    val optionalArguments: Array<String>? = null
) {

    val route: String
        get() = buildRoute(label, arguments, optionalArguments)

    fun address(
        args: Map<String, Any?>? = null,
        valueTransformer: (String, Any) -> String = { _, value -> value.toString() }
    ): String {
        var address: String = route

        args?.forEach { (key, value) ->
            value?.let {
                address = address.replace("{$key}", valueTransformer(key, value))
            }
        }

        return address
    }

    override fun toString(): String {
        return route
    }

    companion object {
        @VisibleForTesting
        fun buildRoute(
            label: String,
            arguments: Array<String>? = null,
            optionalArguments: Array<String>? = null
        ) = StringBuilder(label).apply {
            arguments?.let { args ->
                args.forEach { arg ->
                    append("/{$arg}")
                }
            }
            optionalArguments?.let { args ->
                append("?")
                append(args.joinToString("&", transform = { "$it={$it}" }))
            }
        }.toString()
    }
}
