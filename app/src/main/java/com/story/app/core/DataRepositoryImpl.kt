package com.story.app.core

import androidx.paging.PagingData
import com.story.app.core.data.local.entity.StoryEntity
import com.story.app.core.data.remote.model.GeneralResponse
import com.story.app.core.data.remote.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface DataRepositoryImpl {

    fun doLogin(email: String, password: String): Flow<Resource<LoginResponse>>

    fun doRegister(email: String, password: String, name: String): Flow<Resource<GeneralResponse>>

    fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Float,
        lon: Float
    ): Flow<Resource<GeneralResponse>>

    fun getStories(token: String): Flow<PagingData<StoryEntity>>

    fun getLocalStories(): Flow<List<StoryEntity>>
}