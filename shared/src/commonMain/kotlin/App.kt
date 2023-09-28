import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.theme.BiggBossAppTheme


@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    BiggBossAppTheme {
        val app_name: String = stringResource(MR.strings.app_name)

        var greetingText by remember { mutableStateOf("Hello, World! $app_name") }
        var showImage by remember { mutableStateOf(false) }


        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            Button(onClick = {
                greetingText = "Hello, $app_name ${getPlatformName()}"
                showImage = !showImage
            }) {
                Text(greetingText)
            }


            TextField(greetingText, onValueChange = { greetingText = it })

            AnimatedVisibility(showImage) {
                Column {
                    Image(imageVector = Icons.Default.Add, contentDescription = "Add icon")
                }
            }
        }
    }
}


expect fun getPlatformName(): String