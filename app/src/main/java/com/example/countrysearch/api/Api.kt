package com.example.countrysearch.api

import com.example.countrysearch.model.CountryResponseItem
import retrofit2.http.GET

interface Api {
    @GET("/rest/v2/all")
    suspend fun getCountryList(): ArrayList<CountryResponseItem>
}