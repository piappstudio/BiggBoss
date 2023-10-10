package di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import network.PiRepository
import org.koin.dsl.module

object PiNetwork {
    const val URL =  "https://raw.githubusercontent.com/piappstudio/resources/main/biggboss/"
    object EndPoint {
        const val SHOWS = "json/shows.json"
        const val DETAIL = "json/shows/"
    }
}

val httpClientModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)

            }
        }
    }
}


val repositoryModule = module {
    single<PiRepository> { PiRepository(get()) }
}

expect fun isNetworkAvailable():Boolean