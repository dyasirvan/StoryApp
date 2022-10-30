package com.story.app.core

import android.util.Log
import com.story.app.core.data.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class OnlineBoundResource<RequestType> {
    private var result: Flow<Resource<RequestType>> = flow {

        emit(Resource.Loading())
        when (val apiResponse = createCall().first()) {
            is ApiResponse.Success -> {
                getResponse(apiResponse.body)
                emit(Resource.Success(apiResponse.body))
                /* emitAll(
                     getResponse(apiResponse.body).map {
                     Resource.Success(it)
                 })*/
            }
            is ApiResponse.Empty -> {}
            is ApiResponse.Error -> {
                emit(Resource.Error(apiResponse.errorMessage))
                Log.e("OnlineBoundResource", apiResponse.errorMessage)
            }
        }

    }

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract fun getResponse(data: RequestType)

    fun asFlow(): Flow<Resource<RequestType>> = result
}