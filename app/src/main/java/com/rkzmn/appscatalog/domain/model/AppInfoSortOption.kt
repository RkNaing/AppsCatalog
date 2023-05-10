package com.rkzmn.appscatalog.domain.model

class AppInfoSortOption(
    val property: Property = Property.NAME,
    val isDescending: Boolean = false,
) {
    enum class Property {
        NAME, INSTALLED_DATE, LAST_UPDATED, SIZE, LAST_USED
    }

    override fun toString(): String {
        return "$property (${if (isDescending) "DESC" else "ASC"})"
    }
}