package ui.participant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import model.ParticipantItem
import model.PiGlobalInfo
import ui.chart.RenderBarChart
import ui.chart.RenderTitle
import ui.theme.Dimens
import ui.theme.PiColor
import ui.theme.nominated

@Composable
fun RenderPerformanceGraph(participantItem: ParticipantItem) {

    Column (modifier = Modifier.padding(Dimens.space)) {
        RenderTitle("Nomination Trending")
        Spacer(modifier = Modifier.height(Dimens.space))

        val lstBarParameters = mutableListOf<BarParameters>()
        val lstParticipant = PiGlobalInfo.episodeDetail?.participants?.filter { it.id != participantItem.id }

        val lstName = lstParticipant?.map { it.name?.subSequence(0,3)?.toString()?:"" }?: emptyList()
        lstParticipant?.let {
            var lstVotes = numberOfTimesINominates(lstParticipant, participantItem.id?.toInt()?:0)
            lstBarParameters.add(BarParameters("Who nominates me?", data = lstVotes.values.map {vote-> vote.toDouble() }, PiColor.nominatedBy))
            lstVotes = numberOfTimeIWasNominatedBy(lstParticipant, participantItem.id?.toInt()?:0)
            lstBarParameters.add(BarParameters("Whom I nominated?", data = lstVotes.values.map {vote-> vote.toDouble() }, nominated))
        }

        Box(Modifier.fillMaxWidth().height(400.dp)) {
            BarChart(
                chartParameters = lstBarParameters,
                gridColor = Color.DarkGray,
                xAxisData = lstName,
                isShowGrid = true,
                animateChart = true,
                showGridWithSpacer = true,
                yAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                ),
                xAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.W400
                ),
                yAxisRange = 5,
                barWidth = 20.dp,
                spaceBetweenBars = 0.dp,
                spaceBetweenGroups = Dimens.space,
            )
        }

    }

}


fun numberOfTimeIWasNominatedBy(
    participantList: List<ParticipantItem>,
    yourUserId: Int // Replace with your user ID
): Map<String, Int> {
    return participantList
        .associate { participant ->
            val participantName = participant.name ?: ""
            val nominationsCount = participant.history?.count { historyItem ->
                historyItem.nominatedBy?.any { it.id?.toInt() == yourUserId } == true
            } ?: 0

            participantName to nominationsCount
        }
}
fun numberOfTimesINominates(
    participantList: List<ParticipantItem>,
    yourUserId: Int // Replace with your user ID
): Map<String, Int> {
    return participantList
        .associate { participant ->
            val participantName = participant.name ?: ""
            val nominationsCount = participant.history?.count { historyItem ->
                historyItem.nominations?.any { it == yourUserId } == true
            } ?: 0

            participantName to nominationsCount
        }
}