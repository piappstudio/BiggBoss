package ui.participant

import analytics.AnalyticLogger
import cafe.adriel.voyager.core.model.ScreenModel
import network.PiRepository
import ui.native.LinkLauncher

class ParticipantDetailViewModel (private val piRepository: PiRepository, val linkLauncher: LinkLauncher, val analyticLogger: AnalyticLogger): ScreenModel {

}