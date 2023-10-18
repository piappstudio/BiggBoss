package ui.component.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.piShadow
import ui.theme.Dimens

@Serializable
data class PiSingleNotification (
    @SerialName("title")
    val title:String,
    @SerialName("message")
    val message:String?)
@Composable
fun SingleNotification(modifier: Modifier, piSingleNotification: PiSingleNotification) {
    piSingleNotification.message?.let {
        Surface (modifier = modifier.piShadow()) {
            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(Dimens.space)) {
                Icon(Icons.Default.NotificationImportant, contentDescription = "Important notification")
                Column (modifier = Modifier.padding(start = Dimens.space)) {
                    Text(piSingleNotification.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(Dimens.space))
                    Text(piSingleNotification.message, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }

            }
        }
    }
}

