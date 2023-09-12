package com.myket.farahani.dynamictoken.presentation.blur.blurp

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.myket.farahani.dynamictoken.R


@ExperimentalPermissionsApi
@Composable
fun BlurScreen(
    viewModel: BlurViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    val resources: Resources = context.resources
    val uri = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(
            R.drawable.android_cupcake
        ) + '/' + resources.getResourceTypeName(R.drawable.android_cupcake) + '/' + resources.getResourceEntryName(
            R.drawable.android_cupcake
        )
    )

    state.imageUri = uri


    val workerResult = viewModel.workId?.let { id ->
        viewModel.workManager.getWorkInfoByIdLiveData(id).observeAsState().value
    }


    LaunchedEffect(key1 = workerResult?.outputData) {
        if (workerResult?.outputData != null) {
            val filePath = workerResult.outputData.getString(
                PhotoCompressionWorker.KEY_RESULT_PATH
            )
            filePath?.let {
                val bitmap = BitmapFactory.decodeFile(it)
                viewModel.updateCompressedBitmap(bitmap)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Image(
            painterResource(R.drawable.android_cupcake),
            contentDescription = "cake",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select Blur Amount:")

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = state.littleActive,
                onClick = { viewModel.onEvent(BlurEvent.Little) })
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "a little blurred")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = state.moreActive,
                onClick = { viewModel.onEvent(BlurEvent.More) })
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "more blurred")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = state.mostActive,
                onClick = { viewModel.onEvent(BlurEvent.Most) })
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "the most blurred")
        }
        Spacer(modifier = Modifier.height(16.dp))

        viewModel.compressedBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "new cake",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        if (state.loading)
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
    }
}


