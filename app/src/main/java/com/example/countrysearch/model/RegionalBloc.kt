package com.example.countrysearch.model

import java.io.Serializable

data class RegionalBloc(
    var acronym: String? = "",
    var name: String? = "",
    var otherAcronyms: List<Any>? = listOf(),
    var otherNames: List<Any>? = listOf()
): Serializable