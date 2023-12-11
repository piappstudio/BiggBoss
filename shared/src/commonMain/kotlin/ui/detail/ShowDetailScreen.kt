package ui.detail

import RenderNotification
import RenderTrendingScreen
import analytics.AnalyticConstant
import analytics.AnalyticLogger
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import model.IConstant
import model.ShowDetail
import model.piShadow
import model.toDate
import renderSection
import ui.chart.ChartScreen
import ui.component.PiProgressIndicator
import ui.participant.RenderUnofficialVoting
import ui.theme.Dimens

class ShowDetailScreen(private val title: String, val url:String, val trendUrl:String, val startDate:String, val voteUrl:String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val detailModel = getScreenModel<ShowDetailModel>()

        val state by detailModel.episodeUiData.collectAsState()
        LaunchedEffect(url) {
            if (state.showDetail==null) {
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

            }, actions = {
                IconButton(onClick = {
                    navigator.push(ChartScreen(title = title, url= url, trendUrl = trendUrl, startDate = startDate, voteUrl = voteUrl))
                }) {
                    Icon(imageVector = Icons.Default.BarChart, contentDescription = "Display Charts" )
                }
            })
        }) {
            RenderTabBar(modifier = Modifier.padding(it), detailViewModel = detailModel)
        }
    }




    @Composable
    fun RenderTabBar(modifier: Modifier, detailViewModel: ShowDetailModel) {
        val episodeUiData by detailViewModel.episodeUiData.collectAsState()
        val selectedTab = episodeUiData.selectedTab
        val tabs = listOf("All", "Nominations", "Eliminated", "Polls", "Trending", "Notification")
        Column (modifier = modifier) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = index == selectedTab,
                        onClick = {
                            detailViewModel.selectTab(index)
                        },
                        text = { Text(text = title) }
                    )
                }
            }

            if (episodeUiData.inProgress) {
                PiProgressIndicator()
            }

            // Content for each tab
            when (selectedTab) {
                0 -> {
                    LaunchedEffect(Unit) {
                        detailViewModel.analyticLogger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(
                            Pair(AnalyticConstant.Params.SCREEN_NAME, AnalyticConstant.Screen.ALL)
                        ))
                    }

                    RenderContestantList(episodeUiData.showDetail, modifier = Modifier, detailViewModel.analyticLogger)
                }
                1 -> {
                    LaunchedEffect(Unit)  {
                        detailViewModel.analyticLogger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(
                            Pair(AnalyticConstant.Params.SCREEN_NAME, AnalyticConstant.Screen.NOMINATIONS)
                        ))
                    }

                    RenderNominationScreen(episodeUiData.showDetail, modifier = Modifier, detailViewModel.analyticLogger)
                }
                2 -> {
                    LaunchedEffect(Unit)  {
                        detailViewModel.analyticLogger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(
                            Pair(AnalyticConstant.Params.SCREEN_NAME, AnalyticConstant.Screen.ELIMINATED)
                        ))
                    }

                    RenderEliminatedScreen(episodeUiData.showDetail, modifier = Modifier, detailViewModel.analyticLogger)

                }
                3 -> {
                    LaunchedEffect(Unit)  {
                        detailViewModel.analyticLogger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(
                            Pair(AnalyticConstant.Params.SCREEN_NAME, AnalyticConstant.Screen.VOTING)
                        ))
                    }

                    episodeUiData.showDetail?.votingOption?.let {votingOption->
                        RenderUnofficialVoting(votingOption, detailViewModel.linkLauncher, detailViewModel.analyticLogger)
                    }
                }
                4 -> {
                    LaunchedEffect(Unit)  {
                        detailViewModel.analyticLogger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(
                            Pair(AnalyticConstant.Params.SCREEN_NAME, AnalyticConstant.Screen.TRENDING)
                        ))
                    }

                    RenderTrendingScreen(episodeUiData.trend, detailViewModel.linkLauncher, detailViewModel.analyticLogger)
                }
                5 -> {
                    LaunchedEffect(Unit) {
                        detailViewModel.analyticLogger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(
                            Pair(AnalyticConstant.Params.SCREEN_NAME, AnalyticConstant.Screen.NOTIFICATIONS)
                        ))
                    }

                    RenderNotification(MR.strings.title_notification, episodeUiData.showDetail?.notifications, detailViewModel.analyticLogger)
                }
            }
        }
    }


    @Composable
    fun RenderContestantList(data: ShowDetail?, modifier: Modifier, analyticLogger: AnalyticLogger) {
        LazyColumn(modifier = modifier.fillMaxWidth().padding(Dimens.doubleSpace)) {
            if (data?.participants == null) {
                item {
                    EmptyPlaceholder(
                        stringResource(MR.strings.error_empty_contestant),
                        modifier = modifier
                    )
                }
            } else {
                val lstCaptain = data.participants.filter { it.isCaptain == true }
                if (lstCaptain.isNotEmpty()) {
                    renderSection( MR.strings.title_captain,this, lstCaptain, data, analyticLogger)
                }

                val lstNominated = data.participants.filter { it.isNominated == true }
                val sortedNominatedItems = lstNominated.sortedByDescending { it.history?.lastOrNull()?.nominatedBy?.size  }
                if (sortedNominatedItems.isNotEmpty()) {
                    renderSection( MR.strings.title_nomination,this, sortedNominatedItems, data, analyticLogger)
                }


                val lstOthers =
                    data.participants.filter { it.isNominated != true && it.isCaptain != true && (it.eliminatedDate.isNullOrEmpty() || (!it.reEntryDate.isNullOrEmpty() && it.reEntryEvictedDate.isNullOrEmpty()) ) }
                if (lstOthers.isNotEmpty()) {
                    renderSection( MR.strings.title_others,this, lstOthers, data, analyticLogger)
                }


                val lstEliminated = data.participants.filter { it.isEliminated() }.sortedByDescending { it.eliminatedDate?.toDate() }

                if (lstEliminated.isNotEmpty()) {
                    renderSection(MR.strings.title_eliminated, this, lstEliminated,  data, analyticLogger)
                }
            }

        }
    }
}

@Composable
fun EmptyPlaceholder(title: String, modifier: Modifier) {
    Surface (modifier = modifier.fillMaxSize().padding(Dimens.doubleSpace).piShadow()) {
        Column (modifier = Modifier.padding(Dimens.doubleSpace), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.Error, contentDescription = title)
            Spacer(modifier = Modifier.height(Dimens.space))
            Text(title, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

