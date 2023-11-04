package ui.participant

import analytics.AnalyticLogger
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.getScreenModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import model.HistoryItem
import model.IConstant
import model.ParticipantItem
import model.PiGlobalInfo
import model.VotingOption
import model.daysSoFar
import model.piShadow
import model.toDate
import ui.component.shared.RenderDayScreen
import ui.theme.Dimens

class ParticipantDetailScreen(private val query: String, private val strVotingOption: String) :
    Screen {

    @Composable
    override fun Content() {
        var participantItem by remember { mutableStateOf(ParticipantItem()) }
        var votingOption by remember { mutableStateOf(VotingOption()) }
        LaunchedEffect(query) {
            val json = Json { prettyPrint = true }
            participantItem = json.decodeFromString<ParticipantItem>(query)
            votingOption = json.decodeFromString<VotingOption>(strVotingOption)
        }
        val navigator = LocalNavigator.currentOrThrow
        val participantDetailViewModel = getScreenModel<ParticipantDetailViewModel>()

        Scaffold {
            Column(
                modifier = Modifier.padding(it).fillMaxWidth().verticalScroll(rememberScrollState())
            ) {
                Box {

                    participantItem.fullImage?.let { url ->
                        KamelImage(
                            resource = asyncPainterResource(url),
                            contentDescription = "Logo",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    IconButton(onClick = {
                        navigator.pop()
                    }, modifier = Modifier.padding(Dimens.space)) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Back Button"
                        )
                    }
                }

                Text(
                    participantItem.name ?: IConstant.EMPTY,
                    modifier = Modifier.padding(Dimens.doubleSpace),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(end = Dimens.space), horizontalArrangement = Arrangement.SpaceBetween) {
                    val startDate = participantItem.startDate?:PiGlobalInfo.episodeDetail?.startDate
                    startDate?.let {
                        Text(
                            "Start Date: $startDate",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(Dimens.doubleSpace)
                        )

                        val endDate = participantItem.eliminatedDate?.toDate()?:Clock.System.now()

                        RenderDayScreen("Days", startDate.toDate()?.daysSoFar(endDate).toString())


                    }

                }


                if (participantItem.isNominated == true) {
                    RenderVotingOption(votingOption, participantItem, participantDetailViewModel.linkLauncher, analyticLogger = participantDetailViewModel.analyticLogger)
                }

                participantItem.history?.let { history ->
                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                    Text(
                        "History",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(Dimens.doubleSpace)
                    )

                    Spacer(modifier = Modifier.padding(Dimens.space))
                    for (historyItem in history.sortedByDescending { historyItem ->  historyItem.week }) {
                        Surface(modifier = Modifier.padding(Dimens.doubleSpace).piShadow()) {
                            HistoryRow(historyItem, participantItem)
                        }
                    }

                }


            }


        }
    }



    @Composable
    fun RenderParticipant(participantItem: ParticipantItem) {
        Surface (modifier = Modifier.padding(top = Dimens.space).piShadow()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(Dimens.space),
                verticalAlignment = Alignment.CenterVertically
            ) {
                participantItem.image?.let { imgUrl ->
                    KamelImage(
                        modifier = Modifier.size(100.dp, 70.dp)
                            .clip(RoundedCornerShape(Dimens.doubleSpace)),
                        resource = asyncPainterResource(imgUrl),
                        contentDescription = "Logo"
                    )
                }
                Text(

                    participantItem.name ?: IConstant.EMPTY,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }



    @Composable
    fun HistoryRow(history: HistoryItem, participantItem: ParticipantItem) {

        Column(modifier = Modifier.fillMaxWidth().padding(Dimens.doubleSpace)) {
            Text("Week ${history.week?.toString() ?: "1"}",   style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(Dimens.space))
            history.nominations?.let {
                Text("Nominated To", style = MaterialTheme.typography.titleSmall)
                history.nominations.forEach {id->
                   PiGlobalInfo.episodeDetail?.participants?.firstOrNull { it.id == id.toString() }?.let {
                       RenderParticipant(it)
                   }
                }
            }
            Spacer(modifier = Modifier.height(Dimens.doubleSpace))

            history.nominatedBy?.let { lstParticipant->
                Text("Nominated By", style = MaterialTheme.typography.titleSmall)
                lstParticipant.forEach {
                    RenderParticipant(it)
                }
            }
            Spacer(modifier = Modifier.height(Dimens.doubleSpace))
            history.notes?.let {
                Text("Notes", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    for (note in history.notes) {
                        AssistChip(
                            modifier = Modifier.padding(Dimens.space),
                            onClick = {},
                            label = {
                                Text(note)
                            })
                    }
                }
            }
        }
    }
}