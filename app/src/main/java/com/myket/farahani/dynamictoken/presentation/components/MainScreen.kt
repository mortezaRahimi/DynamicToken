package com.myket.farahani.dynamictoken.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.myket.farahani.dynamictoken.presentation.AppEvent
import com.myket.farahani.dynamictoken.presentation.MainViewModel
import com.myket.farahani.dynamictoken.utils.DownloadController
import com.myket.farahani.dynamictoken.utils.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {
    val context = LocalContext.current
    val state = viewModel.state
    val downloadController = DownloadController(context, state.downloadLink, state.title)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(state.icon),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = state.title, style = MaterialTheme.typography.body2)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onEvent(AppEvent.OnDownloadButtonTap)
                downloadController.enqueueDownload()
            },
            shape = RoundedCornerShape(size = 10.dp),
        ) {
            Text(text = if (state.downloading) "Downloading" else "Download")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            state.downloadProgress, color = Color.Blue,
        )

    }


}