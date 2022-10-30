package com.story.app.core.data.local

import com.story.app.core.data.local.room.StoryDao

class LocalDatasource (
    private val storyDao: StoryDao
) {
    fun getStories() = storyDao.getAllStories()
}