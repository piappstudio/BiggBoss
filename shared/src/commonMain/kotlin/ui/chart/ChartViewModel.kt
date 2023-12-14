package ui.chart

import analytics.AnalyticLogger
import model.PollItem
import network.PiRepository
import ui.detail.ShowDetailModel
import ui.native.LinkLauncher

class ChartViewModel (private val piRepository: PiRepository, linkLauncher: LinkLauncher, analyticLogger: AnalyticLogger):
    ShowDetailModel(piRepository, linkLauncher, analyticLogger) {

        fun filterBasedOnNotes(keys:String): Map<String, Double>? {
            episodeUiData.value.showDetail?.participants?.let { lstParticipants ->
                val allParticipantItem = lstParticipants.associate { participantItem ->
                    (participantItem.name?.subSequence(0, 4).toString()) to
                            (participantItem.history?.filter { historyItem ->
                                historyItem.notes?.contains(
                                    keys
                                ) == true
                            }?.size?.toDouble() ?: 0.0)
                }
                return allParticipantItem.toList()
                    .sortedByDescending { it.second }
                    .toMap()
            }?:run {
                return null
            }
        }

    fun votesTrending(participantId:Int): List<PollItem>? {
        return episodeUiData.value.votes?.poll?.filter { pollItem ->
            pollItem.votes?.any { votesItem ->
                votesItem.players?.any { playerItem -> playerItem.id == participantId } == true
            } == true }
    }

        fun filterMostNumberOfStars(): Map<String, Double>? {
            episodeUiData.value.showDetail?.participants?.let { lstParticipants ->
                val allParticipantItem = lstParticipants.associate { participantItem ->
                    (participantItem.name?.subSequence(0, 4).toString()) to
                            (participantItem.noOfStars().toDouble() ?: 0.0)
                }

                return allParticipantItem.toList()
                    .sortedByDescending { it.second }
                    .toMap()
            }?:run {
                return null
            }

        }

    fun filterMostTTFScore(): Map<String, Double>? {
            episodeUiData.value.showDetail?.participants?.let { lstParticipants ->
                val allParticipantItem = lstParticipants.associate { participantItem ->
                    (participantItem.name?.subSequence(0, 4).toString()) to
                            participantItem.noOfPoint().toDouble()
                }

                return allParticipantItem.toList()
                    .sortedByDescending { it.second }
                    .toMap()
            }?:run {
                return null
            }

        }
}