package com.rkzmn.appscatalog.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.rkzmn.appscatalog.utils.app.Drawables

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    iconPath: String?,
    contentDescription: String? = null,
) {
    val placeholder = painterResource(id = Drawables.ic_android_seed_24dp)
    AsyncImage(
        model = iconPath,
        contentDescription = contentDescription,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        modifier = modifier
    )
}