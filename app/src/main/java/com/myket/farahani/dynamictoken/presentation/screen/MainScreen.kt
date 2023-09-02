package com.myket.farahani.dynamictoken.presentation.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.myket.farahani.dynamictoken.presentation.AppEvent
import com.myket.farahani.dynamictoken.presentation.MainViewModel
import com.myket.farahani.dynamictoken.presentation.components.FileDownloadLayout
import com.myket.farahani.dynamictoken.utils.DownloadController
import com.myket.farahani.dynamictoken.utils.UiEvent
import java.io.File

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
) {
    val context = LocalContext.current
//    val downloadController = DownloadController(context, state.downloadLink, state.title)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }

                is UiEvent.ShowInstallView ->{
                    openNewVersion(event.path , context)
                }
            }
        }
    }

    FileDownloadLayout(
        viewModel = viewModel,
        onStartDownloadClicked = {viewModel.onEvent(AppEvent.OnDownloadButtonTap)},
    )

}
fun openNewVersion(location: String ,context:Context)  {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(
        getUriFromFile(location ,context),
        "application/vnd.android.package-archive"
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}

fun getUriFromFile(filePath: String , context: Context): Uri {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Uri.fromFile(File(filePath))
    } else {
        FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            File(filePath)
        )
    }
}