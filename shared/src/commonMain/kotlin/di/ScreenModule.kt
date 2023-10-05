package di

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import ui.Detail.ShowDetailScreen
import ui.home.HomeScreen

sealed class BiggBossScreen:ScreenProvider {
    object Shows:BiggBossScreen()
    data class ShowDetail(val id:String):BiggBossScreen()
}

val bbScreenModule = screenModule {
    register<BiggBossScreen.Shows> {
        HomeScreen()
    }
    register<BiggBossScreen.ShowDetail> { provider->
        ShowDetailScreen(provider.id)
    }
}