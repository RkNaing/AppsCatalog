package com.rkzmn.appscatalog.presentation.apps.list

import androidx.compose.ui.text.AnnotatedString
import com.rkzmn.appscatalog.domain.mappers.displayName
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.utils.app.getHighlightedMatchingText

data class AppSearchResult(
    val appName: AnnotatedString? = null,
    val appIcon: String? = null,
    val packageName: AnnotatedString,
) {
    companion object {

        fun from(appInfo: AppInfo, query: String): AppSearchResult {
            return AppSearchResult(
                appName = appInfo.displayName?.let { getHighlightedMatchingText(it, query) },
                appIcon = appInfo.appIcon,
                packageName = getHighlightedMatchingText(appInfo.packageName, query)
            )
        }
    }
}
