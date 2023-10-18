package model

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until
import ui.theme.Dimens

object PiGlobalInfo {
    var episodeDetail:ShowDetail?=null
}
fun String.toDate():Instant {
    return Instant.parse(this+"T00:00:00Z")
}

fun Instant.daysSoFar(): Long {
    return this.until(Clock.System.now(), DateTimeUnit.DAY, TimeZone.UTC)
}

fun Modifier.piShadow(elevation: Dp = Dimens.space): Modifier = composed {
    shadow(elevation = elevation, shape = RoundedCornerShape(elevation), clip = true)
}