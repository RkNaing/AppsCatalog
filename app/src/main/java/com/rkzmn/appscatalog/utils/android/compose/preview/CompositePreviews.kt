package com.rkzmn.appscatalog.utils.android.compose.preview

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    group = "Night(Dark) Mode",
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    group = "Day(Light) Mode",
    uiMode = UI_MODE_NIGHT_NO,
    showBackground = true
)
annotation class UiModePreviews