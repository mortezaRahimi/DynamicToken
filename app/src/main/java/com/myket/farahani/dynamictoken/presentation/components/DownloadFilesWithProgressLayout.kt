package com.myket.farahani.dynamictoken.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
 fun DownloadFilesWithProgressLayout(progress: Int) {
    Text(
        text = "Downloaded $progress%",
        style = MaterialTheme.typography.h6,
    )
    Spacer(modifier = Modifier.height(8.dp))
    LinearProgressIndicator(
        progress = progress.toFloat() / 100f,
        modifier = Modifier.fillMaxWidth(),
    )
}