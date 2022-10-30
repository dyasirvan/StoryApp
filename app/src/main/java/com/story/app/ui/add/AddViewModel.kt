package com.story.app.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.story.app.core.DataRepository
import okhttp3.MultipartBody

class AddViewModel(private var dataRepository: DataRepository) : ViewModel() {

    private lateinit var token: String
    private lateinit var file: MultipartBody.Part
    private lateinit var description: String
    private var lat: String? = null
    private var lon: String? = null

    fun setStoryParam(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: String? = null,
        lon: String? = null
    ) {
        this.token = token
        this.file = file
        this.description = description
        this.lat = lat
        this.lon = lon
    }

    fun postStory() = dataRepository.addNewStory("Bearer $token", file, description, lat, lon).asLiveData()

}