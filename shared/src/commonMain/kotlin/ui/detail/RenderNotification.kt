import analytics.AnalyticLogger
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import ui.component.shared.PiSingleNotification
import ui.component.shared.SingleNotification
import ui.theme.Dimens

@Composable
fun RenderNotification(
    title: StringResource,
    notifications: List<PiSingleNotification>?, analyticLogger: AnalyticLogger) {
    Column (modifier = Modifier.padding(Dimens.doubleSpace)){
        Text(
            stringResource(title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(bottom = Dimens.doubleSpace))
        notifications?.let {
            notifications.forEach {notification->
                SingleNotification(modifier = Modifier.padding(Dimens.space), notification)
                Spacer(modifier = Modifier.height(Dimens.space))
            }
        }
    }

}