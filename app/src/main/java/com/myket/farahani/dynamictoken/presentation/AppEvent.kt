package com.myket.farahani.dynamictoken.presentation


sealed class AppEvent {


    data class OnCalcDataAdded(val points: ArrayList<Int>) : AppEvent()

    data class OnTokenAdded(val token: String) : AppEvent()


    data object OnDownloadButtonTap : AppEvent()

    object Idle : AppEvent()
    data class Downloading(val progress: Int) : AppEvent()
    data class Failed(val error: Throwable? = null) : AppEvent()
    data class Downloaded(val destination:String) : AppEvent()

}
