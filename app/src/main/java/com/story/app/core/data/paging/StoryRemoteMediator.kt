package com.story.app.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.story.app.core.data.local.entity.RemoteKeys
import com.story.app.core.data.local.entity.StoryEntity
import com.story.app.core.data.local.room.StoryDatabase
import com.story.app.core.data.remote.network.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val token: String,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
): RemoteMediator<Int, StoryEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {

            val responseData = apiService.getStories("Bearer $token", page.toString(), state.config.pageSize.toString(), "1").listStory

            val endOfPaginationReached = responseData?.isEmpty() ?: true

            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.dao().deleteRemoteKeys()
                    storyDatabase.dao().deleteStory()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData?.map {
                    RemoteKeys(id = it.id.toString(), prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { storyDatabase.dao().insertAll(keys) }
                val storyMap = responseData?.map {
                    with(it){
                        StoryEntity(
                            photoUrl, createdAt, name, description, lon, id.toString(), lat
                        )
                    }
                }
                storyDatabase.dao().insertStory(storyMap)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDatabase.dao().getRemoteKeysId(data.id.toString())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.dao().getRemoteKeysId(data.id.toString())
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDatabase.dao().getRemoteKeysId(id)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


}