package network

import di.PiNetwork
import di.isNetworkAvailable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import model.ShowDetail
import model.ShowList
import model.TrendItem
import model.WeeklyInfo

class PiRepository(private val httpClient: HttpClient) {

    suspend fun fetchShows(): Flow<Resource<ShowList?>> {
        return makeSafeApiCall {
            val response =
                httpClient.get(PiNetwork.URL.plus(PiNetwork.EndPoint.SHOWS)).body<ShowList>()
            Resource.success(response)
        }
    }

    suspend fun fetchDetail(showId: String): Flow<Resource<ShowDetail?>> {
        return makeSafeApiCall {
            val response = httpClient.get(showId).body<ShowDetail>()
            Resource.success(response)
        }
    }
    suspend fun fetchTrends(trendUrl: String): Flow<Resource<List<TrendItem>?>> {
        return makeSafeApiCall {
            val response = httpClient.get(trendUrl).body<List<TrendItem>?>()
            Resource.success(response)
        }
    }

    private suspend fun <T> makeSafeApiCall(api: suspend () -> Resource<T?>) = flow {
        try {
            emit(Resource.loading())
            if (isNetworkAvailable()) {
                val response = api.invoke()
                emit(Resource.success(response.data))

            } else {
                emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_NOT_AVAILABLE)))
            }

        } catch (ex: Exception) {
            println(ex.message)
            emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_CONNECTION_FAILED)))
        }
    }


}