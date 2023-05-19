package com.rkzmn.appscatalog.domain.model

data class AppComponentInfo(
    val name: String,
    val packageName: String,
    val fqn: String,
    val isPrivate: Boolean,
)
