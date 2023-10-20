package ui.participant

import analytics.AnalyticConstant
import analytics.AnalyticLogger
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import model.IConstant
import model.LinkType
import model.OfficialVoteItem
import model.ParticipantItem
import model.VotingOption
import model.piShadow
import ui.native.LinkLauncher
import ui.theme.Dimens

@Composable
fun RenderVotingOption(
    votingOption: VotingOption,
    participantItem: ParticipantItem,
    linkLauncher: LinkLauncher,
    analyticLogger: AnalyticLogger
) {
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
                                    analyticLogger.logEvent(AnalyticConstant.Event.CLICKED, params = mapOf(Pair(AnalyticConstant.Params.URL,link)))
                                    linkLauncher.openLink(
                                        link
                                    )
                                }
                            }

                            LinkType.DIAL -> {
                                voteItem.link?.let { link ->
                                    analyticLogger.logEvent(AnalyticConstant.Event.CLICKED, params = mapOf(Pair(AnalyticConstant.Params.URL,link)))

                                    linkLauncher.dialNumber(
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

    RenderUnofficialVoting(votingOption, linkLauncher, analyticLogger)

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

@Composable
fun RenderUnofficialVoting(
    votingOption: VotingOption,
    linkLauncher: LinkLauncher,
    analyticLogger: AnalyticLogger
) {
    // Unofficial vote option
    votingOption.unofficialVoting?.let { lstUnOfficaOption ->
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
                        analyticLogger.logEvent(AnalyticConstant.Event.CLICKED, params = mapOf(Pair(AnalyticConstant.Params.URL, unOfficialOption.link?:IConstant.EMPTY)))
                        linkLauncher.openLink(
                            unOfficialOption.link ?: IConstant.EMPTY
                        )
                    }
                }
            }
        }
    }

    votingOption.polls?.let { lstUnOfficaOption ->
        Text(
            "Polls",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(Dimens.doubleSpace)
        )
        Surface(modifier = Modifier.padding(Dimens.doubleSpace).piShadow()) {
            Column {
                lstUnOfficaOption.forEach { unOfficialOption ->
                    VoteOptionRow(unOfficialOption.name ?: IConstant.EMPTY) {
                        analyticLogger.logEvent(AnalyticConstant.Event.CLICKED, params = mapOf(Pair(AnalyticConstant.Params.URL, unOfficialOption.link?:IConstant.EMPTY)))
                        linkLauncher.openLink(
                            unOfficialOption.link ?: IConstant.EMPTY
                        )
                    }
                }
            }
        }
    }
}
