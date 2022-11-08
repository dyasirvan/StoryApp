package com.story.app.core

import androidx.paging.PagingData
import com.story.app.common.SharedPreferenceProvider
import com.story.app.core.data.local.LocalDatasource
import com.story.app.core.data.local.entity.StoryEntity
import com.story.app.core.data.paging.StoryPagingSource
import com.story.app.core.data.remote.RemoteDatasource
import com.story.app.core.data.remote.model.GeneralResponse
import com.story.app.core.data.remote.model.LoginResponse
import com.story.app.core.data.remote.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class DataRepository(
    private val remoteDatasource: RemoteDatasource,
    private val sharedPreferenceProvider: SharedPreferenceProvider,
    private val storyPagingSource: StoryPagingSource,
    private val localDatasource: LocalDatasource
): DataRepositoryImpl {
    override fun doLogin(email: String, password: String): Flow<Resource<LoginResponse>> {
        return object : OnlineBoundResource<LoginResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<LoginResponse>> {
                return remoteDatasource.doLogin(email, password)
            }

            override fun getResponse(data: LoginResponse) {
                sharedPreferenceProvider.setToken(data.loginResult?.token)
                sharedPreferenceProvider.setUserId(data.loginResult?.userId)
                sharedPreferenceProvider.setName(data.loginResult?.name)
            }
        }.asFlow()
    }

    override fun doRegister(
        email: String,
        password: String,
        name: String
    ): Flow<Resource<GeneralResponse>> {
        return object : OnlineBoundResource<GeneralResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> {
                return remoteDatasource.doRegister(email, password, name)
            }

            override fun getResponse(data: GeneralResponse) {

            }
        }.asFlow()
    }

    override fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Float,
        lon: Float
    ): Flow<Resource<GeneralResponse>> {
        return object : OnlineBoundResource<GeneralResponse>() {
            override suspend fun createCall(): Flow<ApiResponse<GeneralResponse>> {
                return remoteDatasource.addNewStory(token, file, description, lat, lon)
            }

            override fun getResponse(data: GeneralResponse) {

            }
        }.asFlow()
    }

    override fun getStories(token: String): Flow<PagingData<StoryEntity>> {
        return storyPagingSource.getStories(token)
    }

    override fun getLocalStories(): Flow<List<StoryEntity>> {
        return localDatasource.getStories()
    }
}