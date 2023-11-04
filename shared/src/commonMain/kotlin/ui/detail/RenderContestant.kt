import analytics.AnalyticConstant
import analytics.AnalyticLogger
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.IConstant
import model.ParticipantItem
import model.ShowDetail
import model.VotingOption
import model.daysSoFar
import model.piShadow
import model.toDate
import ui.component.shared.RenderDayScreen
import ui.detail.ShowDetailScreen
import ui.participant.ParticipantDetailScreen
import ui.theme.Dimens
import ui.theme.captain
import ui.theme.evicted
import ui.theme.nominated

fun renderSection(
    title: StringResource,
    lazyListScope: LazyListScope,
    lstNominated: List<ParticipantItem>,
    data: ShowDetail,
    analyticLogger: AnalyticLogger
    ) {
        lazyListScope.item {
            Text(
                stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.padding(bottom = Dimens.space))
        }
        lazyListScope.items(lstNominated) { participant ->
            RenderContestantRow(showDetail = data, participant, data.votingOption?: VotingOption(), analyticLogger = analyticLogger)
            Spacer(modifier = Modifier.height(Dimens.space))
        }
    }

    @Composable
    fun RenderContestantRow(showDetail: ShowDetail, participant: ParticipantItem, votingOption: VotingOption, analyticLogger: AnalyticLogger) {
        val navigator = LocalNavigator.currentOrThrow
        Surface  (modifier = Modifier.piShadow().clickable {
            val json = Json { ignoreUnknownKeys = true }
            val jsonParticipantItem = json.encodeToString(participant)
            val jsonVotingOption = json.encodeToString(votingOption)
            analyticLogger.logEvent(AnalyticConstant.Event.CLICKED, mapOf(Pair(AnalyticConstant.Params.PARTICIPANT_NAME, participant.name?:IConstant.EMPTY)))
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
                    Column(modifier = Modifier.padding(start = Dimens.space).weight(1f)) {
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

                    if(participant.eliminatedDate?.isNotBlank() == true) {
                        participant.eliminatedDate.toDate()?.let {endDate->
                            RenderDayScreen("Days", showDetail.startDate?.toDate()?.daysSoFar(endDate).toString())
                        }
                    }

                    participant.history?.lastOrNull()?.nominatedBy?.let {

                        RenderDayScreen("Votes", it.size.toString())
                    }
                }
            }
        }
    }