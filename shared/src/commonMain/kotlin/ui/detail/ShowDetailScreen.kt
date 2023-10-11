package ui.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.biggboss.shared.MR
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.IConstant
import model.ParticipantItem
import model.ShowDetail
import model.VotingOption
import model.piShadow
import network.Resource
import ui.component.PiProgressIndicator
import ui.participant.ParticipantDetailScreen
import ui.theme.Dimens
import ui.theme.captain
import ui.theme.evicted
import ui.theme.nominated

class ShowDetailScreen(private val title: String, val url:String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val detailModel = getScreenModel<ShowDetailModel>()
        LaunchedEffect(url) {
            detailModel.fetchShowDetails(url)
        }
        val state by detailModel.showDetailState.collectAsState()
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

    private fun renderSection(
        title: StringResource,
        lazyListScope: LazyListScope,
        lstNominated: List<ParticipantItem>,
        showDetailScreen: ShowDetailScreen,
        data: ShowDetail
    ) {
        lazyListScope.item {
            Text(
                stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.padding(bottom = Dimens.doubleSpace))
        }
        lazyListScope.items(lstNominated) { participant ->
            showDetailScreen.RenderContestantRow(participant, data.votingOption?: VotingOption())
            Spacer(modifier = Modifier.height(Dimens.space))
        }
    }


    @Composable
    fun RenderContestantRow(participant: ParticipantItem, votingOption: VotingOption) {
        val navigator = LocalNavigator.currentOrThrow
        Surface  (modifier = Modifier.piShadow().clickable {
            val json = Json { ignoreUnknownKeys = true }
            val jsonParticipantItem = json.encodeToString(participant)
            val jsonVotingOption = json.encodeToString(votingOption)
            navigator.push(ParticipantDetailScreen(jsonParticipantItem, jsonVotingOption))
        }) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(Dimens.doubleSpace),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    participant.image?.let { imgUrl ->
                        KamelImage(
                            modifier = Modifier.size(75.dp).clip(shape = CircleShape)
                                .border(1.dp, Color.Gray, CircleShape),
                            resource = asyncPainterResource(imgUrl),
                            contentDescription = "Participant image"
                        )
                    }
                    Column(modifier = Modifier.padding(start = Dimens.space)) {
                        Text(
                            participant.name ?: IConstant.EMPTY,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black
                        )
                        if (participant.eliminatedDate?.isNotEmpty() == true) {
                            Text(
                                participant.eliminatedDate,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (participant.eliminatedDate != null || participant.isCaptain == true || participant.isNominated == true) {
                            participant.isCaptain?.let {
                                Row {
                                    AssistChip(
                                        onClick = {
                                        },
                                        label = {
                                            Text(
                                                "Captain",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            labelColor = captain
                                        )
                                    )
                                }
                            }
                            if (participant.isNominated == true) {
                                Row {
                                    AssistChip(
                                        onClick = {
                                        },
                                        label = {
                                            Text(
                                                "Nominated",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            labelColor = nominated
                                        )
                                    )
                                }
                            }
                            if (participant.eliminatedDate?.isNotBlank() == true) {
                                Row {
                                    AssistChip(onClick = {
                                    }, label = {
                                        Text(
                                            "Evicted",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }, colors = AssistChipDefaults.assistChipColors(
                                        labelColor = evicted
                                    )
                                    )
                                }
                            }


                        }


                    }
                }
            }
        }
    }


}


