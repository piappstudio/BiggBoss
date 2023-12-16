package ui.compare

import analytics.AnalyticConstant
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import dev.icerock.moko.resources.compose.stringResource
import di.getScreenModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.piShadow
import ui.theme.Dimens

class CompareScreen  (val ids:String):Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val detailModel = getScreenModel<CompareViewModel>()
        val participants by detailModel.getParticipants(ids).collectAsState(emptyList())

        LaunchedEffect(Unit) {
            detailModel.analyticLogger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(Pair(AnalyticConstant.Params.SCREEN_NAME, "CompareScreen"), Pair(AnalyticConstant.Params.PART_IDS, ids)))
        }
        Scaffold (topBar = {
            TopAppBar(title = {
                Text("Participant Comparison")
            }, navigationIcon = {
                IconButton(onClick = {
                    navigator.pop()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
                }

            })
        }){
            Row(modifier = Modifier.padding(it).fillMaxWidth().padding(Dimens.doubleSpace).horizontalScroll(rememberScrollState())) {
                participants?.forEachIndexed { index, participant ->
                    Surface (modifier = Modifier.padding(Dimens.space).piShadow()) {
                        Column(
                            modifier = Modifier.width(150.dp).padding(Dimens.space)
                        ) {
                            Spacer(Modifier.height(Dimens.space))
                            participant.image?.let { imgUrl ->
                                KamelImage(
                                    modifier = Modifier.size(75.dp).clip(shape = CircleShape)
                                        .border(1.dp, Color.Gray, CircleShape)
                                        .align(Alignment.CenterHorizontally),
                                    resource = asyncPainterResource(imgUrl),
                                    contentDescription = "Participant image"
                                )
                            }
                            Spacer(Modifier.height(Dimens.space))
                            Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
                                RenderItemWithTitle(participant.name ?: "", null)
                                RenderItemWithTitle("Days", participant.totalDays())
                                RenderItemWithTitle(
                                    "Gold Stars",
                                    participant.noOfStars().toString()
                                )
                                RenderItemWithTitle(
                                    "TTF Points",
                                    participant.noOfPoint().toString()
                                )
                                val nominated = stringResource(MR.strings.title_nominated)
                                RenderItemWithTitle(
                                    "Nominations",
                                    participant.countBasedOnKey(nominated).toString()
                                )
                                val captain = stringResource(MR.strings.title_captain)
                                RenderItemWithTitle(
                                    "Captain",
                                    participant.countBasedOnKey(captain).toString()
                                )
                                val sbh = stringResource(MR.strings.title_sbh)
                                RenderItemWithTitle(
                                    "Small Boss House",
                                    participant.countBasedOnKey(sbh).toString()
                                )
                                val yellowCard = stringResource(MR.strings.title_yellow_card)
                                RenderItemWithTitle(
                                    "Yellow Strike",
                                    participant.countBasedOnKey(yellowCard).toString()
                                )
                                val arrOfId =
                                    participants?.filter { part -> part.id != participant.id }
                                        ?.map { mapPart -> mapPart.id?.toInt() ?: 0 }
                                arrOfId?.let {
                                    val mapOfNominations =
                                        participant.howManyTimeINominated(arrOfId)
                                    mapOfNominations.forEach { item ->
                                        RenderItemWithTitle(
                                            "Nominated: ${participants?.firstOrNull { part -> part.id == item.key.toString() }?.name}",
                                            item.value.toString()
                                        )
                                    }
                                }
                                Spacer(Modifier.height(Dimens.space))
                            }
                        }
                    }
                }
            }
        }
    }



    @Composable
    fun RenderItemWithTitle(title:String, value:String?) {
        Spacer(Modifier.height(Dimens.doubleSpace))
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = Dimens.space),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal
        )
        value?.let {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = Dimens.space),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }

    }

}