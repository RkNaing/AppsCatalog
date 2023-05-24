package com.rkzmn.appscatalog.utils.app

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rkzmn.appscatalog.domain.model.AppComponentInfo
import com.rkzmn.appscatalog.ui.theme.seed
import com.rkzmn.appscatalog.utils.android.launch
import com.rkzmn.appscatalog.utils.android.toast

typealias AppDrawables = com.rkzmn.appscatalog.R.drawable
typealias AppStrings = com.rkzmn.appscatalog.R.string
typealias AppFonts = com.rkzmn.appscatalog.R.font


fun createCountLabelAnnotatedString(
    count: Int,
    label: String,
    countColor: Color = seed
) = buildAnnotatedString {
    val countLabel = count.toString()
    append(countLabel)
    append(" ")
    append(label)
    addStyle(
        SpanStyle(
            color = countColor,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
        ),
        start = 0,
        end = countLabel.length
    )
}

fun AppComponentInfo.launch(context: Context) {

    if (packageName == context.packageName) {
        context.toast(messageResId = AppStrings.msg_component_launched_already)
        return
    }

    context.toast(message = context.getString(AppStrings.lbl_launching_component, name))

    val isLaunched = context.launch(packageName, fqn)

    if (!isLaunched) {
        context.toast(message = context.getString(AppStrings.msg_component_launch_failed, name))
    }
}