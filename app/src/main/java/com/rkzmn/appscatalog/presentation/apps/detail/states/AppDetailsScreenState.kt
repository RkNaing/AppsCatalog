package com.rkzmn.appscatalog.presentation.apps.detail.states

import com.rkzmn.appscatalog.domain.model.AppDetails

data class AppDetailsScreenState(
    val isLoading: Boolean = false,
    val details: AppDetails? = null,
)
