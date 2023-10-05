package ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import model.ShowItem
import model.ShowList
import model.daysSoFar
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
                        RenderHomeScreen((state as HomeScreenState.Success).showList) { item->
                            currentNav.push(ShowDetailScreen(item))
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RenderHomeScreen(showList: ShowList?, onClick:(showItem: ShowItem)->Unit) {
        showList?.shows?.let { lst ->

                Text("Bigg Boss Shows", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                lst.forEach{
                    item ->
                    ElevatedCard (modifier = Modifier.padding(top = Dimens.space), onClick = {
                        onClick.invoke(item)
                    }, shape = RoundedCornerShape(Dimens.space)) {
                        Row (modifier = Modifier.fillMaxWidth().padding(Dimens.space), verticalAlignment = Alignment.CenterVertically) {
                            item.logo?.let { imgUrl ->
                                KamelImage(
                                    modifier = Modifier.size(100.dp, 70.dp),
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
                                Text(item.startDate?.toDate()?.daysSoFar()?.toString()?:IConstant.EMPTY,style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)

                            }
                        }
                    }
                }

        }
    }
}