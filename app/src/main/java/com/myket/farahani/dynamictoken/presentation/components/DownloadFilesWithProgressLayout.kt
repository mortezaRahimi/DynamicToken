package com.myket.farahani.dynamictoken.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DownloadFilesWithProgressLayout(progress: Float) {

    Text(
        text = "Downloaded ${(progress * 100).toInt()}%",
        style = MaterialTheme.typography.h6,
    )
    Spacer(modifier = Modifier.height(8.dp))
    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier.fillMaxWidth(),
    )
}