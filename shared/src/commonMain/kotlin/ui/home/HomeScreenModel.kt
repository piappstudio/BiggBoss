package ui.home

import analytics.AnalyticLogger
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.ShowList
import network.PIError
import network.PiRepository
import network.Resource
import ui.native.LinkLauncher

/**
 * Defined all possible states here
 * */
sealed class HomeScreenState {
    data object Loading : HomeScreenState()
    data class Error(val piError: PIError) : HomeScreenState()
    data class Success(val showList: ShowList?) : HomeScreenState()
}

/**
 * Additional data states to hold all possible data
 * */

private data class HomeState(
    val isLoading: Boolean = false,
    val piError: PIError? = null,
    val showList: ShowList? = null
) {
    fun toUiState(): HomeScreenState {
        if (isLoading) {
            return HomeScreenState.Loading
        } else if (piError != null) {
            return HomeScreenState.Error(piError)
        } else {
            return HomeScreenState.Success(showList)
        }
    }
}

class HomeScreenModel(private val piRepository: PiRepository, val linkLauncher: LinkLauncher, val analyticLogger: AnalyticLogger) : ScreenModel {

    private val _homeState = MutableStateFlow(HomeState())
    private val _homeScreenState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState.Loading)
    val homeScreenState = _homeScreenState.asStateFlow()

    fun fetchShows() {
        coroutineScope.launch (Dispatchers.IO) {
            piRepository.fetchShows().collect { response ->
                Logger.i("Result: $response")
                when (response.status) {
                    Resource.Status.ERROR -> {
                        _homeState.update { it.copy(isLoading = false, piError = response.error) }
                    }

                    Resource.Status.LOADING -> {
                        _homeState.update { it.copy(isLoading = true) }
                    }

                    else -> {
                        Logger.d( response.data?.toString() ?: "")
                        _homeState.update {
                            it.copy(
                                isLoading = false,
                                piError = null,
                                showList = response.data
                            )
                        }
                    }
                }
               _homeScreenState.value =  _homeState.value.toUiState()
            }
        }
    }
}