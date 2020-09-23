package com.example.countrysearch.model.weather

data class WeatherDetailsItem(
    var date: String? = "",
    var time: String? = "",
    var IceProbability: Int? = 0,
    var IsDaylight: Boolean? = false,
    var RealFeelTemperature: RealFeelTemperature? = RealFeelTemperature(),
    var Temperature: Temperature? = Temperature(),
    var UVIndex: Int? = 0,
    var UVIndexText: String? = "",
    var IconPhrase: String? = "",
    var WeatherIcon: Int? = 0
//    var Wind: Wind? = Wind()
)