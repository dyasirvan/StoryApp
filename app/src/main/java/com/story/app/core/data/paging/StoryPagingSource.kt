package com.story.app.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.story.app.core.data.local.entity.StoryEntity
import com.story.app.core.data.local.room.StoryDatabase
import com.story.app.core.data.remote.network.ApiService
import kotlinx.coroutines.flow.Flow

class StoryPagingSource(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    ) {

    fun getStories(token: String): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator =
                StoryRemoteMediator(token, apiService, storyDatabase)
            ,
            pagingSourceFactory = {
                storyDatabase.dao().getPagingStories()
            }
        ).flow
    }

}