package com.myket.farahani.dynamictoken.presentation

sealed class AppEvent {

    data object OnDownloadButtonTap : AppEvent()

    data object OnAppDownloaded : AppEvent()

    data class OnCalcDataAdded(val points:List<Int>) : AppEvent()

    data class OnTokenAdded(val token:String):AppEvent()
}