package com.example.countrysearch.ui.main

import android.util.Log
import com.example.countrysearch.api.Api
import com.example.countrysearch.api.ApiFactory
import com.example.countrysearch.model.CountryResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {
    private val TAG: String = "MainRepository"
    private var api: Api

    init {
        api = ApiFactory.makeRetrofitService()
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
                Log.e(TAG, ": "+e.toString())
                e

            }
        }
        return response
    }
}