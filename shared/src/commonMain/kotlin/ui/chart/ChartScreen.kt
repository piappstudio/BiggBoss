package ui.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import ui.theme.Dimens
import ui.theme.smallBossHouse

class ChartScreen(
    private val title: String,
    val url: String,
    val trendUrl: String,
    val startDate: String
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

                    val sbh = stringResource(MR.strings.title_sbh)
                    RenderTitle("Weekly Small Boss House Trending")
                    state.showDetail?.participants?.let { lstParticipants ->
                        val allParticipantItem = lstParticipants.associate { participantItem ->
                            (participantItem.name?.subSequence(0, 4).toString()) to
                                    (participantItem.history?.filter { historyItem ->
                                        historyItem.notes?.contains(
                                            sbh
                                        ) == true
                                    }?.size?.toDouble() ?: 0.0)
                        }
                        val sortedEmployeeMap = allParticipantItem.toList()
                            .sortedByDescending { it.second }
                            .toMap()
                        val xAxisData = sortedEmployeeMap.keys.toList()
                        val yAxisData = sortedEmployeeMap.values.toList()
                        RenderBarChart(sbh, yAxisData, xAxisData, ui.theme.smallBossHouse)
                    }

                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))

                    val nominated = stringResource(MR.strings.title_nominated)
                    RenderTitle("Weekly Nomination Trending")
                    state.showDetail?.participants?.let { lstParticipants ->
                        val allParticipantItem = lstParticipants.associate { participantItem ->
                            (participantItem.name?.subSequence(0, 4).toString()) to
                                    (participantItem.history?.filter { historyItem ->
                                        historyItem.notes?.contains(
                                            nominated
                                        ) == true
                                    }?.size?.toDouble() ?: 0.0)
                        }
                        val sortedEmployeeMap = allParticipantItem.toList()
                            .sortedByDescending { it.second }
                            .toMap()
                        val xAxisData = sortedEmployeeMap.keys.toList()
                        val yAxisData = sortedEmployeeMap.values.toList()
                        RenderBarChart(nominated, yAxisData, xAxisData, ui.theme.nominated)
                    }

                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                    // Captain

                    val captain = stringResource(MR.strings.title_captain)
                    RenderTitle("Weekly Captain Trending")
                    state.showDetail?.participants?.let { lstParticipants ->
                        val allParticipantItem = lstParticipants.associate { participantItem ->
                            (participantItem.name?.subSequence(0, 4).toString()) to
                                    (participantItem.history?.filter { historyItem ->
                                        historyItem.notes?.contains(
                                            captain
                                        ) == true
                                    }?.size?.toDouble() ?: 0.0)
                        }
                        val sortedEmployeeMap = allParticipantItem.toList()
                            .sortedByDescending { it.second }
                            .toMap()
                        val xAxisData = sortedEmployeeMap.keys.toList()
                        val yAxisData = sortedEmployeeMap.values.toList()
                        RenderBarChart(captain, yAxisData, xAxisData, ui.theme.captain)
                    }


                }
            }

        }
    }


    @Composable
    fun RenderBarChart(title: String, yAxisData: List<Double>, xAxisData: List<String>, color: Color) {
        Box(Modifier.fillMaxWidth().height(400.dp)) {
            BarChart(
                chartParameters = listOf(
                    BarParameters(
                        title,
                        data = yAxisData,
                        barColor = color
                    )
                ),
                gridColor = Color.DarkGray,
                xAxisData = xAxisData,
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
                spaceBetweenBars = Dimens.doubleSpace,
                spaceBetweenGroups = Dimens.space,
            )
        }
    }


    @Composable
    fun RenderTitle(title: String) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = Dimens.doubleSpace),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )
    }
}