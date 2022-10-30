package com.story.app.core.di

import androidx.room.Room
import com.story.app.common.SharedPreferenceProvider
import com.story.app.core.DataRepository
import com.story.app.core.data.local.LocalDatasource
import com.story.app.core.data.local.room.StoryDatabase
import com.story.app.core.data.paging.StoryPagingSource
import com.story.app.core.data.remote.RemoteDatasource
import com.story.app.core.data.remote.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val databaseModule = module {
    factory { get<StoryDatabase>().dao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java, "story_db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()


    }

    single {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

}

val repositoryModule = module {
    single { RemoteDatasource(get(), get()) }
    single { SharedPreferenceProvider(get()) }
    single { DataRepository(get(), get(), get(), get()) }
    single { StoryPagingSource(get(), get()) }
    single { LocalDatasource(get()) }
}