package com.example.countrysearch.api

import com.example.countrysearch.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {



    fun makeRetrofitService(): Api {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)
    }


}