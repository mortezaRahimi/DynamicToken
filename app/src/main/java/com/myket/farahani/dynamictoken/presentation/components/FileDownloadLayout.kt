package com.myket.farahani.dynamictoken.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.myket.farahani.dynamictoken.presentation.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun FileDownloadLayout(
    viewModel: MainViewModel,
    onStartDownloadClicked: () -> Unit,
) {
    val appState = viewModel.state

    val progress by remember { mutableStateOf(0) }
    val animatedProgress by animateFloatAsState(
        targetValue = appState.downloadProgress / 100f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    LaunchedEffect(key1 = progress, block = {
        delay(1_000)
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .wrapContentSize(Alignment.Center)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(appState.icon),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = appState.title, style = MaterialTheme.typography.body2)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onStartDownloadClicked) {
            Text(
                text = if (appState.downloading) "Downloading" else "Download",
                style = MaterialTheme.typography.button,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.state.downloading)
            DownloadFilesWithProgressLayout(progress = animatedProgress)

    }
}

