package com.myket.farahani.dynamictoken.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.myket.farahani.dynamictoken.presentation.FileDownloadScreenState
import com.myket.farahani.dynamictoken.presentation.MainViewModel

@Composable
fun FileDownloadScreen(
    viewModel: MainViewModel,
    onStartDownloadClicked: () -> Unit,
    onBackToIdleRequested: () -> Unit,
) {

    val downloadState = viewModel.downloadState
    val appState = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .wrapContentSize(Alignment.Center)
            .padding(16.dp)
    ) {

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

            when (downloadState) {
                FileDownloadScreenState.Idle -> {
                    Button(onClick = onStartDownloadClicked) {
                        Text(
                            text = "Download",
                            style = MaterialTheme.typography.button,
                        )
                    }
                }

                is FileDownloadScreenState.Downloading -> {
                    DownloadFilesWithProgressLayout(progress = downloadState.progress)
                }
                is FileDownloadScreenState.Failed -> {
                    Text(
                        text = "Download failed",
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onBackToIdleRequested) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.button,
                        )
                    }
                }
                FileDownloadScreenState.Downloaded -> {
                    Text(
                        text = "Download succeeded",
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onBackToIdleRequested) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.button,
                        )
                    }
                }
                else -> {}
            }
        }

    }
}

