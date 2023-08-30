package com.myket.farahani.dynamictoken.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myket.farahani.dynamictoken.domain.use_case.DynamicTokenUseCase
import com.myket.farahani.dynamictoken.utils.UiEvent
import com.myket.farahani.dynamictoken.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: DynamicTokenUseCase
) : ViewModel() {

    var state by mutableStateOf(AppState())

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
                getToken(calculateMaxProfit(event.points as ArrayList<Int>))
            }

            is AppEvent.OnTokenAdded -> {
                getAppData(event.token)
            }

            else -> {}
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
        state = state.copy(
            token = "FiaSIsImR0IjozOTMzMzQ4MjYsImgiOiIz"
        )
        onEvent(AppEvent.OnTokenAdded("FiaSIsImR0IjozOTMzMzQ4MjYsImgiOiIz"))

//        viewModelScope.launch {
//            useCase.getToken(maxProfit.toString())
//                .onSuccess {
//                    state = state.copy(
//                        token = it
//                    )
//                    onEvent(AppEvent.OnTokenAdded(it))
//                    _uiEvent.send(
//                        UiEvent.ShowSnackbar(
//                            UiText.DynamicString(it)
//                        )
//                    )
//                }
//                .onFailure {
//                    _uiEvent.send(
//                        UiEvent.ShowSnackbar(
//                            UiText.DynamicString(it.message.toString())
//                        )
//                    )
//                }
//        }
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

    private fun calculateMaxProfit(points: ArrayList<Int>): Int {
        var tempList = points
        var maxList = arrayListOf<Int>()

        for (i in tempList) {
            for (j in tempList.subList(1, tempList.size)) {
                if (i < j && tempList.indexOf(j) > tempList.indexOf(i)) {
                    maxList.add(j - i)
                }
            }
        }

        return maxList.max()
    }


}

