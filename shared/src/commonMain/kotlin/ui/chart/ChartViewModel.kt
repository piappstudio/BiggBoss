package ui.chart

import analytics.AnalyticLogger
import network.PiRepository
import ui.detail.ShowDetailModel
import ui.native.LinkLauncher

class ChartViewModel (private val piRepository: PiRepository, linkLauncher: LinkLauncher, analyticLogger: AnalyticLogger):
    ShowDetailModel(piRepository, linkLauncher, analyticLogger) {
}