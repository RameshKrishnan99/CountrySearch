package com.example.countrysearch.model

import java.io.Serializable

data class Currency(
    var code: String? = "",
    var name: String? = "",
    var symbol: String? = ""
): Serializable