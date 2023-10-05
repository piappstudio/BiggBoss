package network

import di.PiNetwork
import di.isNetworkAvailable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import model.ShowDetail
import model.ShowList

class PiRepository (private val httpClient:HttpClient) {

    fun fetchShows():Flow<Resource<ShowList?>> {
        return makeSafeApiCall {
            val response = httpClient.get(PiNetwork.URL.plus(PiNetwork.EndPoint.SHOWS)).body<ShowList>()
            Resource.success(response)
        }
    }
    fun fetchDetail(showId:String):Flow<Resource<ShowDetail?>> {
        return makeSafeApiCall {
            val response = httpClient.get(PiNetwork.URL.plus(PiNetwork.EndPoint.DETAIL).plus(showId)).body<ShowDetail>()
            Resource.success(response)
        }
    }
    private fun <T> makeSafeApiCall(api:suspend ()-> Resource<T?>) = flow {
        try {
            emit(Resource.loading())
            if (isNetworkAvailable()) {
               val response = api.invoke()
                emit(Resource.success(response.data))
            } else {
                emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_NOT_AVAILABLE)))
            }

        }catch (ex:Exception) {
            emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_CONNECTION_FAILED)))
        }

    }
    
}