package com.rkzmn.apps_data_provider.model

data class AppComponent(
    val name: String,
    val packageName: String,
    val fqn: String,
    val isPrivate: Boolean
) {
    enum class Type {
        SERVICE, ACTIVITY, BROADCAST
    }
}