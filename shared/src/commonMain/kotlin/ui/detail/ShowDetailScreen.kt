package ui.detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import model.ShowDetail
import network.Resource
import renderNotification
import renderSection
import ui.component.PiProgressIndicator
import ui.theme.Dimens

class ShowDetailScreen(private val title: String, val url:String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val detailModel = getScreenModel<ShowDetailModel>()

        val state by detailModel.showDetailState.collectAsState()
        LaunchedEffect(url) {
            if (state.data==null) {
                detailModel.fetchShowDetails(url)
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
        }) {
            when (state.status) {
                Resource.Status.LOADING -> {
                    PiProgressIndicator()
                }

                Resource.Status.SUCCESS -> {
                    RenderContestantList(state.data, modifier = Modifier.padding(it))
                }

                Resource.Status.ERROR -> {

                }

                else -> {

                }
            }
        }
    }

    @Composable
    fun EmptyPlaceholder(title: String, modifier: Modifier) {
        Card(modifier = modifier.padding(Dimens.doubleSpace).fillMaxWidth()) {
            Icon(imageVector = Icons.Default.Error, contentDescription = title)
            Spacer(modifier = Modifier.height(Dimens.space))
            Text(title, style = MaterialTheme.typography.bodyLarge)

        }
    }

    @Composable
    fun RenderContestantList(data: ShowDetail?, modifier: Modifier) {
        LazyColumn(modifier = modifier.fillMaxSize().padding(Dimens.doubleSpace)) {
            if (data?.participants == null) {
                item {
                    EmptyPlaceholder(
                        stringResource(MR.strings.error_empty_contestant),
                        modifier = modifier
                    )
                }
            } else {

                data.notifications?.let {
                    renderNotification(MR.strings.title_notification, this, data.notifications)
                }

                val lstCaptain = data.participants.filter { it.isCaptain == true }
                if (lstCaptain.isNotEmpty()) {
                    renderSection( MR.strings.title_captain,this, lstCaptain, this@ShowDetailScreen, data)
                }

                val lstNominated = data.participants.filter { it.isNominated == true }

                if (lstNominated.isNotEmpty()) {
                    renderSection( MR.strings.title_nomination,this, lstNominated, this@ShowDetailScreen, data)
                }

                val lstEliminated = data.participants.filter { it.eliminatedDate?.isNotEmpty() == true }
                if (lstEliminated.isNotEmpty()) {
                    renderSection(MR.strings.title_eliminated, this, lstEliminated, this@ShowDetailScreen, data)
                }

                val lstOthers =
                    data.participants.filter { it.isNominated != true && it.isCaptain != true && it.eliminatedDate.isNullOrEmpty() }
                if (lstOthers.isNotEmpty()) {
                    renderSection( MR.strings.title_others,this, lstOthers, this@ShowDetailScreen, data)
                }
            }

        }
    }




}


