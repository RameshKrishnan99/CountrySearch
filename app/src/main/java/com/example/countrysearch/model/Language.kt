package com.example.countrysearch.model

import java.io.Serializable

data class Language(
    var iso639_1: String? = "",
    var iso639_2: String? = "",
    var name: String? = "",
    var nativeName: String? = ""
): Serializable