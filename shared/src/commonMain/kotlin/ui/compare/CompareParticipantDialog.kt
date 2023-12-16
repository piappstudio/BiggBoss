package ui.compare

import analytics.AnalyticConstant
import analytics.AnalyticLogger
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.PiGlobalInfo
import ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareParticipantDialog(
    selectedIds: List<String>,
    logger: AnalyticLogger,
    callback: ((isDismissed: Boolean) -> Unit)? = null
) {
    var isDialogOpen by remember { mutableStateOf(true) }
    var mutSelectedIds by remember { mutableStateOf(mutableListOf<String>()) }
    mutSelectedIds.addAll(selectedIds)
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        logger.logEvent(AnalyticConstant.Event.SCREEN_VIEW, mapOf(Pair(AnalyticConstant.Params.SCREEN_NAME, "CompareParticipantDialog")))
    }
    if (isDialogOpen) {
        AlertDialog(onDismissRequest = {
            isDialogOpen = false
            callback?.invoke(true)
        }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Card (modifier = Modifier.fillMaxSize(0.8f)){
                Text("Select Contestants to compare", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(Dimens.doubleSpace))

                Column (modifier = Modifier.padding(horizontal = Dimens.doubleSpace).weight(0.9f, true).verticalScroll(
                    rememberScrollState()
                )) {
                    PiGlobalInfo.episodeDetail?.participants?.let { lstParticipants ->
                        lstParticipants.forEach { participant ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Check Box
                                Checkbox(
                                    checked = mutSelectedIds.contains(participant.id),
                                    onCheckedChange = {
                                        val cpyIds = mutableListOf<String>()
                                        cpyIds.addAll(mutSelectedIds)
                                        if (cpyIds.contains(participant.id)) {
                                            cpyIds.remove(participant.id)
                                        } else {
                                            participant.id?.let { it1 ->
                                                cpyIds.add(
                                                    it1
                                                )
                                            }
                                        }

                                        mutSelectedIds = cpyIds
                                    }
                                )

                                Text(
                                    participant.name.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = Dimens.doubleSpace)
                                )
                            }
                        }

                    }
                }

                ElevatedButton(onClick = {
                    if (mutSelectedIds.isNotEmpty()) {
                        logger.logEvent(AnalyticConstant.Event.CLICKED, mapOf(Pair(AnalyticConstant.Params.ACTION_NAME, "Compare: $mutSelectedIds")))
                        navigator.push(CompareScreen(mutSelectedIds.joinToString (separator = ",")))
                        isDialogOpen = false
                        callback?.invoke(true)
                    }

                }, modifier = Modifier.fillMaxWidth().padding(Dimens.doubleSpace)) {
                    Text("Compare")
                }

            }
        }

    }
}
