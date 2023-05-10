package com.rkzmn.appscatalog.navigation.destination

object AppsListDestination : Destination(label = "apps")

object AppDetailDestination : Destination(
    label = "app_detail",
    arguments = arrayOf(ARG_PACKAGE_NAME)
)

///////////////////////////////////////////////////////////////////////////
// Arguments
///////////////////////////////////////////////////////////////////////////
const val ARG_PACKAGE_NAME = "arg_package_name"