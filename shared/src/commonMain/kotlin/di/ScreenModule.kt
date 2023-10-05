package di

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import model.ShowItem
import ui.detail.ShowDetailScreen
import ui.home.HomeScreen

sealed class BiggBossScreen:ScreenProvider {
    object Shows:BiggBossScreen()
    data class ShowDetail(val showItem: ShowItem):BiggBossScreen()
}

val bbScreenModule = screenModule {
    register<BiggBossScreen.Shows> {
        HomeScreen()
    }
    register<BiggBossScreen.ShowDetail> { provider->
        ShowDetailScreen(provider.showItem)
    }
}