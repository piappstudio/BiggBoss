package ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import co.touchlab.kermit.Logger
import com.aay.compose.barChart.model.BarParameters
import com.biggboss.shared.MR
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import dev.icerock.moko.graphics.parseColor
import dev.icerock.moko.resources.compose.stringResource
import model.ParticipantItem
import model.generateRandomColorExcludingWhite

@Composable
fun PiWebChart(modifier:Modifier = Modifier, participants: List<ParticipantItem>?) {

    val html = "<!-- ... (previous HTML content) ... -->" +
            "\n <head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Chart.js Example</title>\n" +
            "    <!-- Include Chart.js library -->\n" +
            "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
            "</head>" +
            "\n" +
            "<body>\n" +
            "<canvas id=\"myChart\" style=\"max-width: 100%; max-height: 100%;\">></canvas>\n" +
            "<script>\n" +
            "        // Update the chart function\n" +
            "        function updateChart() {\n" +
            "            var ctx = document.getElementById('myChart').getContext('2d');\n" +
            "            var canvas = document.getElementById('myChart');\n" +
            "\n" +
            "            var myChart = new Chart(ctx, {\n" +
            "                type: 'line',\n" +
            "                data: {\n" +
            "                    labels: pilabels,\n" +
            "                    datasets: pidatasets\n" +
            "                },\n" +
            "            });\n" +
            "        }\n" +
            "\n" +
            "        // Call the function initially\n" +
            "        updateChart();\n" +
            "    </script>\n" +
            "</body>\n" +
            "<!-- ... (rest of HTML content) ... -->\n"


    participants?.let { lstParticipants ->
        val nominated = stringResource(MR.strings.title_nominated)
        RenderTitle("Weekly nomination votes")
        val lstBarParameter = mutableListOf<BarParameters>()
        val lstWeeks =
            lstParticipants.flatMap { it.history ?: emptyList() }.mapNotNull { it.week }
                .distinct()
        lstParticipants.forEach { participant ->
            val lstVotes = mutableListOf<Double>()
            for (week in lstWeeks) {
                val currentWeek = participant.history?.firstOrNull {
                    it.week == week && it.notes?.contains(nominated) == true
                }
                if (currentWeek != null) {
                    Logger.d { "Week: $week, Participant: ${participant.name}, Vote: ${currentWeek.nominatedBy?.size}" }
                    lstVotes.add(currentWeek.nominatedBy?.size?.toDouble() ?: 0.0)
                } else {
                    lstVotes.add(0.0)
                }
            }

            lstBarParameter.add(
                BarParameters(
                    participant.name ?: "",
                    data = lstVotes,
                    generateRandomColorExcludingWhite()
                )
            )
        }

        val labels = lstWeeks.map { "Week: ${it}" }

        val mutDataSet = mutableListOf<String>()
        for (parameter in lstBarParameter) {
            mutDataSet.add("{\n label: '${parameter.dataName}', data: ${parameter.data.map { it.toInt() }.joinToString(",", "[", "]")}," +
                    "borderColor: '${colorToRgbaString(parameter.barColor) }',\n" +
                    "borderWidth: 1}")
        }

        val dataSetString = mutDataSet.joinToString(",", "[", "]")
        val htmlDataSet = html.replace("pidatasets", dataSetString).replace("pilabels", labels.joinToString(",", "[", "]"){"'${it}'"})


        val webViewState = rememberWebViewStateWithHTMLData(htmlDataSet)
        webViewState.webSettings.apply {
            isJavaScriptEnabled = true
            logSeverity = KLogSeverity.Debug
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            androidWebSettings.apply {
                isAlgorithmicDarkeningAllowed = true
                safeBrowsingEnabled = true
                allowFileAccess = true
            }
        }


        val webViewNavigator = rememberWebViewNavigator()
        var jsRes by mutableStateOf("Chart Data")
        WebView(
            state = webViewState,
            modifier = modifier,
            captureBackPresses = false,
            navigator = webViewNavigator)
    }
}

fun colorToRgbaString(color: Color): String {
    val argb = color.toArgb()
    val red = (argb shr 16) and 0xFF
    val green = (argb shr 8) and 0xFF
    val blue = argb and 0xFF
    val alpha = (argb shr 24) and (0xFF / 255.0).toInt() // Normalize alpha to a range of 0.0 to 1.0

    return "rgba($red, $green, $blue, $alpha)"
}