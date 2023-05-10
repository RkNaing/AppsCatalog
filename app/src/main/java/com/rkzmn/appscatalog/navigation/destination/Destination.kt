package com.rkzmn.appscatalog.navigation.destination

sealed class Destination(
    val label: String,
    val arguments: Array<String>? = null,
    val optionalArguments: Array<String>? = null
) {

    val route: String
        get() = StringBuilder(label).apply {
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

    fun address(
        args: Map<String, Any?>? = null,
        valueTransformer: (String, Any) -> String = { _, value -> value.toString() }
    ): String {
        var address: String = route

        args?.forEach { (key, value) ->
            value?.let {
                address = address.replace("{${key}}", valueTransformer(key, value))
            }
        }

        return address
    }
}