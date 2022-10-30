package com.story.app.core.data.remote.network

import com.story.app.core.data.remote.model.GeneralResponse
import com.story.app.core.data.remote.model.LoginResponse
import com.story.app.core.data.remote.model.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun doRegister(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): GeneralResponse


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: String? = null,
        @Query("size") size: String? = null,
        @Query("location") location: String? = null,
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: String,
        @Part("lat") lat: String?,
        @Part("lon") lon: String?
    ): GeneralResponse
}