package com.rkzmn.appscatalog.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rkzmn.appscatalog.ui.theme.spacingMedium
import com.rkzmn.appscatalog.utils.android.compose.preview.UiModePreviews

@Composable
fun ProgressView(
    message: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            spacingMedium,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()

        if (!message.isNullOrBlank()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@UiModePreviews
@Composable
fun ProgressViewPreview() {
    ThemedPreview(modifier = Modifier.fillMaxSize()) {
        ProgressView(
            modifier = Modifier.fillMaxSize(),
            message = "Cooking something delicious"
        )
    }
}
