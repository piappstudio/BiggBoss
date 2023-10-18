import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.biggboss.shared.MR
import dev.icerock.moko.resources.compose.*
import di.BiggBossScreen
import di.appModule
import di.bbScreenModule
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.KoinApplication
import org.koin.dsl.koinApplication
import ui.home.HomeScreen
import ui.theme.BiggBossAppTheme


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    ScreenRegistry {
        screenModule { bbScreenModule() }
    }
    KoinApplication(application = {
        modules(appModule())
    }) {
        BiggBossAppTheme {
            Navigator(screen = HomeScreen())
        }
    }
}

@Composable
fun SplashScreen() {
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


expect fun getPlatformName(): String