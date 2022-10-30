package com.story.app

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.story.app.core.data.local.entity.StoryEntity
import com.story.app.core.data.remote.model.GeneralResponse
import com.story.app.core.data.remote.model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type

object DataDummy {
    fun loginResponse(): LoginResponse {
        val json = """
            {
                "error": false,
                "message": "success",
                "loginResult": {
                    "userId": "user-tkztmV-mCHGD628D",
                    "name": "dyas",
                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXRrenRtVi1tQ0hHRDYyOEQiLCJpYXQiOjE2NjcxMTcyMTZ9.sR3fwAxHj64b5aGa7KfuhlGaQzw-zUx2hn8t_kjt0W0"
                }
            }
        """.trimIndent()
        return Gson().fromJson(json, LoginResponse::class.java)
    }

    fun generalResponse() = GeneralResponse(
        false,
        "User created"
    )

    fun multipartFile() = MultipartBody.Part.create("dummyFile".toRequestBody())

    fun getStories(): List<StoryEntity>{
        val json = """
            [
                {
                    "id":"story-Yt41Z0zJlmiDpJ_O",
                    "name":"Tri 2","description":
                    "test",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667116639270_5GyOKhkV.jpg",
                    "createdAt":"2022-10-30T07:57:19.278Z",
                    "lat":-6.8570527,
                    "lon":107.5322845
                },
                {
                    "id":"story-q-N_8-au4cP5Xp--",
                    "name":"Tri 2",
                    "description":"test",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667115505741_gd1fjMip.jpg",
                    "createdAt":"2022-10-30T07:38:25.744Z",
                    "lat":-6.8571033,
                    "lon":107.5324641
                },
                {
                    "id":"story-phZv-UR2pV5a2YBZ",
                    "name":"MF",
                    "description":"g",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667114434086_N0K5X9kK.jpg",
                    "createdAt":"2022-10-30T07:20:34.088Z",
                    "lat":-7.1273527,
                    "lon":107.7028562
                },
                {
                    "id":"story-b03HM_siukOoFvrZ",
                    "name":"joko",
                    "description":"final tes jakarta geser dikit",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667114101740_jBTfX4_p.jpg",
                    "createdAt":"2022-10-30T07:15:01.743Z",
                    "lat":-6.1692047,
                    "lon":106.83822
                },
                {
                    "id":"story-34GERswBgrFdqflA",
                    "name":"joko",
                    "description":"tes koarmada jakarta",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667113892387_cJ5aBqe-.jpg",
                    "createdAt":"2022-10-30T07:11:32.390Z",
                    "lat":-6.1638117,
                    "lon":106.83916
                },
                {
                    "id":"story-LVjUtV4NEeEBY8n6",
                    "name":"joko",
                    "description":"tes koarmada jakarta",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667113848418_1HRcQIon.jpg",
                    "createdAt":"2022-10-30T07:10:48.421Z",
                    "lat":-6.8951607,"lon":107.60794
                },
                {
                    "id":"story-1oJAIVFHUsZ4DMw9",
                    "name":"joko",
                    "description":"tes permission d monas",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667113470048__F_kHIPv.jpg",
                    "createdAt":"2022-10-30T07:04:30.051Z",
                    "lat":-6.1754584,
                    "lon":106.82727
                },
                {
                    "id":"story-JlEyRMVRPnuhsRY4",
                    "name":"Tri 2",
                    "description":"hot crazy",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667113314630_8EKFv-Wa.jpg",
                    "createdAt":"2022-10-30T07:01:54.631Z",
                    "lat":-6.8570329,"lon":107.532414
                },
                {
                    "id":"story-MiDdIWITEM0kGq2L",
                    "name":"joko",
                    "description":"tes galery dan lapangan banteng, masih jakarta",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667112572752_w-6y15Ro.jpg",
                    "createdAt":"2022-10-30T06:49:32.754Z",
                    "lat":-6.1704226,"lon":106.83502
                },
                {
                    "id":"story-fjQZaR7nTaoGazC1",
                    "name":"joko",
                    "description":"tes jakarta dan camerax",
                    "photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1667112478961_-94LQZ2I.jpg",
                    "createdAt":"2022-10-30T06:47:58.964Z",
                    "lat":-6.16527,"lon":106.83734
                }
            ]
        """.trimIndent()
        val listStory: Type = object : TypeToken<List<StoryEntity>>() {}.type
        return Gson().fromJson(json, listStory)
    }
}