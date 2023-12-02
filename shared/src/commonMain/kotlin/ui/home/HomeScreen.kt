package ui.home

import analytics.AnalyticConstant
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.IConstant
import model.ReviewerItem
import model.ShowItem
import model.ShowList
import model.daysSoFar
import model.daysUntilNow
import model.piShadow
import model.toDate
import ui.component.PiProgressIndicator
import ui.detail.ShowDetailScreen
import ui.theme.Dimens


class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val homeScreenModel = getScreenModel<HomeScreenModel>()
        LaunchedEffect(Unit) {
            homeScreenModel.fetchShows()
        }
        val state by homeScreenModel.homeScreenState.collectAsState()
        val currentNav = LocalNavigator.currentOrThrow
        
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        Scaffold(topBar = {
            MediumTopAppBar(title = {
                Text(stringResource(MR.strings.title_home))
            }, scrollBehavior = scrollBehavior)

        }) {
            Column (modifier = Modifier.fillMaxSize().padding(it).padding(Dimens.doubleSpace).nestedScroll(scrollBehavior.nestedScrollConnection).verticalScroll(
                rememberScrollState()
            )) {
                when (state) {
                    is HomeScreenState.Loading -> {
                        PiProgressIndicator()
                    }

                    is HomeScreenState.Error -> {

                    }

                    is HomeScreenState.Success -> {
                        RenderHomeScreen((state as HomeScreenState.Success).showList, onClick = {item->
                            // item clicked event
                            homeScreenModel.analyticLogger.logEvent(AnalyticConstant.Event.CLICKED, mapOf(
                                Pair(AnalyticConstant.Params.ACTION_NAME, item.title?:IConstant.EMPTY)
                            ) )
                            currentNav.push(ShowDetailScreen(title = item.title?:IConstant.EMPTY, url= item.moreInfo?:IConstant.EMPTY, trendUrl = item.trends?:IConstant.EMPTY, startDate = item.startDate?:IConstant.EMPTY))
                        }) { youtube ->
                            youtube.url?.let {
                                homeScreenModel.analyticLogger.logEvent(AnalyticConstant.Event.YOUTUBE, mapOf(
                                    Pair(AnalyticConstant.Params.URL, youtube.url)
                                ) )
                                homeScreenModel.linkLauncher.openLink(youtube.url)
                            }

                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RenderHomeScreen(showList: ShowList?, onClick:(showItem: ShowItem)->Unit, onYoutubeClick:(reviewer:ReviewerItem)->Unit) {
        showList?.shows?.let { lst ->
            Text("Bigg Boss Shows", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                lst.forEach{
                    item ->
                    Surface (modifier = Modifier.padding(top = Dimens.space).piShadow(), onClick = {
                        onClick.invoke(item)
                    }, shape = RoundedCornerShape(Dimens.space)) {
                        Row (modifier = Modifier.fillMaxWidth().padding(Dimens.space), verticalAlignment = Alignment.CenterVertically) {
                            item.logo?.let { imgUrl ->
                                KamelImage(
                                    modifier = Modifier.size(100.dp, 70.dp).clip(RoundedCornerShape(Dimens.doubleSpace)),
                                    resource = asyncPainterResource(imgUrl),
                                    contentDescription = "Logo"
                                )
                            }
                            Column (modifier = Modifier.padding(start = Dimens.space).weight(1f)) {
                                Text(item.title ?: IConstant.EMPTY, style = MaterialTheme.typography.titleMedium)
                                Text(item.host ?: IConstant.EMPTY, style = MaterialTheme.typography.bodyMedium)
                            }
                            Column (modifier = Modifier.padding(start = Dimens.space, end = Dimens.space), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("DAYS", style = MaterialTheme.typography.titleSmall)
                                Text(item.startDate?.toDate()?.daysUntilNow()?.toString()?:IConstant.EMPTY,style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }

        }

        showList?.reviewers?.let { lst ->
            Spacer(modifier = Modifier.height(Dimens.tripleSpace))
            Text("Youtube Reviewers", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(Dimens.space))
                lst.forEach{
                    item ->
                    Surface  (modifier = Modifier.padding(top = Dimens.space).piShadow(), onClick = {
                        onYoutubeClick.invoke(item)
                    }, shape = RoundedCornerShape(Dimens.space)) {
                        Row (modifier = Modifier.fillMaxWidth().padding(Dimens.space), verticalAlignment = Alignment.CenterVertically) {
                            item.image?.let { imgUrl ->
                                KamelImage(
                                    modifier = Modifier.size(70.dp, 70.dp).clip(CircleShape),
                                    resource = asyncPainterResource(imgUrl),
                                    contentDescription = "Logo"
                                )
                            }
                            Column (modifier = Modifier.padding(start = Dimens.space).weight(1f)) {
                                Text(item.channelName ?: IConstant.EMPTY, style = MaterialTheme.typography.labelLarge)
                                Text(item.description ?: IConstant.EMPTY, style = MaterialTheme.typography.labelSmall)
                            }

                        }
                    }
                }

        }
    }
}