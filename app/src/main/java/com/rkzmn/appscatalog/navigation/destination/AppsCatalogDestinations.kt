package com.rkzmn.appscatalog.navigation.destination

import com.rkzmn.appscatalog.navigation.ARG_PACKAGE_NAME

object AppsListDestination : Destination(label = "apps")

object AppDetailDestination : Destination(
    label = "app_detail",
    arguments = arrayOf(ARG_PACKAGE_NAME)
) {
    fun getAddress(packageName: String): String {
        return address(
            args = mapOf(
                ARG_PACKAGE_NAME to packageName
            )
        )
    }
}

object AppSettingsDestination: Destination(label = "app_setting")

///////////////////////////////////////////////////////////////////////////
// Arguments
///////////////////////////////////////////////////////////////////////////
