package com.example.countrysearch.api

import com.example.countrysearch.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiFactory {



    fun makeRetrofitService(): Api {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)
    }

    fun weatherService(): Api {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)
    }

}