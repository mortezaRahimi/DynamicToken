package com.myket.farahani.dynamictoken.utils.ext

import android.os.Environment
import com.myket.farahani.dynamictoken.presentation.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File


fun ResponseBody.saveFile(filePostfix: String): Flow<DownloadState> {
    return flow {
        emit(DownloadState.Downloading(0f))
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationFile = File(downloadFolder.absolutePath, "file_${filePostfix}.apk")


        try {
            byteStream().use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    val totalBytes = contentLength()
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var progressBytes = 0L

                    var bytes = inputStream.read(buffer)
                    while (bytes >= 0) {
                        outputStream.write(buffer, 0, bytes)
                        progressBytes += bytes
                        bytes = inputStream.read(buffer)
                        println("Progress bytes: ${((progressBytes * 100).toFloat() / totalBytes)}")
                        emit(DownloadState.Downloading(((progressBytes * 100).toFloat() / totalBytes)))
                    }
                }
            }
            emit(DownloadState.Finished(destinationFile.path))
        } catch (e: Exception) {
            emit(DownloadState.Failed(e))
        }
    }
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
}
