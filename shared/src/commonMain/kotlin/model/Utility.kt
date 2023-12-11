package model

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until
import ui.theme.Dimens
import kotlin.random.Random

object PiGlobalInfo {
    var episodeDetail:ShowDetail?=null
    var voteInfo:VoteInfo? = null
}
fun String.toDate():Instant? {
    return try {
        Instant.parse(this+"T00:00:00Z")
    }catch (ex:Exception) {
        null
    }

}

fun Instant.daysUntilNow(): Long {
    return this.until(Clock.System.now(), DateTimeUnit.DAY, TimeZone.UTC)
}

@Deprecated("Use daysUntilNow", replaceWith = ReplaceWith("daysUntilNow()"))
fun Instant.daysSoFar(): Long {
    return this.until(Clock.System.now(), DateTimeUnit.DAY, TimeZone.UTC)
}

fun Instant.daysSoFar(endDate:Instant): Long {
    return this.until(endDate, DateTimeUnit.DAY, TimeZone.UTC)
}


fun Modifier.piShadow(elevation: Dp = Dimens.space): Modifier = composed {
    shadow(elevation = elevation, shape = RoundedCornerShape(elevation), clip = true)
}


fun generateRandomColorExcludingWhite(): Color {
    var red: Int
    var green: Int
    var blue: Int

    // Generate random RGB values while ensuring they are not all 255 (white)
    do {
        red = Random.nextInt(256)
        green = Random.nextInt(256)
        blue = Random.nextInt(256)
    } while (red == 255 && green == 255 && blue == 255) // Continue until not white

    // Create and return a Color object with the RGB components
    return Color(red, green, blue)
}