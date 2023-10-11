package ui.participant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.getScreenModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.serialization.json.Json
import model.HistoryItem
import model.IConstant
import model.LinkType
import model.OfficialVoteItem
import model.ParticipantItem
import model.VotingOption
import model.piShadow
import ui.native.LinkLauncher
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

                if (participantItem.isNominated == true) {

                    // Official vote option
                    votingOption.officialVote?.let { lstOptions ->
                        val mutOfficialVote = mutableListOf<OfficialVoteItem>()
                        mutOfficialVote.addAll(lstOptions)
                        mutOfficialVote.add(
                            OfficialVoteItem(
                                name = "Missed Call to +91${participantItem.dialNumber}",
                                link = "+91${participantItem.dialNumber}",
                                type = LinkType.DIAL
                            )
                        )

                        Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                        Text(
                            "Official Voting Options",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(Dimens.doubleSpace)
                        )
                        Surface(modifier = Modifier.padding(Dimens.doubleSpace).piShadow()) {
                            Column {
                                mutOfficialVote.forEach { voteItem ->
                                    VoteOptionRow(voteItem.name ?: IConstant.EMPTY) {
                                        when (voteItem.type) {
                                            LinkType.URL -> {
                                                voteItem.link?.let { link ->
                                                    participantDetailViewModel.linkLauncher.openLink(
                                                        link
                                                    )
                                                }
                                            }

                                            LinkType.DIAL -> {
                                                voteItem.link?.let { link ->
                                                    participantDetailViewModel.linkLauncher.dialNumber(
                                                        link
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

                    // Unofficial vote option
                    votingOption.unofficialVoting?.let { lstUnOfficaOption ->
                        Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                        Text(
                            "Unofficial Voting Option",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(Dimens.doubleSpace)
                        )
                        Surface(modifier = Modifier.padding(Dimens.doubleSpace).piShadow()) {
                            Column {
                                lstUnOfficaOption.forEach { unOfficialOption ->
                                    VoteOptionRow(unOfficialOption.name ?: IConstant.EMPTY) {
                                        participantDetailViewModel.linkLauncher.openLink(
                                            unOfficialOption.link ?: IConstant.EMPTY
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

                participantItem.history?.let { history ->
                    Spacer(modifier = Modifier.height(Dimens.doubleSpace))
                    Text(
                        "History",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(Dimens.doubleSpace)
                    )

                    for (historyItem in history) {
                        Surface(modifier = Modifier.padding(Dimens.doubleSpace).piShadow()) {
                            HistoryRow(historyItem)
                        }
                    }

                }


            }


        }
    }


    @Composable
    fun HistoryRow(history: HistoryItem) {
        Column(modifier = Modifier.fillMaxWidth().padding(Dimens.doubleSpace)) {
            Text("Week ${history.week?.toString() ?: "1"}")

            history.notes?.let {
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

    @Composable
    fun VoteOptionRow(title: String, callback: () -> (Unit)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().clickable {
                callback.invoke()
            }.padding(Dimens.doubleSpace)
        ) {
            Text(title)
            Icon(imageVector = Icons.Default.ChevronRight, "Detail page")
        }
    }
}