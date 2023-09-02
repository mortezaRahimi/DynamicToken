package com.myket.farahani.dynamictoken.presentation

sealed class DownloadState {
    data class Downloading(val progress: Float) : DownloadState()
    data class Finished(val destionation: String) : DownloadState()
    data class Failed(val error: Throwable? = null) : DownloadState()
}
