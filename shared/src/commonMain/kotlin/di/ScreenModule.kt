package di

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import model.ShowItem
import ui.detail.ShowDetailScreen
import ui.home.HomeScreen

sealed class BiggBossScreen:ScreenProvider {
    object Shows:BiggBossScreen()
    data class ShowDetail(val title:String, val url:String, val trendUrl:String, val startDate:String, val voteUrl:String ):BiggBossScreen()
}

val bbScreenModule = screenModule {
    register<BiggBossScreen.Shows> {
        HomeScreen()
    }
    register<BiggBossScreen.ShowDetail> { provider->
        ShowDetailScreen(provider.title, provider.url, provider.trendUrl, provider.startDate, provider.voteUrl)
    }
}