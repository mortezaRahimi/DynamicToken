package com.myket.farahani.dynamictoken.presentation.blur.blurp

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.work.*
import com.myket.farahani.dynamictoken.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class BlurViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {

    var state by mutableStateOf(BlurState())
    val workManager = WorkManager.getInstance(application)

    var uncompressedUri: Uri? by mutableStateOf(null)
        private set

    var compressedBitmap: Bitmap? by mutableStateOf(null)
        private set

    var workId: UUID? by mutableStateOf(null)
        private set

    fun updateUncompressUri(uri: Uri?) {
        uncompressedUri = uri
    }

    fun updateCompressedBitmap(bmp: Bitmap?) {
        compressedBitmap = bmp
    }

    fun updateWorkId(uuid: UUID?) {
        workId = uuid
    }


    fun blur() {

        updateUncompressUri(state.imageUri)

        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>()
            .setInputData(
                workDataOf(
                    PhotoCompressionWorker.KEY_CONTENT_URI to state.imageUri.toString(),
                    PhotoCompressionWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 20L,
                    PhotoCompressionWorker.KEY_LEVEL to 1
                )
            )
            .build()
        updateWorkId(request.id)
        workManager.enqueue(request)
    }


    init {
        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        state.imageUri = getImageUri(application.applicationContext)
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()
    }


    internal fun setOutputUri(outputImageUri: String?) {
        state.outputUri = uriOrNull(outputImageUri)

        state.bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, state.outputUri)
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    fun onEvent(blurEvent: BlurEvent) {
        when (blurEvent) {

            is BlurEvent.Finished -> {
                state = state.copy(loading = false)
            }

            is BlurEvent.Loading -> {
                state = state.copy(loading = true)
            }

            is BlurEvent.Little -> {
                state = state.copy(
                    name = "little",
                    littleActive = true,
                    moreActive = false,
                    mostActive = false,
                    level = 1
                )
                blur()
            }

            is BlurEvent.More -> {
                state = state.copy(
                    name = "more",
                    littleActive = false,
                    moreActive = true,
                    mostActive = false,
                    level = 2
                )
                blur()
            }

            is BlurEvent.Most -> {
                state = state.copy(
                    name = "most",
                    littleActive = false,
                    moreActive = false,
                    mostActive = true,
                    level = 3
                )
                blur()
            }
        }
    }
}


data class BlurState(
    val name: String = "little",
    var littleActive: Boolean = false,
    var moreActive: Boolean = false,
    var mostActive: Boolean = false,
    val level: Int = 1,
    val loading: Boolean = false,
    var imageUri: Uri? = null,
    var outputUri: Uri? = null,
    var bitmap: Bitmap? = null
)