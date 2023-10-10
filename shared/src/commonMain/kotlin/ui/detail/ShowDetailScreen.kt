package ui.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Chip
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import io.github.aakira.napier.Napier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.IConstant
import model.ShowDetail
import model.ShowItem
import network.Resource
import ui.component.PiProgressIndicator
import ui.theme.Dimens
import ui.theme.captain
import ui.theme.evicted
import ui.theme.nominated

class ShowDetailScreen (private val showItem: ShowItem):Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val detailModel = getScreenModel<ShowDetailModel>()
        LaunchedEffect(showItem) {
            showItem.moreInfo?.let { detailModel.fetchShowDetails(it) }
        }
        val state by detailModel.showDetailState.collectAsState()
        Scaffold (topBar = {
            TopAppBar(title = {
                Text("Dashboard")
            })
        }) {
            when (state.status) {
                Resource.Status.LOADING-> {
                    PiProgressIndicator()
                }
                Resource.Status.SUCCESS -> {
                    RenderContestantList(state.data, modifier = Modifier.padding(it))
                }
                Resource.Status.ERROR->{

                }

                else -> {

                }
            }
        }
    }

    @Composable
    fun EmptyPlaceholder(title:String, modifier: Modifier) {
        Card (modifier = modifier.padding(Dimens.doubleSpace).fillMaxWidth()) {
            Icon(imageVector = Icons.Default.Error, contentDescription = title)
            Spacer(modifier = Modifier.height(Dimens.space))
            Text(title, style = MaterialTheme.typography.bodyLarge)

        }
    }
    @Composable
    fun RenderContestantList(data: ShowDetail?, modifier: Modifier) {
        LazyColumn (modifier = modifier.fillMaxSize().padding(Dimens.doubleSpace)){
            item {
                Text(stringResource(MR.strings.title_contestant), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.padding(bottom = Dimens.doubleSpace))
            }
            if (data?.participants == null) {
                item {
                    EmptyPlaceholder(stringResource(MR.strings.error_empty_contestant), modifier = modifier)
                }
            } else {
                items(data.participants.sortedByDescending { it.isNominated == true }.sortedByDescending { it.isCaptain == true }) { participant->
                    Column  {
                        Card (modifier = Modifier.padding(Dimens.space)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(Dimens.space),
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

                                }
                            }
                            Row(
                                modifier = Modifier.padding(start = Dimens.space, bottom = Dimens.space, end = Dimens.space),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Dimens.space)
                            ) {
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
                                    if(participant.eliminatedDate?.isNotBlank() ==true) {
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
                                            ))
                                        }
                                    }


                                }

                            }
                        }
                    }
                }
            }

        }
    }




}


