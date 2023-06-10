package com.rkzmn.appscatalog.presentation.apps.list.states

import com.rkzmn.appscatalog.presentation.apps.list.AppSearchResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AppsSearchState(
    val query: String = "",
    val results: ImmutableList<AppSearchResult> = persistentListOf(),
    val isActive: Boolean = false,
)
