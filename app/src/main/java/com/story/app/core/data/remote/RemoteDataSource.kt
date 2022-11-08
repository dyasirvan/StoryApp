package com.story.app.core.data.remote

import android.content.Context
import android.util.Log
import com.story.app.R
import com.story.app.core.data.remote.model.EmailResponse
import com.story.app.core.data.remote.model.GeneralResponse
import com.story.app.core.data.remote.model.LoginResponse
import com.story.app.core.data.remote.model.StoriesResponse
import com.story.app.core.data.remote.network.ApiResponse
import com.story.app.core.data.remote.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

class RemoteDatasource(
    private val apiService: ApiService?,
    private val context: Context
) {

    suspend fun doLogin(email: String, password: String): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                val response = apiService?.doLogin(email, password)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "doLogin"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun doRegister(
        email: String,
        password: String,
        name: String
    ): Flow<ApiResponse<GeneralResponse>> {
        return flow {
            try {
                val response = apiService?.doRegister(email, password, name)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "doRegister"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStories(
        token: String,
        page: String?,
        size: String?,
        location: String?
    ): Flow<ApiResponse<StoriesResponse>> {
        return flow {
            try {
                val response = apiService?.getStories(token, page, size, location)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "getStories"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Float,
        lon: Float
    ): Flow<ApiResponse<GeneralResponse>> {
        return flow {
            try {
                val response = apiService?.addNewStory(token, file, description.toRequestBody("text/plain".toMediaType()), lat, lon)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "addNewStory"))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun exceptionLog(e: Exception, tagLog: String): ApiResponse.Error {
        val tag = this::class.java.simpleName

        when (e) {
            is SocketTimeoutException -> {
                Log.e(tag, e.message.toString())
                return ApiResponse.Error(
                    e.message.toString() + ", " + context.resources.getString(
                        R.string.check_your_internet_connection
                    )
                )
            }

            is HttpException -> {
                return try {
                    val `object` = JSONObject(e.response()?.errorBody()?.string().toString())
                    val messageString: String = `object`.getString("message")
                    Log.e(tag, messageString)
                    ApiResponse.Error(messageString)
                } catch (e: Exception) {
                    val messageString = context.resources.getString(R.string.something_wrong)
                    Log.e(tag, messageString)
                    ApiResponse.Error(messageString)
                }
            }

            is NoSuchElementException -> {
                Log.e(tag, e.message.toString())
                return (ApiResponse.Error(
                    e.message.toString() + ", " + context.resources.getString(
                        R.string.check_your_internet_connection
                    )
                ))
            }

            else -> {
                val messageString = context.resources.getString(R.string.something_wrong)
                Log.e(tag, e.message.toString())
                return ApiResponse.Error(messageString)
            }
        }
    }
}