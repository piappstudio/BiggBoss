package ui.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.ShowDetail
import model.ShowItem
import network.PiRepository
import network.Resource

class ShowDetailModel(private val piRepository: PiRepository):ScreenModel {

    private val _showDetailState: MutableStateFlow<Resource<ShowDetail?>> = MutableStateFlow(Resource.loading(null))
    val showDetailState:StateFlow<Resource<ShowDetail?>> = _showDetailState

    fun fetchShowDetails(showId: String) {
        coroutineScope.launch (Dispatchers.IO) {
          piRepository.fetchDetail(showId).collect {
              _showDetailState.value = it
          }
        }

    }


}