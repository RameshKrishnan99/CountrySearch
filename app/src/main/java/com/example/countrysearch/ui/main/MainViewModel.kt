package com.example.countrysearch.ui.main

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.countrysearch.R
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.model.weather.WeatherDetailsItem
import com.example.countrysearch.util.Connection
import com.example.countrysearch.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var app: Application
    val editTextContent = MutableLiveData<String>()
    val repo = MainRepository()
    val error by lazy { MutableLiveData<String>() }
    val countryData by lazy { MutableLiveData<MutableList<CountryResponseItem>>() }
    val searchData by lazy { MutableLiveData<MutableList<CountryResponseItem>>() }
    val weatherData by lazy { MutableLiveData<WeatherDetailsItem>() }
    val showProgress by lazy { MutableLiveData<Boolean>() }
    val connection by lazy { MutableLiveData<Boolean>() }
    val location by lazy { MutableLiveData<Boolean>() }
    private var searchJob: Job? = null

    init {
        app = application
    }

    internal fun getCountryList(): MutableLiveData<MutableList<CountryResponseItem>> {
        showProgress.value = true
        viewModelScope.launch {
            when (val response = repo.callCountryListApi()) {
                is Exception -> connection.value = false

                is String -> error.value = response

                is List<*> -> {
                     val modelList = response as MutableList<CountryResponseItem>
                    modelList.sortBy { it.name }
                    countryData.value = modelList
                }
            }
            showProgress.value = false

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

    fun checkConnectivity() {
        showProgress.value = true
        connection.value = Connection.isOnline(app)
        showProgress.value = false
    }

    fun getDate() = Util.getCurrentDate()

    fun getTime() = Util.getCurrentTime()

    fun getDrawable(model: WeatherDetailsItem?): Drawable {
        if (model != null) {
            if (model.IsDaylight ?: false) {
                model.IconPhrase?.let {
                    if (it.contains("sun"))
                        return app.resources.getDrawable(R.drawable.sunny)
                    if (it.contains("cloud"))
                        return app.resources.getDrawable(R.drawable.ic_clouds)
                }
            } else {
                return app.resources.getDrawable(R.drawable.ic_night)
            }
        }
        return app.resources.getDrawable(R.drawable.sunny)
    }

}