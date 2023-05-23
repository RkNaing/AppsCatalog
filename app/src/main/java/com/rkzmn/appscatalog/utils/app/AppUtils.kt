package com.rkzmn.appscatalog.utils.app

import android.content.Context
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.utils.android.launch
import com.rkzmn.appscatalog.utils.android.toast

typealias AppDrawables = com.rkzmn.appscatalog.R.drawable
typealias AppStrings = com.rkzmn.appscatalog.R.string
typealias AppFonts = com.rkzmn.appscatalog.R.font

fun AppComponentInfo.launch(context: Context) {

    if (packageName == context.packageName){
        context.toast(messageResId = AppStrings.msg_component_launched_already)
        return
    }

    context.toast(message = context.getString(AppStrings.lbl_launching_component, name))

    val isLaunched = context.launch(packageName, fqn)

    if (!isLaunched) {
        context.toast(message = context.getString(AppStrings.msg_component_launch_failed, name))
    }
}