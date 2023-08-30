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
import kotlin.math.max

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
                getToken(maxProfit(event.points, 0, event.points.size - 1))
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
        viewModelScope.launch {
            useCase.getToken(maxProfit.toString())
                .onSuccess {
                    state = state.copy(
                        token = it
                    )
                    onEvent(AppEvent.OnTokenAdded(it))
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.DynamicString(it)
                        )
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


}

