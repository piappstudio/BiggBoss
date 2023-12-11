package ui.participant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import model.IConstant
import model.PollItem
import model.generateRandomColorExcludingWhite
import ui.chart.RenderBarChart
import ui.chart.RenderTitle
import ui.theme.Dimens

@Composable
fun ParticipantDetailChart(participantId:Int, lstPolls: List<PollItem>?) {

    lstPolls?.forEach { pollItem ->
        val yAxisData = mutableListOf<Double>()
        val xAxisData = mutableListOf<String>()
        pollItem.votes?.forEach { votesItem ->
            votesItem.players?.firstOrNull { it.id == participantId }?.percentage?.toDouble()?.let {
                xAxisData.add("${votesItem.week}")
                yAxisData.add(it)
            }
        }
        if (xAxisData.isNotEmpty()) {
            Logger.i ("xAxisDat: ${xAxisData}, yAxis: ${yAxisData}")
            Column (modifier = Modifier.padding(horizontal = Dimens.space)) {
                RenderTitle(pollItem.name +"- Vote Share (Week vs %)")
                RenderBarChart(
                    pollItem.name ?: IConstant.EMPTY,
                    yAxisData = yAxisData,
                    xAxisData = xAxisData,
                    color = generateRandomColorExcludingWhite()
                )
            }
        }

    }
}