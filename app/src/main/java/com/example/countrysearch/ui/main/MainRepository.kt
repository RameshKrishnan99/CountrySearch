package com.example.countrysearch.ui.main

import android.util.Log
import com.example.countrysearch.api.Api
import com.example.countrysearch.api.ApiFactory
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.model.LocationkeyItem
import com.example.countrysearch.model.weather.WeatherDetailsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {
    private val TAG: String = "MainRepository"
    private var api: Api
    private var weatherApi: Api

    init {
        api = ApiFactory.makeRetrofitService()
        weatherApi = ApiFactory.weatherService()
    }

    suspend fun callCountryListApi(): Any {
        var response: Any = ""
        withContext(Dispatchers.IO) {
            response = try {
                val countryList: MutableList<CountryResponseItem> =
                    api.getCountryList()
                if (countryList.isNullOrEmpty())
                    "No Data Found"
                else
                    countryList
            } catch (e: Exception) {
                Log.e(TAG, ": " + e.toString())
                e

            }
        }
        return response
    }

    suspend fun callLocationApi(cityname: String): Any {
        var response: Any = ""
        withContext(Dispatchers.IO) {
            response = try {
                val keyList: ArrayList<LocationkeyItem> =
                    weatherApi.getLocationKey(cityname)
                if (keyList.isNullOrEmpty())
                    "No Data Found"
                else {
                    keyList.get(0).Key?.let {
                        getWeatherApi(it)
                    } ?: ""

                }
            } catch (e: Exception) {
                Log.e(TAG, ": " + e.toString())
                e

            }
        }
        return response
    }

    suspend fun getWeatherApi(key: String): Any {
        var response: Any = ""
        withContext(Dispatchers.IO) {
            response = try {
                val keyList: ArrayList<WeatherDetailsItem> =
                    weatherApi.getWeatherApi(key)
                if (keyList.isNullOrEmpty())
                    "No Data Found"
                else
                    keyList
            } catch (e: Exception) {
                Log.e(TAG, ": " + e.toString())
                e

            }
        }
        return response
    }
}