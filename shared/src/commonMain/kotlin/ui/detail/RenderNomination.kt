package ui.detail

import analytics.AnalyticLogger
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.biggboss.shared.MR
import model.ShowDetail
import renderSection
import ui.component.shared.PiSingleNotification
import ui.component.shared.SingleNotification
import ui.theme.Dimens

@Composable
fun RenderNominationScreen(data: ShowDetail?, modifier: Modifier, analyticLogger: AnalyticLogger) {
    LazyColumn(modifier = modifier.fillMaxWidth().padding(Dimens.doubleSpace)) {
        val lstNominated = data?.participants?.filter { it.isNominated == true }
        if (lstNominated.isNullOrEmpty()) {
            item {
                SingleNotification(modifier = Modifier.padding(Dimens.doubleSpace), PiSingleNotification("Nomination is in progress!", "Nomination process is yet to be completed. Please wait, we will update the list as soon as possible"))
            }
        } else {
            renderSection( MR.strings.title_nomination,this, lstNominated, data, analyticLogger)
        }

    }
}