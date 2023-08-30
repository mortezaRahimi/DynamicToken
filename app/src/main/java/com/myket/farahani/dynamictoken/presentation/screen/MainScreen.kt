package com.myket.farahani.dynamictoken.presentation.screen

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.myket.farahani.dynamictoken.presentation.MainViewModel
import com.myket.farahani.dynamictoken.presentation.components.FileDownloadScreen
import com.myket.farahani.dynamictoken.utils.DownloadController
import com.myket.farahani.dynamictoken.utils.UiEvent

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
            }
        }
    }

    FileDownloadScreen(
        viewModel = viewModel,
        onStartDownloadClicked = viewModel::downloadFile,
        onBackToIdleRequested = viewModel::onIdleRequested,
    )

}