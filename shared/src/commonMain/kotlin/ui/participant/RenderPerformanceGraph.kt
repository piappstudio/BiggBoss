package ui.participant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aay.compose.barChart.model.BarParameters
import model.ParticipantItem
import model.PiGlobalInfo
import ui.chart.RenderTitle
import ui.theme.Dimens
import ui.theme.PiColor

@Composable
fun RenderPerformanceGraph(participantItem: ParticipantItem) {
    Column {
        RenderTitle("Nomination Trending")
        Spacer(modifier = Modifier.height(Dimens.space))

        val lstParticipant = PiGlobalInfo.episodeDetail?.participants?.map { it.name?.subSequence(0,2) }

        val lstBarParameters = mutableListOf<BarParameters>()

        val lstNominatedTo = mutableListOf<Double>()
        val lstNominatedBy = mutableListOf<Double>()

        lstBarParameters.add(BarParameters("Nominated To", data = lstNominatedTo, PiColor.nominatedTo))
        lstBarParameters.add(BarParameters("Nominated By", data = lstNominatedBy, PiColor.nominatedBy))
    }

}
