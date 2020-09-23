package com.example.countrysearch.api

import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.model.LocationkeyItem
import com.example.countrysearch.model.weather.WeatherDetailsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("/rest/v2/all")
    suspend fun getCountryList(): ArrayList<CountryResponseItem>

    @GET("/locations/v1/cities/search?apikey=TxSCaVMsM3exMyGa6xtJWSzGG3QYCjj1&language=en")
    suspend fun getLocationKey(@Query("q") cityname: String): ArrayList<LocationkeyItem>


    @GET("/forecasts/v1/hourly/1hour/{location}?apikey=TxSCaVMsM3exMyGa6xtJWSzGG3QYCjj1&language=en&details=true&metric=true")
    suspend fun getWeatherApi(@Path("location") location: String): ArrayList<WeatherDetailsItem>


}