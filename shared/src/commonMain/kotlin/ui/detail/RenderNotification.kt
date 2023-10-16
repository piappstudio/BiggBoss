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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.IConstant
import model.ParticipantItem
import model.ShowDetail
import model.VotingOption
import model.piShadow
import ui.component.shared.PiSingleNotification
import ui.component.shared.SingleNotification
import ui.detail.ShowDetailScreen
import ui.participant.ParticipantDetailScreen
import ui.theme.Dimens
import ui.theme.captain
import ui.theme.evicted
import ui.theme.nominated

fun renderNotification(
    title: StringResource,
    lazyListScope: LazyListScope,
    notifications: List<PiSingleNotification>) {
    lazyListScope.item {
        Text(
            stringResource(title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(bottom = Dimens.doubleSpace))
    }
    lazyListScope.items(notifications) { notification ->
        SingleNotification(modifier = Modifier.padding(Dimens.space), notification)
        Spacer(modifier = Modifier.height(Dimens.space))
    }
}