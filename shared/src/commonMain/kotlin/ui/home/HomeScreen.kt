package ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.IConstant
import model.ShowList
import ui.component.PiProgressIndicator
import ui.theme.Dimens


class HomeScreen : Screen {


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val homeScreenModel = getScreenModel<HomeScreenModel>()
        val state by homeScreenModel.uiState.collectAsState()
        Scaffold(topBar = {
            MediumTopAppBar(title = {
                Text(stringResource(MR.strings.title_home))
            })

        }) {
            Text("Home Screen")
            Surface(modifier = Modifier.fillMaxSize().padding(it)) {
                when (state) {
                    is HomeScreenState.Loading -> {
                        PiProgressIndicator()
                    }

                    is HomeScreenState.Error -> {
                    }

                    is HomeScreenState.Success -> {
                        RenderHomeScreen((state as HomeScreenState.Success).showList)
                    }
                }
            }
        }
    }

    @Composable
    fun RenderHomeScreen(showList: ShowList?) {
        showList?.shows?.let { lst ->
            LazyColumn {
                items(lst) { item ->
                    Card (modifier = Modifier.padding(Dimens.space)) {
                        Row (modifier = Modifier.fillMaxWidth().padding(Dimens.space), verticalAlignment = Alignment.CenterVertically) {
                            item.logo?.let { imgUrl ->
                                KamelImage(
                                    modifier = Modifier.size(100.dp, 70.dp),
                                    resource = asyncPainterResource(imgUrl),
                                    contentDescription = "Logo"
                                )
                            }
                            Column (modifier = Modifier.padding(start = Dimens.space)) {
                                Text(item.title ?: IConstant.EMPTY, style = MaterialTheme.typography.titleMedium)
                                Text(item.host ?: IConstant.EMPTY, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                    }

                }

            }
        }
    }
}