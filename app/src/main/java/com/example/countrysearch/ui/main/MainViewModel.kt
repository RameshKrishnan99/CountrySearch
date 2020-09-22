package com.example.countrysearch.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countrysearch.model.CountryResponseItem
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val editTextContent = MutableLiveData<String>()
    val repo = MainRepository()
    val error by lazy { MutableLiveData<String>() }
    val countryData by lazy { MutableLiveData<MutableList<CountryResponseItem>>() }



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


}