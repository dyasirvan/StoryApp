package com.story.app.core.data.remote.model

import com.google.gson.annotations.SerializedName

data class GeneralResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
