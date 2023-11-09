package di

import analytics.AnalyticLogger
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import ui.chart.ChartViewModel
import ui.detail.ShowDetailModel
import ui.home.HomeScreenModel
import ui.native.LinkLauncher
import ui.participant.ParticipantDetailViewModel

@Composable
public inline fun <reified T : ScreenModel> Screen.getScreenModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = getKoin()
    return rememberScreenModel(tag = qualifier?.value) { koin.get(qualifier, parameters) }
}

val viewModel = module {
    single {
        LinkLauncher()
    }
    single {
        AnalyticLogger()
    }
    factory {
        HomeScreenModel(get(), get(), get())
    }
    factory {
        ShowDetailModel(get(), get(),get())
    }

    factory {
        ParticipantDetailViewModel(get(), get(), get())
    }

    factory {
        ChartViewModel(get(), get(), get())
    }

}