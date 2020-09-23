package com.example.countrysearch.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.model.weather.WeatherDetailsItem
import com.example.countrysearch.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val editTextContent = MutableLiveData<String>()
    val repo = MainRepository()
    val error by lazy { MutableLiveData<String>() }
    val countryData by lazy { MutableLiveData<MutableList<CountryResponseItem>>() }
    val searchData by lazy { MutableLiveData<MutableList<CountryResponseItem>>() }
    val weatherData by lazy { MutableLiveData<WeatherDetailsItem>() }
    private var searchJob: Job? = null

    internal fun getCountryList(): MutableLiveData<MutableList<CountryResponseItem>> {
        viewModelScope.launch {
            when (val response = repo.callCountryListApi()) {
                is Exception -> error.value = "Network Error. Please Try again"

                is String -> error.value = response

                is List<*> -> countryData.value = response as MutableList<CountryResponseItem>
            }
        }
        return countryData
    }


    fun searchData(text: String) {
        viewModelScope.launch {
            searchJob?.cancelAndJoin()
            searchJob = launch {
                if (text.isNullOrEmpty())
                    searchData.value = countryData.value
                else {
                    val list = countryData.value
                    val searchList = mutableListOf<CountryResponseItem>()
                    list?.forEach {
                        if (it.name.toLowerCase().startsWith(text.toLowerCase())) {
                            searchList.add(it)
                        }
                    }
                    searchData.value = searchList
                }
            }

        }
    }

    internal fun getLocationKey(cityname: String) {
        viewModelScope.launch {
            when (val response = repo.callLocationApi(cityname)) {
                is Exception -> error.value = "Network Error. Please Try again"

                is String -> error.value = response

                is List<*> -> {
                    val list = response.get(0) as WeatherDetailsItem
                    list.apply {
                        date = Util.getCurrentDate()
                        time = Util.getCurrentTime()
                    }
                    weatherData.value = list
                }
            }
        }
    }
}