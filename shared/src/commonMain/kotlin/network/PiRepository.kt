package network

import co.touchlab.kermit.Logger
import di.PiNetwork
import di.isNetworkAvailable
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import model.ShowDetail
import model.ShowList
import model.TrendItem
import model.WeeklyInfo
import network.PiRepository.FileNamee.DETAILS
import network.PiRepository.FileNamee.SHOWS
import network.PiRepository.FileNamee.TRENDS
import ui.native.loadFromCache
import ui.native.writeToCache

class PiRepository(private val httpClient: HttpClient) {

    object FileNamee {
        const val SHOWS = "shows"
        const val DETAILS  = "details"
        const val TRENDS = "trends"
    }

    suspend fun fetchShows(): Flow<Resource<ShowList?>> {
        return makeSafeApiCall (fileName = SHOWS) {
            val response =
                httpClient.get(PiNetwork.URL.plus(PiNetwork.EndPoint.SHOWS)).body<ShowList>()
            Resource.success(response)
        }
    }

    suspend fun fetchDetail(showId: String): Flow<Resource<ShowDetail?>> {
        return makeSafeApiCall (fileName = DETAILS+showId.split("/").lastOrNull()) {
            val response = httpClient.get(showId).body<ShowDetail>()
            Resource.success(response)
        }
    }
    suspend fun fetchTrends(trendUrl: String): Flow<Resource<List<TrendItem>?>> {
        return makeSafeApiCall (fileName = TRENDS+trendUrl.split("/").lastOrNull()) {
            val response = httpClient.get(trendUrl).body<List<TrendItem>?>()
            Resource.success(response)
        }
    }

    private suspend inline fun <reified T> makeSafeApiCall(fileName:String, crossinline api: suspend () -> Resource<T?>) = flow {
        emit(Resource.loading())
        val cacheJson = loadFromCache(fileName)
        try {
            cacheJson?.let {
                Logger.d (
                    "Loading it from local: $cacheJson"
                )
                val jsonObject = Json {ignoreUnknownKeys = true}.decodeFromString<T>(cacheJson)
                Logger.d (
                    "Loading it from local: "
                )
                emit(Resource.success(jsonObject))
            }

            if (isNetworkAvailable()) {
                val response = api.invoke()
                Logger.d (
                    "Network call: $response"
                )
                val data = response.data
                if (response.status == Resource.Status.SUCCESS) {
                    val gson = Json.encodeToString(data)
                    val isWriteSuccessful = writeToCache(gson, fileName)
                    Logger.d (
                        "Writing is completed: $isWriteSuccessful"
                    )
                }

                if (cacheJson==null) {
                    emit(Resource.success(data))
                }


            } else {
                if (cacheJson == null) {
                    emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_NOT_AVAILABLE)))
                }
            }

        } catch (ex: Exception) {
            Logger.e( ex.message.toString() , ex)
            if (cacheJson == null) {
                emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_CONNECTION_FAILED)))

            }
        }
    }


}