package com.myket.farahani.dynamictoken.presentation

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myket.farahani.dynamictoken.domain.use_case.DynamicTokenUseCase
import com.myket.farahani.dynamictoken.utils.UiEvent
import com.myket.farahani.dynamictoken.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: DynamicTokenUseCase
) : ViewModel() {

    var state by mutableStateOf(AppState())
        private set
    var downloadState by mutableStateOf<FileDownloadScreenState>(FileDownloadScreenState.Idle)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        getCalculationData()
    }

    fun onEvent(event: AppEvent) {
        when (event) {

            is AppEvent.OnDownloadButtonTap -> {
                if (state.tokenAdded) {
                    state = state.copy(
                        downloading = true
                    )
                }

            }

            is AppEvent.OnAppDownloaded -> {
                state = state.copy(
                    downloading = false
                )
            }

            is AppEvent.OnCalcDataAdded -> {
                getToken(maxProfit(event.points, 0, event.points.size - 1))
            }

            is AppEvent.OnTokenAdded -> {
                getAppData(event.token)
            }
        }
    }

    private fun getAppData(token: String) {
        viewModelScope.launch {
            useCase.getAppData(token)
                .onSuccess {
                    state = state.copy(
                        title = it.title,
                        icon = it.icon,
                        downloadLink = it.url
                    )
                }
                .onFailure {
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.DynamicString(it.message.toString())
                        )
                    )
                }
        }
    }

    private fun getToken(maxProfit: Int) {
        viewModelScope.launch {
            useCase.getToken(maxProfit.toString())
                .onSuccess {
                    state = state.copy(
                        token = it,
                        tokenAdded = true
                    )
                    onEvent(AppEvent.OnTokenAdded(it))
                }
                .onFailure {
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.DynamicString(it.message.toString())
                        )
                    )
                }
        }
    }

    private fun getCalculationData() {
        viewModelScope.launch {
            useCase.getCalcData()
                .onSuccess {
                    state = state.copy(
                        calcData = it as ArrayList
                    )
                    onEvent(AppEvent.OnCalcDataAdded(state.calcData))
                }
                .onFailure {
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.DynamicString(it.message.toString())
                        )
                    )
                }
        }
    }

    private fun maxProfit(price: ArrayList<Int>, start: Int, end: Int): Int {
        if (end <= start) return 0
        var profit = 0
        for (i in start until end) {

            for (j in i + 1..end) {

                if (price[j] > price[i]) {
                    val currProfit = (price[j] - price[i] + maxProfit(price, start, i - 1)
                            + maxProfit(price, j + 1, end))

                    profit = max(profit, currProfit)
                }
            }
        }
        return profit
    }

    fun downloadFile() {
        viewModelScope.launch(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            useCase.downloadFile(state.downloadLink).onSuccess {
                it.saveFile(timestamp.toString())
                    .collect { mdownloadState ->
                        downloadState = when (mdownloadState) {
                            is DownloadState.Downloading -> {
                                FileDownloadScreenState.Downloading(progress = mdownloadState.progress)
                            }
                            is DownloadState.Failed -> {
                                FileDownloadScreenState.Failed(error = mdownloadState.error)
                            }
                            DownloadState.Finished -> {
                                FileDownloadScreenState.Downloaded
                            }
                        }
                    }
            }
        }
    }

    fun onIdleRequested() {
        downloadState =   FileDownloadScreenState.Idle
    }

    private sealed class DownloadState {
        data class Downloading(val progress: Int) : DownloadState()
        object Finished : DownloadState()
        data class Failed(val error: Throwable? = null) : DownloadState()
    }


    private fun ResponseBody.saveFile(filePostfix: String): Flow<DownloadState> {
        return flow {
            emit(DownloadState.Downloading(0))
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
                            emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                        }
                    }
                }
                emit(DownloadState.Finished)
            } catch (e: Exception) {
                emit(DownloadState.Failed(e))
            }
        }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }


}

