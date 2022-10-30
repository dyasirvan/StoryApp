package com.story.app

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.story.app.core.data.local.entity.StoryEntity

class PageDataSourceTest :
    PagingSource<Int, LiveData<List<StoryEntity>>>() {

    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}
