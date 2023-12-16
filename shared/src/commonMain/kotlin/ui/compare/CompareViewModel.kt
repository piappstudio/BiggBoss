package ui.compare

import analytics.AnalyticLogger
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.flow
import model.ParticipantItem
import model.PiGlobalInfo
import network.PiRepository

class CompareViewModel(val analyticLogger: AnalyticLogger): ScreenModel {

    fun getParticipants(ids:String) = flow {
        val arrOfIds = ids.split(",")
       emit(PiGlobalInfo.episodeDetail?.participants?.filter { participantItem -> arrOfIds.contains(participantItem.id) })
    }
}