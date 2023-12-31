import analytics.AnalyticConstant
import analytics.AnalyticLogger
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.IConstant
import model.TrendItem
import model.piShadow
import ui.native.LinkLauncher
import ui.theme.Dimens

@Composable
fun RenderTrendingScreen(lstTrends:List<TrendItem>?, linkLauncher: LinkLauncher, analyticLogger: AnalyticLogger) {
    LazyColumn (modifier = Modifier.padding(Dimens.doubleSpace)) {
        lstTrends?.forEach { trendItem->
            item {
                Text(
                    trendItem.title?:IConstant.EMPTY,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(Dimens.doubleSpace))

            }

            trendItem.items?.let { lstItem ->
                items(lstItem) { trend->
                    Surface (modifier = Modifier.fillMaxWidth().padding(Dimens.space).piShadow().clickable {
                        trend.url?.let {
                            analyticLogger.logEvent(AnalyticConstant.Event.YOUTUBE, mapOf(Pair(AnalyticConstant.Params.URL, it )))
                            linkLauncher.openLink(it)
                        }

                    }) {
                        Row (modifier = Modifier.fillMaxWidth().padding(Dimens.space), verticalAlignment = Alignment.CenterVertically)  {
                            trend.image?.let { imgUrl ->
                                KamelImage(
                                    modifier = Modifier.width(150.dp),
                                    resource = asyncPainterResource(imgUrl),
                                    contentDescription = "Logo"
                                )
                            }
                            Spacer(modifier = Modifier.padding(Dimens.space))
                            Text(trend.title?:IConstant.EMPTY, modifier = Modifier.weight(1f), maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }

            }
        }
    }

}