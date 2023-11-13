package ui.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import ui.theme.Dimens

@Composable
fun RenderBarChart(
    title: String,
    yAxisData: List<Double>,
    xAxisData: List<String>,
    color: Color
) {
    Box(Modifier.fillMaxWidth().height(400.dp)) {
        BarChart(
            chartParameters = listOf(
                BarParameters(
                    title,
                    data = yAxisData,
                    barColor = color
                )
            ),
            gridColor = Color.DarkGray,
            xAxisData = xAxisData,
            isShowGrid = true,
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 5,
            barWidth = 20.dp,
            spaceBetweenBars = Dimens.doubleSpace,
            spaceBetweenGroups = Dimens.space,
        )
    }
}

@Composable
fun RenderTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = Dimens.doubleSpace),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold
    )
}
