package com.myket.farahani.dynamictoken.utils

sealed class UiEvent {
    data class ShowSnackbar(val message: UiText) : UiEvent()

    data class ShowInstallView(val path:String):UiEvent()

}
