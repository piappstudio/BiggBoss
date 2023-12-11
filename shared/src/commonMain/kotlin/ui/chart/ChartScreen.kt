package ui.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import model.ParticipantItem
import model.generateRandomColorExcludingWhite
import ui.theme.Dimens
import ui.theme.PiColor

class ChartScreen(
    private val title: String,
    val url: String,
    val trendUrl: String,
    val startDate: String,
    val voteUrl:String
) :
    Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val detailModel = getScreenModel<ChartViewModel>()

        val state by detailModel.episodeUiData.collectAsState()
        LaunchedEffect(url) {
            if (state.showDetail == null) {
                detailModel.fetchShowDetails(url, startDate)
                detailModel.fetchTrends(trendUrl)
                detailModel.fetchChartData(voteUrl)
            }

        }

        Scaffold(topBar = {
            TopAppBar(title = {
                Text(title)
            }, navigationIcon = {
                IconButton(onClick = {
                    navigator.pop()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
                }

            })
        }) { it ->

            LazyColumn(Modifier.padding(it).padding(Dimens.space)) {
                item {

                    val goldStar = "GS"
                    RenderTitle("Gold Star Trending")
                   detailModel.filterMostNumberOfStars()?.let {sortedEmployeeMap->
                        val xAxisData = sortedEmployeeMap.keys.toList()
                        val yAxisData = sortedEmployeeMap.values.toList()
                        RenderBarChart("Gold Star", yAxisData, xAxisData, PiColor.goldStar)
                    }
                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                    val sbh = stringResource(MR.strings.title_sbh)
                    RenderTitle("Weekly Small Boss House Trending")
                   detailModel.filterBasedOnNotes(sbh)?.let { sortedEmployeeMap->
                       val xAxisData = sortedEmployeeMap.keys.toList()
                       val yAxisData = sortedEmployeeMap.values.toList()
                       RenderBarChart(sbh, yAxisData, xAxisData, ui.theme.smallBossHouse)
                   }


                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                    val nominated = stringResource(MR.strings.title_nominated)
                    RenderTitle("Most number of nominations")
                    detailModel.filterBasedOnNotes(nominated)?.let {sortedEmployeeMap->
                        val xAxisData = sortedEmployeeMap.keys.toList()
                        val yAxisData = sortedEmployeeMap.values.toList()
                        RenderBarChart(nominated, yAxisData, xAxisData, ui.theme.nominated)
                    }

                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))

                    // Captain
                    val captain = stringResource(MR.strings.title_captain)
                    RenderTitle("Weekly Captain Trending")
                    detailModel.filterBasedOnNotes(captain)?.let {sortedEmployeeMap->
                        val xAxisData = sortedEmployeeMap.keys.toList()
                        val yAxisData = sortedEmployeeMap.values.toList()
                        RenderBarChart(captain, yAxisData, xAxisData, ui.theme.captain)
                    }
                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                    // Week wise nominations in html
                    PiWebChart(modifier = Modifier.fillMaxSize().height(400.dp), state.showDetail?.participants)
                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                    PiOverallVotingChart(modifier = Modifier.fillMaxSize().height(400.dp), state.showDetail?.participants, polls = state.votes?.poll)
                }
            }

        }
    }


    @Composable
    fun RenderWeeklyWiseVotingChart(participants: List<ParticipantItem>?) {
        participants?.let { lstParticipants ->
            val nominated = stringResource(MR.strings.title_nominated)
            RenderTitle("Weekly nomination votes")
            val lstBarParameter = mutableListOf<BarParameters>()
            val lstWeeks =
                lstParticipants.flatMap { it.history ?: emptyList() }.mapNotNull { it.week }
                    .distinct()
            lstParticipants.forEach { participant ->
                val lstVotes = mutableListOf<Double>()
                for (week in lstWeeks) {
                    val currentWeek = participant.history?.firstOrNull {
                        it.week == week /*&& it.notes?.contains(nominated) == true*/
                    }
                    if (currentWeek != null) {
                        Logger.d { "Week: $week, Participant: ${participant.name}, Vote: ${currentWeek.nominatedBy?.size}" }
                        lstVotes.add(currentWeek.nominatedBy?.size?.toDouble() ?: 0.0)
                    } else {
                        lstVotes.add(0.0)
                    }
                }

                lstBarParameter.add(
                    BarParameters(
                        participant.name ?: "",
                        data = lstVotes,
                        generateRandomColorExcludingWhite()
                    )
                )
            }

            Box(Modifier.padding(Dimens.doubleSpace).fillMaxWidth().height(400.dp)) {

                BarChart(
                    chartParameters = lstBarParameter,
                    gridColor = Color.DarkGray,
                    xAxisData = lstWeeks.map { "Week: ${it}" },
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
                    barWidth = Dimens.doubleSpace,
                    spaceBetweenBars = 0.dp,
                    spaceBetweenGroups = Dimens.forthSpace,
                )
            }

        }


    }

}