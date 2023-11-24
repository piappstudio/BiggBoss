package ui.detail

import analytics.AnalyticLogger
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.biggboss.shared.MR
import model.ShowDetail
import model.toDate
import renderSection
import ui.component.shared.PiSingleNotification
import ui.component.shared.SingleNotification
import ui.theme.Dimens

@Composable
fun RenderEliminatedScreen(data: ShowDetail?, modifier: Modifier, analyticLogger: AnalyticLogger) {
    LazyColumn(modifier = modifier.fillMaxWidth().padding(Dimens.doubleSpace)) {
        val lstNominated = data?.participants?.filter { it.isEliminated() }?.sortedByDescending { it.eliminatedDate?.toDate() }
        if (lstNominated.isNullOrEmpty()) {
            item {
                SingleNotification(modifier = Modifier.padding(Dimens.doubleSpace), PiSingleNotification("No eliminations at the moment", "No eliminations at the moment. Please wait, we will update the list as soon as possible"))
            }
        } else {
            renderSection( MR.strings.title_eliminated,this, lstNominated, data, analyticLogger)
        }

    }
}