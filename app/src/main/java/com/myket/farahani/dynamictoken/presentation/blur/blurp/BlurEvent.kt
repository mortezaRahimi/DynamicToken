package com.myket.farahani.dynamictoken.presentation.blur.blurp

sealed class BlurEvent {
    object Little : BlurEvent()
    object More : BlurEvent()
    object Most : BlurEvent()
    object Finished : BlurEvent()
    object Loading : BlurEvent()
}
