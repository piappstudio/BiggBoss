package ui.detail

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
import kotlinx.datetime.Instant
import model.HistoryItem
import model.ParticipantItem
import model.PiGlobalInfo
import model.ShowDetail
import model.Trend
import model.TrendItem
import model.WeeklyInfo
import model.toDate
import network.PIError
import network.PiRepository
import network.Resource
import ui.native.LinkLauncher


data class EpisodeUiData(
    val selectedTab: Int = 0,
    val showDetail: ShowDetail? = null,
    val inProgress: Boolean = false,
    val error: PIError? = null,
    val weeklyInfo: WeeklyInfo? = null,
    val trend: List<TrendItem>? = null)


open class ShowDetailModel(private val piRepository: PiRepository, val linkLauncher: LinkLauncher, val analyticLogger: AnalyticLogger) :
    ScreenModel {

    private val _episodeUiState: MutableStateFlow<EpisodeUiData> = MutableStateFlow(EpisodeUiData())
    val episodeUiData = _episodeUiState.asStateFlow()

    fun fetchShowDetails(showId: String, startDate:String) {
        coroutineScope.launch(Dispatchers.IO) {
            piRepository.fetchDetail(showId).collect { result ->
                when (result.status) {
                    Resource.Status.LOADING -> {
                        _episodeUiState.update { it.copy(inProgress = true) }
                    }

                    Resource.Status.ERROR -> {
                        _episodeUiState.update { it.copy(inProgress = false, error = result.error) }
                    }

                    Resource.Status.SUCCESS -> {
                        _episodeUiState.update {
                            it.copy(
                                showDetail = result.data?.copy(startDate = startDate),
                                inProgress = false
                            )
                        }
                        performReportMapping()
                        PiGlobalInfo.episodeDetail = _episodeUiState.value.showDetail
                    }

                    else -> {
                        _episodeUiState.update { it.copy(inProgress = true) }
                    }
                }
            }
        }
    }
    fun fetchTrends(trendUrl: String) {
        coroutineScope.launch(Dispatchers.IO) {
            piRepository.fetchTrends(trendUrl).collect { result ->
                Logger.i (result.status.toString())
                when (result.status) {
                    Resource.Status.LOADING -> {
                        _episodeUiState.update { it.copy(inProgress = true) }
                    }

                    Resource.Status.ERROR -> {
                        _episodeUiState.update { it.copy(inProgress = false, error = result.error) }
                    }

                    Resource.Status.SUCCESS -> {
                        Logger.i(result.data.toString())
                        _episodeUiState.update {
                            it.copy(
                                trend = result.data,
                                inProgress = false
                            )
                        }
                        performReportMapping()
                        PiGlobalInfo.episodeDetail = _episodeUiState.value.showDetail
                    }

                    else -> {
                        _episodeUiState.update { it.copy(inProgress = true) }
                    }
                }
            }
        }
    }
    fun selectTab(index: Int) {
        _episodeUiState.update { it.copy(selectedTab = index) }
    }

    private fun performReportMapping() {
        val showDetail = _episodeUiState.value.showDetail
        if (showDetail != null) {
            // Update nominated by list
            val updateLstOfNominatedBy: MutableList<ParticipantItem> = mutableListOf()

            showDetail.participants?.forEach { participantItem ->

                val coParticipantItem:ParticipantItem

                val lstWeeks:MutableList<HistoryItem> = mutableListOf()
                participantItem.history?.forEach {weekItem ->
                   val lstItems =  showDetail.participants.filter { allParticipant ->
                       allParticipant.history?.any { historyItem ->
                           historyItem.week == weekItem.week
                                   && historyItem.nominations?.contains(participantItem.id?.toInt()) == true
                       } == true }

                    // nominated to
                    lstWeeks.add(weekItem.copy(nominatedBy =  lstItems))
                }

                coParticipantItem = participantItem.copy(history = lstWeeks)
                updateLstOfNominatedBy.add(coParticipantItem)

                /*
                val lstNominatedBy = showDetail.participants.filter {
                    it.history?.lastOrNull { week ->
                        week.nominations?.contains(participantItem.id?.toInt()) == true
                    } != null
                }


                val lstOfNominatedByDetail: MutableList<ParticipantItem> = mutableListOf()
                lstNominatedBy.forEach { nominatedBy ->
                    showDetail.participants.firstOrNull { player -> nominatedBy.id.toString() == player.id }
                        ?.let {
                            lstOfNominatedByDetail.add(it)
                        }
                }

                // Update the list
                val lstOfNominatedItem:MutableList<ParticipantItem> = mutableListOf()

                // Update nominated users details
                participantItem.history?.lastOrNull()?.nominations?.forEach { player->
                  showDetail.participants.firstOrNull { it.id == player.toString() }?.let {
                      lstOfNominatedItem.add(it)
                  }
                }

                val updatedParticipantItem =
                    participantItem.copy(nominatedBy = lstOfNominatedByDetail, nominatedPlayer = lstOfNominatedItem)

                updateLstOfNominatedBy.add(updatedParticipantItem)
                */

            }

            //Update the original list
            _episodeUiState.update { it.copy(showDetail = it.showDetail?.copy(participants = updateLstOfNominatedBy)) }


        }


    }

}