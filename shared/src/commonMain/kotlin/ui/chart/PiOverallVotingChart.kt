package ui.chart

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aay.compose.barChart.model.BarParameters
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import model.ParticipantItem
import model.PollItem
import model.generateRandomColorExcludingWhite
import ui.theme.Dimens

@Composable
fun PiOverallVotingChart(
    modifier: Modifier = Modifier,
    participants: List<ParticipantItem>?,
    polls: List<PollItem>?
) {
    val html = "<!-- ... (previous HTML content) ... -->" +
            "\n <head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Chart.js Example</title>\n" +
            "    <!-- Include Chart.js library -->\n" +
            "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
            "    <script src=\"https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom\"></script>\n" +
            "</head>" +
            "<style>\n" +
            "        canvas {\n" +
            "            max-width: 100%;\n" +
            "            height: auto;\n" +
            "        }\n" +
            "    </style>" +
            "\n" +
            "<body>\n" +
            "<canvas id=\"myChart\"></canvas>\n" +
            "<script>\n" +
            "        // Update the chart function\n" +
            "        function updateChart() {\n" +
            "            var ctx = document.getElementById('myChart').getContext('2d');\n" +
            "            var canvas = document.getElementById('myChart');\n" +
            "\n" +
            "            var myChart = new Chart(ctx, {\n" +
            "                type: 'line',\n" + "  options: {\n responsive: true, maintainAspectRatio: false, " +

            "        plugins: {\n" +
            "          legend: {\n display: true," +

            "            position: 'top',\n" +
            "            align: 'center'\n" +
            "          }\n" +
            "        }\n" +
            "      }," +
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

    polls?.forEach { pollItem ->

        Spacer(modifier = Modifier.height(Dimens.doubleSpace))

        RenderTitle("${pollItem.name} vote shares")

        val lstBarParameter = mutableListOf<BarParameters>()
        val labels = pollItem.votes?.map { "Week ${it.week}" } ?: emptyList()
        participants?.forEach { participant ->
            val lstVotes = mutableListOf<Double>()
            pollItem.votes?.forEach { weekInfo ->
                val playerInfo =
                    weekInfo.players?.firstOrNull { it.id.toString() == participant.id }
                lstVotes.add((playerInfo?.percentage?.toDouble() ?: 0.0))
            }
            lstBarParameter.add(
                BarParameters(
                    participant.name ?: "",
                    data = lstVotes,
                    generateRandomColorExcludingWhite()
                )
            )
        }
        val mutDataSet = mutableListOf<String>()
        for (parameter in lstBarParameter) {
            mutDataSet.add(
                "{\n label: '${
                    parameter.dataName.subSequence(
                        0,
                        4
                    )
                }', data: ${parameter.data.map { it.toInt() }.joinToString(",", "[", "]")}," +
                        "borderColor: '${colorToRgbaString(parameter.barColor)}',\n" +
                        "borderWidth: 1,   pointRadius: 6, pointHoverRadius: 8 }"
            )
        }

        val dataSetString = mutDataSet.joinToString(",", "[", "]")
        val htmlDataSet = html.replace("pidatasets", dataSetString)
            .replace("pilabels", labels.joinToString(",", "[", "]") { "'${it}'" })


        val webViewState = rememberWebViewStateWithHTMLData(htmlDataSet)
        webViewState.webSettings.apply {
            isJavaScriptEnabled = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            androidWebSettings.apply {
                isAlgorithmicDarkeningAllowed = true
                safeBrowsingEnabled = true
                allowFileAccess = true
            }
        }

        WebView(
            state = webViewState,
            modifier = modifier,
            captureBackPresses = false
        )

        Spacer(modifier = Modifier.height(Dimens.doubleSpace))

    }
}