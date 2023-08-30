package com.myket.farahani.dynamictoken.presentation

data class AppState(
    val token: String = "",
    val title: String = "",
    val icon: String = "",
    val downloadLink: String = "",
    val calcData: ArrayList<Int> = arrayListOf(),
    val downloading: Boolean = false,
    val downloadProgress: Float = 0.0f,
    val tokenAdded: Boolean = false,
)
