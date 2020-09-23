package com.example.countrysearch.model

import java.io.Serializable

data class CountryResponseItem(
    var alpha2Code: String? = "",
    var alpha3Code: String? = "",
    var altSpellings: List<String>? = listOf(),
    var area: Double? = 0.0,
    var borders: List<String>? = listOf(),
    var callingCodes: List<String>? = listOf(),
    var capital: String? = "",
    var cioc: String? = "",
    var currencies: List<Currency>? = listOf(),
    var demonym: String? = "",
    var flag: String? = "",
    var gini: Double? = 0.0,
    var languages: List<Language>? = listOf(),
    var latlng: List<Double>? = listOf(),
    var name: String = "",
    var nativeName: String? = "",
    var numericCode: String? = "",
    var population: Int? = 0,
    var region: String? = "",
    var regionalBlocs: List<RegionalBloc>? = listOf(),
    var subregion: String? = "",
    var timezones: List<String>? = listOf(),
    var topLevelDomain: List<String>? = listOf(),
    var translations: Translations? = Translations()
):Serializable